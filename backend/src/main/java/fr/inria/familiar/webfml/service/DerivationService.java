package fr.inria.familiar.webfml.service;

import fr.familiar.variable.ConfigurationVariable;
import fr.inria.familiar.webfml.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Service for deriving product variants from template projects.
 * Uses TemplateEngine to process conditional blocks based on feature configurations.
 */
@Service
public class DerivationService {

    private static final Logger log = LoggerFactory.getLogger(DerivationService.class);

    // File extensions that should be treated as text and processed
    private static final Set<String> TEXT_EXTENSIONS = Set.of(
        "java", "c", "cpp", "h", "hpp", "py", "js", "ts", "jsx", "tsx",
        "html", "css", "scss", "less", "xml", "json", "yaml", "yml",
        "md", "txt", "properties", "cfg", "conf", "ini", "sh", "bash",
        "sql", "go", "rs", "rb", "php", "swift", "kt", "scala", "gradle",
        "makefile", "dockerfile", "svg", "toml"
    );

    private final ProjectService projectService;
    private final ConfigurationService configurationService;
    private final TemplateEngine templateEngine;

    public DerivationService(ProjectService projectService,
                            ConfigurationService configurationService,
                            TemplateEngine templateEngine) {
        this.projectService = projectService;
        this.configurationService = configurationService;
        this.templateEngine = templateEngine;
    }

    /**
     * Derive a variant from a project using the current session configuration or a specific config variable.
     */
    public DerivationResult derive(String sessionId, String projectId, String configId) throws IOException {
        ConfigurationVariable config;

        if (configId != null && !configId.isEmpty()) {
            // Use specific configuration variable
            config = configurationService.getConfigurationByVariableId(sessionId, configId);
            if (config == null) {
                throw new IllegalStateException("Configuration variable not found: " + configId);
            }
        } else {
            // Use session configuration
            config = configurationService.getConfiguration(sessionId);
            if (config == null) {
                throw new IllegalStateException("No active configuration session. Start a configuration first.");
            }
        }

        ProjectMetadata project = projectService.getProject(projectId);
        Path sourcePath = projectService.getProjectSourcePath(projectId);

        Set<String> selected = config.getSelected();
        Set<String> deselected = config.getDeselected();

        log.info("Deriving variant for project '{}' with {} selected, {} deselected features",
            project.getName(), selected.size(), deselected.size());

        DerivationResult result = new DerivationResult();
        result.setProjectId(projectId);
        result.setProjectName(project.getName());
        result.setConfigurationId(config.getIdentifier());
        result.setSelectedFeatures(new ArrayList<>(selected));
        result.setDeselectedFeatures(new ArrayList<>(deselected));

        List<DerivedFile> derivedFiles = new ArrayList<>();
        List<TemplateError> errors = new ArrayList<>();

        // Process all files (excluding macOS metadata and hidden files)
        try (Stream<Path> stream = Files.walk(sourcePath)) {
            stream.filter(Files::isRegularFile)
                .filter(this::shouldProcessFile)
                .forEach(filePath -> {
                    try {
                        String relativePath = sourcePath.relativize(filePath).toString();
                        DerivedFile derivedFile = processFile(filePath, relativePath, selected, deselected, errors);
                        derivedFiles.add(derivedFile);
                    } catch (IOException e) {
                        log.error("Error processing file: {}", filePath, e);
                        errors.add(new TemplateError(filePath.toString(), 0,
                            "Error reading file: " + e.getMessage(), "IO_ERROR"));
                    }
                });
        }

        // Sort files by path
        derivedFiles.sort(Comparator.comparing(DerivedFile::getPath));

        result.setFiles(derivedFiles);
        result.setErrors(errors);
        result.setHasErrors(!errors.isEmpty());

        return result;
    }

