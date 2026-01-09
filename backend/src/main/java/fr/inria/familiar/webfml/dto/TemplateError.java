package fr.inria.familiar.webfml.dto;

/**
 * Represents an error found during template parsing or processing
 */
public class TemplateError {
    private String filePath;
    private int lineNumber;
    private String message;
    private String errorType; // SYNTAX, UNCLOSED_BLOCK, UNKNOWN_FEATURE

    public TemplateError() {}

    public TemplateError(String filePath, int lineNumber, String message, String errorType) {
        this.filePath = filePath;
        this.lineNumber = lineNumber;
        this.message = message;
        this.errorType = errorType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
