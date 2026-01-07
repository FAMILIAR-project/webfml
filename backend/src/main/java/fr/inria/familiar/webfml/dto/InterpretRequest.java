package fr.inria.familiar.webfml.dto;

import lombok.Data;

/**
 * Request DTO for interpreting FAMILIAR commands
 */
@Data
public class InterpretRequest {
    private String command;
}
