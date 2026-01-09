package fr.inria.familiar.webfml.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Metadata for a template project
 */
public class ProjectMetadata {
    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private String associatedFM;      // Variable ID of associated feature model
    private String sourceType;        // "zip", "filesystem", or "bundled"
    private String originalPath;      // For filesystem references
    private List<String> files;       // List of all file paths in the project

    public ProjectMetadata() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssociatedFM() {
        return associatedFM;
    }

    public void setAssociatedFM(String associatedFM) {
        this.associatedFM = associatedFM;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
