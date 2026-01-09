package fr.inria.familiar.webfml.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.inria.familiar.webfml.dto.FileTreeNode;
import fr.inria.familiar.webfml.dto.ProjectMetadata;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Service for managing template projects.
 * Projects are stored in repository/projects/{projectId}/
 */
@Service
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    @Value("${webfml.workspace.base-path:repository}")
    private String workspacePath;

    private final ObjectMapper objectMapper;

    public ProjectService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Get the projects directory path
     */
    private Path getProjectsPath() {
        return Paths.get(workspacePath, "projects");
    }

    /**
     * Get a specific project directory path
     */
    private Path getProjectPath(String projectId) {
        return getProjectsPath().resolve(projectId);
    }

    /**
     * Upload a project from a ZIP file
     */
    public ProjectMetadata uploadZip(MultipartFile file, String name) throws IOException {
        String projectId = UUID.randomUUID().toString();
        Path projectPath = getProjectPath(projectId);
        Path sourcePath = projectPath.resolve("source");

        // Create directories
        Files.createDirectories(sourcePath);

        // Extract ZIP
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = sourcePath.resolve(entry.getName()).normalize();

                // Security check: prevent path traversal
                if (!entryPath.startsWith(sourcePath)) {
                    throw new SecurityException("Zip entry outside target directory: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }

        // Create and save metadata
        ProjectMetadata metadata = new ProjectMetadata();
        metadata.setId(projectId);
        metadata.setName(name);
        metadata.setCreatedAt(LocalDateTime.now());
        metadata.setSourceType("zip");
        metadata.setFiles(listAllFiles(sourcePath));

        saveMetadata(projectPath, metadata);

        log.info("Uploaded project '{}' with ID: {}", name, projectId);
        return metadata;
    }

    /**
     * Register a project from a filesystem path
     */
    public ProjectMetadata registerFromPath(String filesystemPath, String name) throws IOException {
        Path sourcePath = Paths.get(filesystemPath);

        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException("Path does not exist or is not a directory: " + filesystemPath);
        }

        String projectId = UUID.randomUUID().toString();
        Path projectPath = getProjectPath(projectId);
        Path targetSourcePath = projectPath.resolve("source");

        // Create project directory
        Files.createDirectories(projectPath);

        // Copy the source directory
        FileUtils.copyDirectory(sourcePath.toFile(), targetSourcePath.toFile());

        // Create and save metadata
        ProjectMetadata metadata = new ProjectMetadata();
        metadata.setId(projectId);
        metadata.setName(name);
        metadata.setCreatedAt(LocalDateTime.now());
        metadata.setSourceType("filesystem");
        metadata.setOriginalPath(filesystemPath);
        metadata.setFiles(listAllFiles(targetSourcePath));

        saveMetadata(projectPath, metadata);

        log.info("Registered project '{}' from path '{}' with ID: {}", name, filesystemPath, projectId);
        return metadata;
    }

    /**
     * Associate a project with a feature model variable
     */
    public ProjectMetadata associateWithFM(String projectId, String fmVariableId) throws IOException {
        Path projectPath = getProjectPath(projectId);
        ProjectMetadata metadata = loadMetadata(projectPath);

        metadata.setAssociatedFM(fmVariableId);
        saveMetadata(projectPath, metadata);

        log.info("Associated project {} with FM {}", projectId, fmVariableId);
        return metadata;
    }

    /**
     * Dissociate a project from its feature model (clear FM binding)
     */
    public ProjectMetadata dissociateFromFM(String projectId) throws IOException {
        Path projectPath = getProjectPath(projectId);
        ProjectMetadata metadata = loadMetadata(projectPath);

        String previousFM = metadata.getAssociatedFM();
        metadata.setAssociatedFM(null);
        saveMetadata(projectPath, metadata);

        log.info("Dissociated project {} from FM {}", projectId, previousFM);
        return metadata;
    }

    /**
     * List all projects
     */
    public List<ProjectMetadata> listProjects() throws IOException {
        Path projectsPath = getProjectsPath();

        if (!Files.exists(projectsPath)) {
            Files.createDirectories(projectsPath);
            return new ArrayList<>();
        }

        List<ProjectMetadata> projects = new ArrayList<>();

        try (Stream<Path> stream = Files.list(projectsPath)) {
            for (Path projectPath : stream.collect(Collectors.toList())) {
                if (Files.isDirectory(projectPath)) {
                    try {
                        ProjectMetadata metadata = loadMetadata(projectPath);
                        projects.add(metadata);
                    } catch (Exception e) {
                        log.warn("Could not load metadata for project: {}", projectPath, e);
                    }
                }
            }
        }

        return projects;
    }

    /**
     * Get project metadata
     */
    public ProjectMetadata getProject(String projectId) throws IOException {
        Path projectPath = getProjectPath(projectId);
        return loadMetadata(projectPath);
    }

    /**
     * Delete a project
     */
    public void deleteProject(String projectId) throws IOException {
        Path projectPath = getProjectPath(projectId);

        if (!Files.exists(projectPath)) {
            throw new IllegalArgumentException("Project not found: " + projectId);
        }

        FileUtils.deleteDirectory(projectPath.toFile());
        log.info("Deleted project: {}", projectId);
    }

    /**
     * Get project file tree
     */
    public List<FileTreeNode> getProjectFiles(String projectId) throws IOException {
        Path sourcePath = getProjectPath(projectId).resolve("source");

        if (!Files.exists(sourcePath)) {
            return new ArrayList<>();
        }

        // Return children of source folder directly, not the source folder itself
        File[] children = sourcePath.toFile().listFiles();
        if (children == null || children.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.stream(children)
            .filter(this::shouldIncludeFile)
            .sorted((a, b) -> {
                if (a.isDirectory() != b.isDirectory()) {
                    return a.isDirectory() ? -1 : 1;
                }
                return a.getName().compareToIgnoreCase(b.getName());
            })
            .map(f -> buildFileTree(f, ""))
            .collect(Collectors.toList());
    }

    /**
     * Get file content from a project
     */
    public String getFileContent(String projectId, String filePath) throws IOException {
        Path sourcePath = getProjectPath(projectId).resolve("source");
        Path targetFile = sourcePath.resolve(filePath).normalize();

        // Security check
        if (!targetFile.startsWith(sourcePath)) {
            throw new SecurityException("Access denied: path outside project");
        }

        if (!Files.exists(targetFile) || Files.isDirectory(targetFile)) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        return Files.readString(targetFile, StandardCharsets.UTF_8);
    }

    /**
     * Get the source path for a project
     */
    public Path getProjectSourcePath(String projectId) {
        return getProjectPath(projectId).resolve("source");
    }

    /**
     * Save metadata to meta.json
     */
    private void saveMetadata(Path projectPath, ProjectMetadata metadata) throws IOException {
        Path metaFile = projectPath.resolve("meta.json");
        objectMapper.writerWithDefaultPrettyPrinter()
            .writeValue(metaFile.toFile(), metadata);
    }

    /**
     * Load metadata from meta.json
     */
    private ProjectMetadata loadMetadata(Path projectPath) throws IOException {
        Path metaFile = projectPath.resolve("meta.json");

        if (!Files.exists(metaFile)) {
            throw new IllegalArgumentException("Project metadata not found");
        }

        return objectMapper.readValue(metaFile.toFile(), ProjectMetadata.class);
    }

    /**
     * List all files in a directory recursively
     */
    private List<String> listAllFiles(Path directory) throws IOException {
        List<String> files = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isRegularFile)
                .forEach(path -> {
                    String relativePath = directory.relativize(path).toString();
                    files.add(relativePath);
                });
        }

        Collections.sort(files);
        return files;
    }

    /**
     * Build file tree node recursively
     */
    private FileTreeNode buildFileTree(File file, String relativePath) {
        FileTreeNode node = new FileTreeNode();
        node.setLabel(file.getName());
        node.setType(file.isDirectory() ? "folder" : "file");
        node.setLeaf(!file.isDirectory());

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                List<FileTreeNode> childNodes = Arrays.stream(children)
                    .filter(this::shouldIncludeFile) // Filter unwanted files
                    .sorted((a, b) -> {
                        // Folders first, then by name
                        if (a.isDirectory() != b.isDirectory()) {
                            return a.isDirectory() ? -1 : 1;
                        }
                        return a.getName().compareToIgnoreCase(b.getName());
                    })
                    .map(f -> buildFileTree(f, relativePath.isEmpty() ? f.getName() : relativePath + "/" + f.getName()))
                    .collect(Collectors.toList());
                node.setChildren(childNodes);
                node.setExpanded(true);
            }
        }

        return node;
    }

    /**
     * Check if a file/folder should be included in the tree
     */
    private boolean shouldIncludeFile(File file) {
        String name = file.getName();
        // Skip hidden files, macOS metadata, and other unwanted files
        return !name.startsWith(".") &&
               !name.equals("__MACOSX") &&
               !name.equals("Thumbs.db") &&
               !name.endsWith(".DS_Store");
    }
}
