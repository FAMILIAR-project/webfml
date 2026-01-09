package fr.inria.familiar.webfml.service;

import fr.familiar.parser.ConfigurationVariableFactory;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import gsd.synthesis.Expression;
import gsd.synthesis.FeatureEdge;
import gsd.synthesis.FeatureGraph;
import gsd.synthesis.FeatureNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xtext.example.mydsl.fml.AutoConfMode;
import org.xtext.example.mydsl.fml.OpSelection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for interactive feature model configuration with SAT-based propagation
 */
@Service
public class ConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    private final FamiliarInterpreterService interpreterService;
    private final Map<String, ConfigurationVariable> configurationsBySession = new ConcurrentHashMap<>();
    private final Map<String, String> fmVariableIdsBySession = new ConcurrentHashMap<>();
    private final Map<String, FeatureModelVariable> fmsBySession = new ConcurrentHashMap<>();

    public ConfigurationService(FamiliarInterpreterService interpreterService) {
        this.interpreterService = interpreterService;
    }

    /**
     * Start a new configuration session for a feature model
     */
    public Map<String, Object> startConfiguration(String sessionId, String variableId, FeatureModelVariable fmv) {
        log.info("Starting configuration for session {} on variable {}", sessionId, variableId);

        // Retry logic for flaky BDD library (similar to KSynthesis)
        ConfigurationVariable config = null;
        Exception lastError = null;

        for (int attempt = 1; attempt <= 5; attempt++) {
            try {
                // Try FeatureIDE implementation first (more stable), fall back to SPLOT
                try {
                    config = ConfigurationVariableFactory.INSTANCE.mkFeatureIDE(fmv, variableId + "_config");
                } catch (Exception e) {
                    log.warn("FeatureIDE config failed, trying SPLOT: {}", e.getMessage());
                    config = ConfigurationVariableFactory.INSTANCE.mkSPLOT(fmv, variableId + "_config");
                }
                break; // Success
            } catch (ArrayIndexOutOfBoundsException e) {
                lastError = e;
                log.warn("BDD library error on attempt {}/5: {}", attempt, e.getMessage());
                if (attempt < 5) {
                    try {
                        Thread.sleep(150 * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                log.error("Error starting configuration", e);
                throw new RuntimeException("Failed to start configuration: " + e.getMessage(), e);
            }
        }

        if (config == null) {
            throw new RuntimeException("Failed to start configuration after 5 attempts: " +
                (lastError != null ? lastError.getMessage() : "unknown error"), lastError);
        }

        configurationsBySession.put(sessionId, config);
        fmVariableIdsBySession.put(sessionId, variableId);
        fmsBySession.put(sessionId, fmv);

        return buildStateResponse(sessionId);
    }

    /**
     * Select a feature in the configuration
     */
    public Map<String, Object> selectFeature(String sessionId, String featureName) {
        ConfigurationVariable config = getConfiguration(sessionId);
        if (config == null) {
            throw new IllegalStateException("No active configuration session");
        }

        try {
            log.info("Selecting feature: {}", featureName);
            config.applySelection(featureName, OpSelection.SELECT);
            return buildStateResponse(sessionId);
        } catch (Exception e) {
            log.error("Error selecting feature {}", featureName, e);
            throw new RuntimeException("Failed to select feature: " + e.getMessage(), e);
        }
    }

    /**
     * Deselect a feature in the configuration
     */
    public Map<String, Object> deselectFeature(String sessionId, String featureName) {
        ConfigurationVariable config = getConfiguration(sessionId);
        if (config == null) {
            throw new IllegalStateException("No active configuration session");
        }

        try {
            log.info("Deselecting feature: {}", featureName);
            config.applySelection(featureName, OpSelection.DESELECT);
            return buildStateResponse(sessionId);
        } catch (Exception e) {
            log.error("Error deselecting feature {}", featureName, e);
            throw new RuntimeException("Failed to deselect feature: " + e.getMessage(), e);
        }
    }

    /**
     * Unselect (reset) a feature in the configuration
     */
    public Map<String, Object> unselectFeature(String sessionId, String featureName) {
        ConfigurationVariable config = getConfiguration(sessionId);
        if (config == null) {
            throw new IllegalStateException("No active configuration session");
        }

        try {
            log.info("Unselecting feature: {}", featureName);
            config.applySelection(featureName, OpSelection.UNSELECT);
            return buildStateResponse(sessionId);
        } catch (Exception e) {
            log.error("Error unselecting feature {}", featureName, e);
            throw new RuntimeException("Failed to unselect feature: " + e.getMessage(), e);
        }
    }

    /**
     * Auto-complete the configuration using SAT solver
     */
    public Map<String, Object> autoComplete(String sessionId, String mode) {
        ConfigurationVariable config = getConfiguration(sessionId);
        if (config == null) {
            throw new IllegalStateException("No active configuration session");
        }

        try {
            log.info("Auto-completing configuration with mode: {}", mode);
            AutoConfMode autoMode;
            switch (mode.toUpperCase()) {
                case "MAX":
                    autoMode = AutoConfMode.MAX;
                    break;
                case "MIN":
                    autoMode = AutoConfMode.MIN;
                    break;
                case "RANDOM":
                    autoMode = AutoConfMode.RANDOM;
                    break;
                default:
                    autoMode = AutoConfMode.MAX;
            }
            config.autoselect(autoMode);
            return buildStateResponse(sessionId);
        } catch (Exception e) {
            log.error("Error auto-completing configuration", e);
            throw new RuntimeException("Failed to auto-complete: " + e.getMessage(), e);
        }
    }

    /**
     * Get current configuration state
     */
    public Map<String, Object> getState(String sessionId) {
        ConfigurationVariable config = getConfiguration(sessionId);
        if (config == null) {
            return null;
        }
        return buildStateResponse(sessionId);
    }

    /**
     * Load an existing configuration variable for viewing/editing
     */
    public Map<String, Object> loadConfiguration(String sessionId, String variableId, ConfigurationVariable config) {
        log.info("Loading existing configuration for session {} from variable {}", sessionId, variableId);

        // Get the associated feature model
        FeatureModelVariable fmv = config.getFmv();

        configurationsBySession.put(sessionId, config);
        fmVariableIdsBySession.put(sessionId, variableId);
        if (fmv != null) {
            fmsBySession.put(sessionId, fmv);
        }

        return buildStateResponse(sessionId);
    }

    /**
     * Get the configuration variable
     */
    public ConfigurationVariable getConfiguration(String sessionId) {
        return configurationsBySession.get(sessionId);
    }

    /**
     * Get a configuration variable by its variable ID from the interpreter
     */
    public ConfigurationVariable getConfigurationByVariableId(String sessionId, String variableId) {
        try {
            Object variable = interpreterService.getVariable(sessionId, variableId);
            if (variable instanceof ConfigurationVariable) {
                return (ConfigurationVariable) variable;
            }
            log.warn("Variable {} is not a ConfigurationVariable, it is {}", variableId,
                variable != null ? variable.getClass().getSimpleName() : "null");
            return null;
        } catch (Exception e) {
            log.error("Error getting configuration variable {}: {}", variableId, e.getMessage());
            return null;
        }
    }

    /**
     * Save configuration to a variable
     */
    public ConfigurationVariable saveConfiguration(String sessionId, String newVariableId) {
        ConfigurationVariable config = getConfiguration(sessionId);
        if (config == null) {
            throw new IllegalStateException("No active configuration session");
        }

        String varId = newVariableId != null ? newVariableId : config.getIdentifier();
        interpreterService.addOrReplaceVariable(sessionId, varId, config);
        return config;
    }

    /**
     * Build JSON response with configuration state
     */
    private Map<String, Object> buildStateResponse(String sessionId) {
        ConfigurationVariable config = configurationsBySession.get(sessionId);
        String fmVariableId = fmVariableIdsBySession.get(sessionId);
        FeatureModelVariable fmv = fmsBySession.get(sessionId);

        Map<String, Object> result = new HashMap<>();
        result.put("variableId", config.getIdentifier());
        result.put("fmVariableId", fmVariableId);

        // Get selection states - these methods return Set<String>
        Set<String> selected = getSelectedFeatures(config);
        Set<String> deselected = getDeselectedFeatures(config);
        Set<String> unselected = getUnselectedFeatures(config);

        result.put("selected", new ArrayList<>(selected));
        result.put("deselected", new ArrayList<>(deselected));
        result.put("unselected", new ArrayList<>(unselected));

        // Check validity and completeness
        result.put("valid", isValid(config));
        result.put("complete", isComplete(config));

        // Build tree structure with selection states
        if (fmv != null) {
            result.put("tree", buildConfigTree(fmv, selected, deselected, unselected));
            result.put("constraints", getConstraints(fmv));
        }

        return result;
    }

    private Set<String> getSelectedFeatures(ConfigurationVariable config) {
        try {
            // getSelected returns Set<String> directly
            return config.getSelected();
        } catch (Exception e) {
            log.warn("Error getting selected features: {}", e.getMessage());
            return Collections.emptySet();
        }
    }

    private Set<String> getDeselectedFeatures(ConfigurationVariable config) {
        try {
            // getDeselected returns Set<String> directly
            return config.getDeselected();
        } catch (Exception e) {
            log.warn("Error getting deselected features: {}", e.getMessage());
            return Collections.emptySet();
        }
    }

    private Set<String> getUnselectedFeatures(ConfigurationVariable config) {
        try {
            // getUnselected returns Set<String> directly
            return config.getUnselected();
        } catch (Exception e) {
            log.warn("Error getting unselected features: {}", e.getMessage());
            return Collections.emptySet();
        }
    }

    private boolean isValid(ConfigurationVariable config) {
        try {
            return config.isValid();
        } catch (Exception e) {
            log.warn("Error checking validity: {}", e.getMessage());
            return true;
        }
    }

    private boolean isComplete(ConfigurationVariable config) {
        try {
            return config.isComplete();
        } catch (Exception e) {
            log.warn("Error checking completeness: {}", e.getMessage());
            return false;
        }
    }

    private List<String> getConstraints(FeatureModelVariable fmv) {
        try {
            Set<Expression<String>> allConstraints = fmv.getAllConstraints();
            if (allConstraints != null) {
                return allConstraints.stream()
                        .map(Expression::toString)
                        .sorted()
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("Could not extract constraints: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Build tree structure with selection states for visualization
     */
    private Map<String, Object> buildConfigTree(FeatureModelVariable fmv,
                                                  Set<String> selected,
                                                  Set<String> deselected,
                                                  Set<String> unselected) {
        FeatureGraph<String> diagram = fmv.getFm().getDiagram();

        // Find the root feature
        String rootFeature = null;
        for (FeatureNode<String> node : diagram.vertices()) {
            if (!node.isTop() && !node.isBottom()) {
                Set<FeatureNode<String>> parents = diagram.parents(node);
                if (parents.isEmpty() || (parents.size() == 1 && parents.iterator().next().isTop())) {
                    rootFeature = node.getFeature();
                    break;
                }
            }
        }

        if (rootFeature == null) {
            try {
                rootFeature = fmv.root().name();
            } catch (Exception e) {
                log.warn("Could not determine root feature: {}", e.getMessage());
                return new HashMap<>();
            }
        }

        return buildFeatureNode(diagram, rootFeature, selected, deselected, unselected);
    }

    private Map<String, Object> buildFeatureNode(FeatureGraph<String> diagram,
                                                   String featureName,
                                                   Set<String> selected,
                                                   Set<String> deselected,
                                                   Set<String> unselected) {
        Map<String, Object> node = new HashMap<>();
        node.put("name", featureName);

        // Determine state
        String state = "unselected";
        if (selected.contains(featureName)) {
            state = "selected";
        } else if (deselected.contains(featureName)) {
            state = "deselected";
        }
        node.put("state", state);

        FeatureNode<String> featureNode = diagram.findVertex(featureName);
        if (featureNode == null) {
            node.put("mandatory", new ArrayList<>());
            node.put("optional", new ArrayList<>());
            node.put("orGroups", new ArrayList<>());
            node.put("xorGroups", new ArrayList<>());
            node.put("mutexGroups", new ArrayList<>());
            return node;
        }

        List<Map<String, Object>> mandatoryChildren = new ArrayList<>();
        List<Map<String, Object>> optionalChildren = new ArrayList<>();
        Map<FeatureEdge, Set<String>> orGroupEdges = new HashMap<>();
        Map<FeatureEdge, Set<String>> xorGroupEdges = new HashMap<>();
        Map<FeatureEdge, Set<String>> mutexGroupEdges = new HashMap<>();

        // Find all children
        List<String> allChildren = new ArrayList<>();
        for (FeatureNode<String> vertex : diagram.vertices()) {
            if (vertex.isTop() || vertex.isBottom()) continue;
            String childName = vertex.getFeature();
            if (childName.equals(featureName)) continue;

            Set<FeatureNode<String>> parents = diagram.parents(vertex);
            for (FeatureNode<String> parent : parents) {
                if (parent.getFeature().equals(featureName)) {
                    allChildren.add(childName);
                    break;
                }
            }
        }

        // Get incident edges for the parent feature
        Collection<FeatureEdge> parentIncidentEdges = diagram.incidentEdges(featureNode);

        for (String childName : allChildren) {
            boolean isMandatory = false;
            FeatureEdge orEdge = null;
            FeatureEdge xorEdge = null;
            FeatureEdge mutexEdge = null;

            FeatureNode<String> childNode = diagram.findVertex(childName);
            if (childNode == null) continue;

            Collection<FeatureEdge> childIncidentEdges = diagram.incidentEdges(childNode);

            for (FeatureEdge edge : childIncidentEdges) {
                if (!parentIncidentEdges.contains(edge)) continue;

                int edgeType = edge.getType();
                if (edgeType == FeatureEdge.MANDATORY) {
                    isMandatory = true;
                } else if (edgeType == FeatureEdge.OR) {
                    orEdge = edge;
                } else if (edgeType == 16) { // XOR
                    xorEdge = edge;
                } else if (edgeType == FeatureEdge.MUTEX) {
                    mutexEdge = edge;
                }
            }

            if (orEdge != null) {
                orGroupEdges.computeIfAbsent(orEdge, k -> new HashSet<>()).add(childName);
            } else if (xorEdge != null) {
                xorGroupEdges.computeIfAbsent(xorEdge, k -> new HashSet<>()).add(childName);
            } else if (mutexEdge != null) {
                mutexGroupEdges.computeIfAbsent(mutexEdge, k -> new HashSet<>()).add(childName);
            } else if (isMandatory) {
                mandatoryChildren.add(buildFeatureNode(diagram, childName, selected, deselected, unselected));
            } else {
                optionalChildren.add(buildFeatureNode(diagram, childName, selected, deselected, unselected));
            }
        }

        // Build groups
        node.put("mandatory", mandatoryChildren);
        node.put("optional", optionalChildren);
        node.put("orGroups", buildGroups(diagram, orGroupEdges, "or", selected, deselected, unselected));
        node.put("xorGroups", buildGroups(diagram, xorGroupEdges, "xor", selected, deselected, unselected));
        node.put("mutexGroups", buildGroups(diagram, mutexGroupEdges, "mutex", selected, deselected, unselected));

        return node;
    }

    private List<Map<String, Object>> buildGroups(FeatureGraph<String> diagram,
                                                   Map<FeatureEdge, Set<String>> groupEdges,
                                                   String type,
                                                   Set<String> selected,
                                                   Set<String> deselected,
                                                   Set<String> unselected) {
        List<Map<String, Object>> groups = new ArrayList<>();
        for (Set<String> members : groupEdges.values()) {
            List<Map<String, Object>> memberNodes = new ArrayList<>();
            for (String member : members) {
                memberNodes.add(buildFeatureNode(diagram, member, selected, deselected, unselected));
            }
            Map<String, Object> group = new HashMap<>();
            group.put("type", type);
            group.put("members", memberNodes);
            groups.add(group);
        }
        return groups;
    }
}
