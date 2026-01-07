package fr.inria.familiar.webfml.service;

import fr.inria.familiar.webfml.dto.FileTreeNode;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing workspace files
 */
@Service
public class WorkspaceService {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceService.class);

    @Value("${webfml.workspace.base-path:repository}")
    private String workspacePath;

    /**
     * List all files in the workspace
     */
    public List<FileTreeNode> listFiles() {
        File workspace = new File(workspacePath);
        if (!workspace.exists()) {
            workspace.mkdirs();
        }

        return Arrays.asList(buildFileTree(workspace));
    }

    /**
     * Build a file tree recursively
     */
    private FileTreeNode buildFileTree(File file) {
        FileTreeNode node = new FileTreeNode();
        node.setLabel(file.getName());
        node.setType(file.isDirectory() ? "folder" : "file");
        node.setLeaf(!file.isDirectory());

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null && children.length > 0) {
                List<FileTreeNode> childNodes = Arrays.stream(children)
                        .filter(f -> f.isDirectory() || f.getName().endsWith(".fml") || f.getName().endsWith(".dimacs"))
                        .map(this::buildFileTree)
                        .collect(Collectors.toList());
                node.setChildren(childNodes);
                node.setExpanded(true);
            }
        }

        return node;
    }

    /**
     * Load file content
     */
    public String loadFile(String filename) throws IOException {
        Path filePath = Paths.get(workspacePath, filename);
        validatePath(filePath);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    /**
     * Save file content
     */
    public void saveFile(String filename, String content) throws IOException {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        Path filePath = Paths.get(workspacePath, filename);
        validatePath(filePath);

        // Ensure parent directories exist
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, content, StandardCharsets.UTF_8);
        log.info("Saved file: {}", filename);
    }

    /**
     * Create a new file
     */
    public void createFile(String name) throws IOException {
        Path filePath = Paths.get(workspacePath, name);
        validatePath(filePath);

        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
        log.info("Created file: {}", name);
    }

    /**
     * Delete a file
     */
    public void deleteFile(String name) throws IOException {
        Path filePath = Paths.get(workspacePath, name);
        validatePath(filePath);

        Files.delete(filePath);
        log.info("Deleted file: {}", name);
    }

    /**
     * Create a new folder
     */
    public void createFolder(String name) throws IOException {
        Path folderPath = Paths.get(workspacePath, name);
        validatePath(folderPath);

        Files.createDirectories(folderPath);
        log.info("Created folder: {}", name);
    }

    /**
     * Delete a folder
     */
    public void deleteFolder(String name) throws IOException {
        Path folderPath = Paths.get(workspacePath, name);
        validatePath(folderPath);

        FileUtils.deleteDirectory(folderPath.toFile());
        log.info("Deleted folder: {}", name);
    }

    /**
     * Validate that the path is within the workspace (prevent directory traversal)
     */
    private void validatePath(Path path) throws IOException {
        Path normalizedPath = path.normalize();
        Path workspaceAbsPath = Paths.get(workspacePath).toAbsolutePath().normalize();

        if (!normalizedPath.startsWith(workspaceAbsPath)) {
            throw new SecurityException("Access denied: path outside workspace");
        }
    }
}
