package fr.inria.familiar.webfml.dto;

import java.util.List;

/**
 * Response DTO for interpret operations
 */
public class InterpretResponse {
    private List<String> varIds;
    private String lastVar;

    public InterpretResponse() {}

    public InterpretResponse(List<String> varIds, String lastVar) {
        this.varIds = varIds;
        this.lastVar = lastVar;
    }

    public List<String> getVarIds() { return varIds; }
    public void setVarIds(List<String> varIds) { this.varIds = varIds; }
    public String getLastVar() { return lastVar; }
    public void setLastVar(String lastVar) { this.lastVar = lastVar; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private List<String> varIds;
        private String lastVar;

        public Builder varIds(List<String> varIds) { this.varIds = varIds; return this; }
        public Builder lastVar(String lastVar) { this.lastVar = lastVar; return this; }
        public InterpretResponse build() { return new InterpretResponse(varIds, lastVar); }
    }
}
