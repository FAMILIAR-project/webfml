package fr.inria.familiar.webfml.dto;

import java.util.List;

/**
 * Result of deriving a variant from a project using a configuration
 */
public class DerivationResult {
    private String projectId;
    private String projectName;
    private String configurationId;
    private List<String> selectedFeatures;
    private List<String> deselectedFeatures;
    private List<DerivedFile> files;
    private boolean hasErrors;
    private List<TemplateError> errors;

    public DerivationResult() {}

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public List<String> getSelectedFeatures() {
        return selectedFeatures;
    }

    public void setSelectedFeatures(List<String> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;
    }

    public List<String> getDeselectedFeatures() {
        return deselectedFeatures;
    }

    public void setDeselectedFeatures(List<String> deselectedFeatures) {
        this.deselectedFeatures = deselectedFeatures;
    }

    public List<DerivedFile> getFiles() {
        return files;
    }

    public void setFiles(List<DerivedFile> files) {
        this.files = files;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public List<TemplateError> getErrors() {
        return errors;
    }

    public void setErrors(List<TemplateError> errors) {
        this.errors = errors;
    }
}