    /**
     * Preview a single file derivation.
     */
    public DerivedFile previewFile(String sessionId, String projectId, String filePath) throws IOException {
        ConfigurationVariable config = configurationService.getConfiguration(sessionId);
        if (config == null) {
            throw new IllegalStateException("No active configuration session.");
        }

        Path sourcePath = projectService.getProjectSourcePath(projectId);
        Path targetFile = sourcePath.resolve(filePath).normalize();

        if (!targetFile.startsWith(sourcePath)) {
            throw new SecurityException("Access denied: path outside project");
        }

        Set<String> selected = config.getSelected();
        Set<String> deselected = config.getDeselected();

        List<TemplateError> errors = new ArrayList<>();
        return processFile(targetFile, filePath, selected, deselected, errors);
    }

    /**
     * Download derived variant as ZIP.
     */
    public byte[] downloadZip(String sessionId, String projectId) throws IOException {
        DerivationResult result = derive(sessionId, projectId, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (DerivedFile file : result.getFiles()) {
                ZipEntry entry = new ZipEntry(file.getPath());
                zos.putNextEntry(entry);
                zos.write(file.getDerivedContent().getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
        }

        return baos.toByteArray();
    }

    /**
     * Process a single file through the template engine.
     */
    private DerivedFile processFile(Path filePath, String relativePath,
                                    Set<String> selected, Set<String> deselected,
                                    List<TemplateError> errors) throws IOException {
        DerivedFile derivedFile = new DerivedFile();
        derivedFile.setPath(relativePath);

        // Check if file should be processed as text
        String extension = getFileExtension(relativePath).toLowerCase();
        boolean isTextFile = TEXT_EXTENSIONS.contains(extension) ||
                             relativePath.toLowerCase().contains("makefile") ||
                             relativePath.toLowerCase().contains("dockerfile");

        if (isTextFile) {
            String originalContent = Files.readString(filePath, StandardCharsets.UTF_8);
            derivedFile.setOriginalContent(originalContent);

            // Check for conditionals
            boolean hasConditionals = templateEngine.hasConditionals(originalContent);
            derivedFile.setHasConditionals(hasConditionals);

            if (hasConditionals) {
                // Validate and report errors
                List<TemplateError> fileErrors = templateEngine.validate(originalContent, relativePath);
                errors.addAll(fileErrors);

                // Extract used features
                Set<String> usedFeatures = templateEngine.extractFeatureNames(originalContent);
                derivedFile.setUsedFeatures(new ArrayList<>(usedFeatures));

                // Process template
                String derivedContent = templateEngine.process(originalContent, selected, deselected);
                derivedFile.setDerivedContent(derivedContent);
            } else {
                derivedFile.setDerivedContent(originalContent);
                derivedFile.setUsedFeatures(new ArrayList<>());
            }
        } else {
            // Binary file - just copy
            byte[] bytes = Files.readAllBytes(filePath);
            String content = Base64.getEncoder().encodeToString(bytes);
            derivedFile.setOriginalContent("[Binary file]");
            derivedFile.setDerivedContent("[Binary file]");
            derivedFile.setHasConditionals(false);
            derivedFile.setUsedFeatures(new ArrayList<>());
        }

        return derivedFile;
    }

    /**
     * Get file extension from path.
     */
    private String getFileExtension(String path) {
        int lastDot = path.lastIndexOf('.');
        if (lastDot > 0 && lastDot < path.length() - 1) {
            return path.substring(lastDot + 1);
        }
        return "";
    }

    /**
     * Check if a file should be processed during derivation.
     * Filters out macOS metadata files, hidden files, and other unwanted files.
     */
    private boolean shouldProcessFile(Path filePath) {
        String pathStr = filePath.toString();
        String fileName = filePath.getFileName().toString();

        // Skip macOS metadata folders and files
        if (pathStr.contains("__MACOSX") || pathStr.contains("/.")) {
            return false;
        }

        // Skip hidden files and macOS resource fork files
        if (fileName.startsWith(".") || fileName.startsWith("._")) {
            return false;
        }

        // Skip common unwanted files
        if (fileName.equals("Thumbs.db") || fileName.equals(".DS_Store")) {
            return false;
        }

        return true;
    }
}
