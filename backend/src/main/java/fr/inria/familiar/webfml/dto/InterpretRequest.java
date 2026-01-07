package fr.inria.familiar.webfml.dto;

/**
 * Request DTO for interpreting FAMILIAR commands
 */
public class InterpretRequest {
    private String command;

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }
}
