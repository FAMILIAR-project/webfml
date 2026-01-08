package fr.inria.familiar.webfml.controller;

import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;
import fr.inria.familiar.webfml.service.ConfigurationService;
import fr.inria.familiar.webfml.service.FamiliarInterpreterService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for interactive feature model configuration
 */
@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationController.class);

    private final FamiliarInterpreterService interpreterService;
    private final ConfigurationService configurationService;

    public ConfigurationController(FamiliarInterpreterService interpreterService,
                                    ConfigurationService configurationService) {
        this.interpreterService = interpreterService;
        this.configurationService = configurationService;
    }

    /**
     * Start configuration for a feature model variable, or load an existing configuration
     */
    @PostMapping("/start")
    public ResponseEntity<?> startConfiguration(
            @RequestParam String variableId,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            log.info("Starting/loading configuration for variable {} in session {}", variableId, sessionId);

            // Check available variables first
            var availableVars = interpreterService.getAllVariableIds(sessionId);
            if (!availableVars.contains(variableId)) {
                log.warn("Variable {} not found. Available: {}", variableId, availableVars);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Variable not found: " + variableId +
                        ". Available variables: " + availableVars +
                        ". Try re-interpreting your feature model."));
            }

            Variable variable = interpreterService.getVariable(sessionId, variableId);

            // Check if it's an existing configuration
            if (variable instanceof ConfigurationVariable) {
                log.info("Loading existing configuration: {}", variableId);
                ConfigurationVariable config = (ConfigurationVariable) variable;
                Map<String, Object> result = configurationService.loadConfiguration(sessionId, variableId, config);
                return ResponseEntity.ok(result);
            }

            // Otherwise, try to create a new configuration from a feature model
            if (variable instanceof FeatureModelVariable) {
                FeatureModelVariable fmv = (FeatureModelVariable) variable;
                Map<String, Object> result = configurationService.startConfiguration(sessionId, variableId, fmv);
                return ResponseEntity.ok(result);
            }

            // Neither FM nor Configuration
            String actualType = variable.getClass().getSimpleName();
            log.warn("Variable {} is neither FeatureModel nor Configuration, actual type: {}", variableId, actualType);
            return ResponseEntity.badRequest()
                .body(Map.of("error", variableId + " is not a feature model or configuration (type: " + actualType + ")"));

        } catch (Exception e) {
            log.error("Error starting configuration: ", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Select a feature in the configuration
     */
    @PostMapping("/select")
    public ResponseEntity<?> selectFeature(
            @RequestParam String feature,
            HttpSession session) {
        try {
            Map<String, Object> result = configurationService.selectFeature(session.getId(), feature);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error selecting feature: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Deselect a feature in the configuration
     */
    @PostMapping("/deselect")
    public ResponseEntity<?> deselectFeature(
            @RequestParam String feature,
            HttpSession session) {
        try {
            Map<String, Object> result = configurationService.deselectFeature(session.getId(), feature);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error deselecting feature: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Unselect (reset) a feature in the configuration
     */
    @PostMapping("/unselect")
    public ResponseEntity<?> unselectFeature(
            @RequestParam String feature,
            HttpSession session) {
        try {
            Map<String, Object> result = configurationService.unselectFeature(session.getId(), feature);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error unselecting feature: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Auto-complete the configuration using SAT solver
     */
    @PostMapping("/auto-complete")
    public ResponseEntity<?> autoComplete(
            @RequestParam(defaultValue = "MAX") String mode,
            HttpSession session) {
        try {
            Map<String, Object> result = configurationService.autoComplete(session.getId(), mode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error auto-completing configuration: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get current configuration state
     */
    @GetMapping("/state")
    public ResponseEntity<?> getState(HttpSession session) {
        try {
            Map<String, Object> result = configurationService.getState(session.getId());
            if (result == null) {
                return ResponseEntity.ok(Map.of("active", false));
            }
            result.put("active", true);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getting state: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Save configuration to a variable
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveConfiguration(
            @RequestParam(required = false) String newVariableId,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            ConfigurationVariable config = configurationService.saveConfiguration(sessionId, newVariableId);

            if (config == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "No active configuration"));
            }

            String varId = newVariableId != null ? newVariableId : config.getIdentifier();

            return ResponseEntity.ok(Map.of(
                "variableId", varId,
                "value", config.getValue()
            ));
        } catch (Exception e) {
            log.error("Error saving configuration: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
