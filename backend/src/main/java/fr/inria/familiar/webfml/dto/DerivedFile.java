package fr.inria.familiar.webfml.dto;

import java.util.List;

/**
 * Represents a file after template processing (derivation)
 */
public class DerivedFile {
    private String path;
    private String originalContent;
    private String derivedContent;
    private boolean hasConditionals;
    private List<String> usedFeatures;

    public DerivedFile() {}

    public DerivedFile(String path, String originalContent, String derivedContent,
                       boolean hasConditionals, List<String> usedFeatures) {
        this.path = path;
        this.originalContent = originalContent;
        this.derivedContent = derivedContent;
        this.hasConditionals = hasConditionals;
        this.usedFeatures = usedFeatures;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getDerivedContent() {
        return derivedContent;
    }

    public void setDerivedContent(String derivedContent) {
        this.derivedContent = derivedContent;
    }

    public boolean isHasConditionals() {
        return hasConditionals;
    }

    public void setHasConditionals(boolean hasConditionals) {
        this.hasConditionals = hasConditionals;
    }

    public List<String> getUsedFeatures() {
        return usedFeatures;
    }

    public void setUsedFeatures(List<String> usedFeatures) {
        this.usedFeatures = usedFeatures;
    }
}
