package fr.inria.familiar.webfml.controller;

import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;
import fr.inria.familiar.webfml.service.FamiliarInterpreterService;
import fr.inria.familiar.webfml.service.KSynthesisService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for interactive KSynthesis operations
 */
@RestController
@RequestMapping("/ksynthesis")
public class KSynthesisController {

    private static final Logger log = LoggerFactory.getLogger(KSynthesisController.class);

    private final FamiliarInterpreterService interpreterService;
    private final KSynthesisService ksynthesisService;

    public KSynthesisController(FamiliarInterpreterService interpreterService, KSynthesisService ksynthesisService) {
        this.interpreterService = interpreterService;
        this.ksynthesisService = ksynthesisService;
    }

    /**
     * Start interactive ksynthesis on a feature model variable
     */
    @PostMapping("/start")
    public ResponseEntity<?> startSynthesis(
            @RequestParam String variableId,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            Variable v = interpreterService.getVariable(sessionId, variableId);

            if (!(v instanceof FeatureModelVariable)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", variableId + " is not a feature model"));
            }

            FeatureModelVariable fmv = (FeatureModelVariable) v;
            Map<String, Object> result = ksynthesisService.startSynthesis(sessionId, variableId, fmv);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error starting synthesis: ", e);
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Select a parent for a feature or cluster
     */
    @PostMapping("/select-parent")
    public ResponseEntity<?> selectParent(
            @RequestBody Map<String, Object> request,
            HttpSession session) {
        try {
            @SuppressWarnings("unchecked")
            List<String> children = (List<String>) request.get("children");
            String parent = (String) request.get("parent");

            Map<String, Object> result = ksynthesisService.selectParent(session.getId(), children, parent);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error selecting parent: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Ignore a suggested parent for a feature
     */
    @PostMapping("/ignore-parent")
    public ResponseEntity<?> ignoreParent(
            @RequestParam String child,
            @RequestParam String parent,
            HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.ignoreParent(session.getId(), child, parent);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error ignoring parent: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Set the root feature
     */
    @PostMapping("/set-root")
    public ResponseEntity<?> setRoot(
            @RequestParam String root,
            HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.setRoot(session.getId(), root);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error setting root: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Complete the feature model automatically
     */
    @PostMapping("/complete")
    public ResponseEntity<?> completeFM(HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.completeFM(session.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error completing FM: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Undo last action
     */
    @PostMapping("/undo")
    public ResponseEntity<?> undo(HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.undo(session.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error undoing: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Redo last undone action
     */
    @PostMapping("/redo")
    public ResponseEntity<?> redo(HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.redo(session.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error redoing: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Save synthesized FM to a variable
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveToVariable(
            @RequestParam(required = false) String newVariableId,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            FeatureModelVariable fm = ksynthesisService.getSynthesizedFM(sessionId);

            if (fm == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "No synthesized FM available"));
            }

            String varId = newVariableId != null ? newVariableId : fm.getIdentifier();
            interpreterService.addOrReplaceVariable(sessionId, varId, fm);

            return ResponseEntity.ok(Map.of(
                "variableId", varId,
                "value", fm.getValue()
            ));
        } catch (Exception e) {
            log.error("Error saving FM: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get available heuristics
     */
    @GetMapping("/heuristics")
    public ResponseEntity<?> getHeuristics() {
        return ResponseEntity.ok(ksynthesisService.getAvailableHeuristics());
    }

    /**
     * Set ranking list heuristic
     */
    @PostMapping("/heuristic/ranking")
    public ResponseEntity<?> setRankingHeuristic(
            @RequestParam String heuristic,
            HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.setRankingHeuristic(session.getId(), heuristic);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error setting heuristic: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Set clustering parameters
     */
    @PostMapping("/heuristic/clustering")
    public ResponseEntity<?> setClusteringParameters(
            @RequestParam String heuristic,
            @RequestParam double threshold,
            HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.setClusteringParameters(
                session.getId(), heuristic, threshold);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error setting clustering: ", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get current synthesis state
     */
    @GetMapping("/state")
    public ResponseEntity<?> getState(HttpSession session) {
        try {
            Map<String, Object> result = ksynthesisService.getState(session.getId());
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
}
