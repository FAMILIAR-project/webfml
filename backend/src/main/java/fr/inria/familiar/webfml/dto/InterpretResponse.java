package fr.inria.familiar.webfml.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Response DTO for interpret operations
 */
@Data
@Builder
public class InterpretResponse {
    private List<String> varIds;
    private String lastVar;
}
