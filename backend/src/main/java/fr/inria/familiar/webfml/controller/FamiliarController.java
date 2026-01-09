package fr.inria.familiar.webfml.controller;

import fr.familiar.interpreter.FMLAssertionError;
import fr.familiar.interpreter.FMLFatalError;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.SetVariable;
import fr.familiar.variable.Variable;
import fr.inria.familiar.webfml.dto.InterpretRequest;
import fr.inria.familiar.webfml.dto.InterpretResponse;
import fr.inria.familiar.webfml.service.FamiliarInterpreterService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for FAMILIAR interpreter operations
 */
@RestController
@RequestMapping("/familiar")
public class FamiliarController {

    private static final Logger log = LoggerFactory.getLogger(FamiliarController.class);

    private final FamiliarInterpreterService interpreterService;

    public FamiliarController(FamiliarInterpreterService interpreterService) {
        this.interpreterService = interpreterService;
    }

    /**
     * Interpret a FAMILIAR command
     */
    @PostMapping("/interpret")
    public ResponseEntity<?> interpret(
            @RequestBody InterpretRequest request,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            log.info("Interpreting command for session: {}", sessionId);

            Variable lastVar = interpreterService.interpret(sessionId, request.getCommand());
            List<String> allVarIds = interpreterService.getAllVariableIds(sessionId);

            InterpretResponse response = InterpretResponse.builder()
                    .varIds(allVarIds)
                    .lastVar(lastVar != null ? lastVar.getIdentifier() + " = " + lastVar.getValue() : "")
                    .build();

            return ResponseEntity.ok(response);
        } catch (FMLAssertionError | FMLFatalError e) {
            log.error("FAMILIAR error: ", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("msgError", getStackTraceAsString(e)));
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("msgError", getStackTraceAsString(e)));
        }
    }

    /**
     * Evaluate a prompt command (without reset)
     */
    @PostMapping("/eval-prompt")
    public ResponseEntity<?> evalPrompt(
            @RequestParam String command,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            Variable lastVar = interpreterService.evalPrompt(sessionId, command);
            List<String> allVarIds = interpreterService.getAllVariableIds(sessionId);

            String lastVarStr = "";
            if (lastVar != null) {
                String value = lastVar.getValue();
                // Configs formatting workaround:
                // FAMILIAR's getValue() uses semicolons as separators in sets: {{A;B};{C;D}}
                // For better readability, we replace semicolons with commas: {{A,B},{C,D}}
                // We detect configs by checking if the variable is a SetVariable containing SetVariables.
                // This is a heuristic - ideally the FAMILIAR API would provide a configurable formatter.
                // Edge case: a SetVariable<SetVariable> with feature names containing semicolons
                // would be incorrectly formatted, but this is unlikely in practice.
                if (lastVar instanceof SetVariable) {
                    SetVariable setVar = (SetVariable) lastVar;
                    if (!setVar.getVars().isEmpty() && setVar.getVars().iterator().next() instanceof SetVariable) {
                        value = value.replace(";", ",");
                    }
                }
                lastVarStr = lastVar.getIdentifier() + " = " + value;
            }

            InterpretResponse response = InterpretResponse.builder()
                    .varIds(allVarIds)
                    .lastVar(lastVarStr)
                    .build();

            return ResponseEntity.ok(response);
        } catch (FMLAssertionError | FMLFatalError e) {
            return ResponseEntity.badRequest().body(Map.of("msgError", e.getMessage()));
        }
    }

    /**
     * Get a variable value
     */
    @GetMapping("/variable/{id}")
    public ResponseEntity<String> getVariable(
            @PathVariable String id,
            HttpSession session) {
        try {
            Variable var = interpreterService.getVariable(session.getId(), id);
            return ResponseEntity.ok(var.getValue());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get structured feature model for visualization
     */
    @GetMapping("/fm/{id}/structure")
    public ResponseEntity<?> getFeatureModelStructure(
            @PathVariable String id,
            HttpSession session) {
        try {
            Map<String, Object> structure = interpreterService.getFeatureModelStructure(session.getId(), id);
            return ResponseEntity.ok(structure);
        } catch (Exception e) {
            log.error("Error getting FM structure: ", e);
            return ResponseEntity.badRequest().body(Map.of("msgError", e.getMessage()));
        }
    }

    /**
     * Analyze a feature model (isValid, deadFeatures, falseOptionals)
     */
    @GetMapping("/fm/{id}/analyze")
    public ResponseEntity<?> analyzeFeatureModel(
            @PathVariable String id,
            HttpSession session) {
        try {
            Map<String, Object> analysis = interpreterService.analyzeFeatureModel(session.getId(), id);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            log.error("Error analyzing FM: ", e);
            return ResponseEntity.badRequest().body(Map.of("msgError", e.getMessage()));
        }
    }

    /**
     * Get all valid configurations of a feature model
     */
    @GetMapping("/fm/{id}/configs")
    public ResponseEntity<?> getConfigurations(
            @PathVariable String id,
            @RequestParam(defaultValue = "100") int limit,
            HttpSession session) {
        try {
            Map<String, Object> configs = interpreterService.getConfigurations(session.getId(), id, limit);
            return ResponseEntity.ok(configs);
        } catch (Exception e) {
            log.error("Error getting configurations: ", e);
            return ResponseEntity.badRequest().body(Map.of("msgError", e.getMessage()));
        }
    }

    /**
     * Get all variable IDs
     */
    @GetMapping("/variables")
    public ResponseEntity<List<String>> getAllVariables(HttpSession session) {
        List<String> varIds = interpreterService.getAllVariableIds(session.getId());
        return ResponseEntity.ok(varIds);
    }

    /**
     * Get variable info with type
     */
    @GetMapping("/variable/{id}/info")
    public ResponseEntity<?> getVariableInfo(
            @PathVariable String id,
            HttpSession session) {
        try {
            Variable var = interpreterService.getVariable(session.getId(), id);
            String type = "unknown";
            String value = var.getValue();

            if (var instanceof FeatureModelVariable) {
                type = "FeatureModel";
            } else if (var instanceof ConfigurationVariable) {
                type = "Configuration";
            } else if (var instanceof SetVariable) {
                type = "Set";
                SetVariable setVar = (SetVariable) var;
                // Apply configs formatting workaround (see evalPrompt for detailed explanation)
                if (!setVar.getVars().isEmpty() && setVar.getVars().iterator().next() instanceof SetVariable) {
                    value = value.replace(";", ",");
                }
            }
            return ResponseEntity.ok(Map.of(
                "id", id,
                "value", value,
                "type", type
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Reset the interpreter
     */
    @PostMapping("/reset")
    public ResponseEntity<Void> reset(HttpSession session) {
        interpreterService.reset(session.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Get all FAMILIAR keywords for syntax highlighting
     */
    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getKeywords() {
        List<String> keywords = List.of(
                "setDiff", "setUnion", "aggregateMerge", "ksynthesis", "slice", "over", "including",
                "cores", "serialize", "constraint", "constraints", "removeConstraint", "addConstraint",
                "deads", "setIntersection", "to", "falseOptionals", "excluding",
                "withMapping", "map", "cleanup", "Set", "asFM", "aggregate",
                "isConflicting", "autoSelect", "setIsEmpty", "isComplete", "isNull",
                "isExisting", "diff", "counting", "removeFeature", "renameFeature", "isValid", "requires",
                "implies", "excludes", "FeatureModel", "Feature", "String",
                "operator", "configuration", "select", "deselect", "size", "unselect",
                "min", "max", "random", "not", "==", "else", "configs", "set", "setAdd",
                "exit", "extract", "setEmpty", "export", "hide", "strConcat",
                "and", "or", "mand", "opt", "xor", "str_concat", "eq", "neq", "do",
                "merge", "union", "sunion", "intersection", "whichfm", "name", "run",
                "into", "valid?", "parent", "parameter", "then", "if",
                "end", "is_null", "root", "with", "print_var", "print", "children", "foreach", "in",
                "selectedF", "deselectedF", "unselectedF", "removeVariable",
                "println", "copy", "FM", "as", "assert", "insert", "compare"
        );
        return ResponseEntity.ok(keywords);
    }

    private String getStackTraceAsString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
