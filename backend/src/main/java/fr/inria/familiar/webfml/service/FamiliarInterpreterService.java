package fr.inria.familiar.webfml.service;

import fr.familiar.interpreter.FMLAssertionError;
import fr.familiar.interpreter.FMLBasicInterpreter;
import fr.familiar.interpreter.FMLFatalError;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;
import gsd.synthesis.Expression;
import gsd.synthesis.FeatureEdge;
import gsd.synthesis.FeatureGraph;
import gsd.synthesis.FeatureNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service to manage FAMILIAR interpreter instances per session
 */
@Service
public class FamiliarInterpreterService {

    private static final Logger log = LoggerFactory.getLogger(FamiliarInterpreterService.class);

    private final Map<String, FMLBasicInterpreter> interpretersBySession = new ConcurrentHashMap<>();

    /**
     * Get or create an interpreter for the given session ID
     */
    public FMLBasicInterpreter getInterpreter(String sessionId) {
        return interpretersBySession.computeIfAbsent(sessionId, id -> {
            log.info("Creating new FAMILIAR interpreter for session: {}", id);
            return new FMLBasicInterpreter();
        });
    }

    /**
     * Execute a FAMILIAR command and return the result
     */
    public Variable interpret(String sessionId, String command) throws FMLAssertionError, FMLFatalError {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        synchronized (interpreter) {
            interpreter.reset();
            return interpreter.eval(command);
        }
    }

    /**
     * Evaluate a prompt command (without resetting the environment)
     */
    public Variable evalPrompt(String sessionId, String command) throws FMLAssertionError, FMLFatalError {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        synchronized (interpreter) {
            return interpreter.eval(command);
        }
    }

    /**
     * Get a specific variable from the interpreter
     */
    public Variable getVariable(String sessionId, String variableId) {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);

        // Synchronize on the interpreter to prevent race conditions
        synchronized (interpreter) {
            // First check if the variable exists in the interpreter
            List<String> allIds = interpreter.getAllIdentifiers();
            if (!allIds.contains(variableId)) {
                throw new RuntimeException("Variable not found: " + variableId + ". Available variables: " + allIds);
            }

            try {
                return interpreter.eval(variableId);
            } catch (FMLFatalError | FMLAssertionError e) {
                throw new RuntimeException("Error evaluating variable: " + variableId, e);
            }
        }
    }

    /**
     * Get all variable identifiers for the session
     */
    public List<String> getAllVariableIds(String sessionId) {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        return interpreter.getAllIdentifiers();
    }

    /**
     * Reset the interpreter for a session
     */
    public void reset(String sessionId) {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        interpreter.reset();
        log.info("Reset interpreter for session: {}", sessionId);
    }

    /**
     * Add or replace a variable in the interpreter
     */
    public void addOrReplaceVariable(String sessionId, String identifier, Variable variable) {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        interpreter.addOrReplaceVariable(identifier, variable);
    }

    /**
     * Remove an interpreter session
     */
    public void removeSession(String sessionId) {
        FMLBasicInterpreter removed = interpretersBySession.remove(sessionId);
        if (removed != null) {
            log.info("Removed interpreter for session: {}", sessionId);
        }
    }

    /**
     * Check if a variable is a feature model
     */
    public boolean isFeatureModel(String sessionId, String variableId) {
        Variable var = getVariable(sessionId, variableId);
        return var instanceof FeatureModelVariable;
    }

    /**
     * Get a feature model variable
     */
    public Optional<FeatureModelVariable> getFeatureModel(String sessionId, String variableId) {
        Variable var = getVariable(sessionId, variableId);
        if (var instanceof FeatureModelVariable) {
            return Optional.of((FeatureModelVariable) var);
        }
        return Optional.empty();
    }

    /**
     * Get a configuration variable
     */
    public Optional<ConfigurationVariable> getConfiguration(String sessionId, String variableId) {
        Variable var = getVariable(sessionId, variableId);
        if (var instanceof ConfigurationVariable) {
            return Optional.of((ConfigurationVariable) var);
        }
        return Optional.empty();
    }

    /**
     * Get structured feature model representation for visualization
     */
    public Map<String, Object> getFeatureModelStructure(String sessionId, String variableId) {
        Optional<FeatureModelVariable> fmOpt = getFeatureModel(sessionId, variableId);
        if (fmOpt.isEmpty()) {
            throw new RuntimeException("Variable is not a feature model: " + variableId);
        }

        FeatureModelVariable fmv = fmOpt.get();
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
            // If no root found, try to get one from the FM
            try {
                rootFeature = fmv.root().name();
            } catch (Exception e) {
                log.warn("Could not determine root feature: {}", e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("variableId", variableId);
        result.put("root", rootFeature);

        // Build tree structure
        if (rootFeature != null) {
            result.put("tree", buildFeatureTree(diagram, rootFeature));
        }

        // Extract constraints
        List<String> constraints = new ArrayList<>();
        try {
            Set<Expression<String>> allConstraints = fmv.getAllConstraints();
            if (allConstraints != null) {
                constraints = allConstraints.stream()
                        .map(Expression::toString)
                        .sorted()
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("Could not extract constraints: {}", e.getMessage());
        }
        result.put("constraints", constraints);

        return result;
    }

    /**
     * Recursively build the feature tree structure
     */
    private Map<String, Object> buildFeatureTree(FeatureGraph<String> diagram, String featureName) {
        Map<String, Object> node = new HashMap<>();
        node.put("name", featureName);

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

        // Track group edges and their members (to keep separate groups distinct)
        Map<FeatureEdge, Set<String>> orGroupEdges = new HashMap<>();      // OR groups (1..n)
        Map<FeatureEdge, Set<String>> xorGroupEdges = new HashMap<>();     // XOR groups (exactly 1)
        Map<FeatureEdge, Set<String>> mutexGroupEdges = new HashMap<>();   // MUTEX groups (0..1)
        Set<String> groupedChildren = new HashSet<>();

        // First: find all children by checking parent relationships
        List<String> allChildren = new ArrayList<>();
        for (FeatureNode<String> vertex : diagram.vertices()) {
            if (vertex.isTop() || vertex.isBottom()) continue;
            String childName = vertex.getFeature();
            if (childName.equals(featureName)) continue;

            // Check if this vertex's parent is our feature
            Set<FeatureNode<String>> parents = diagram.parents(vertex);
            for (FeatureNode<String> parent : parents) {
                if (parent.getFeature().equals(featureName)) {
                    allChildren.add(childName);
                    break;
                }
            }
        }

        // Second: categorize each child by checking edge types
        // Get incident edges for the parent feature
        Collection<FeatureEdge> parentIncidentEdges = diagram.incidentEdges(featureNode);

        for (String childName : allChildren) {
            boolean isMandatory = false;
            FeatureEdge orEdge = null;
            FeatureEdge xorEdge = null;
            FeatureEdge mutexEdge = null;

            FeatureNode<String> childNode = diagram.findVertex(childName);
            if (childNode == null) continue;

            // Get incident edges for the child
            Collection<FeatureEdge> childIncidentEdges = diagram.incidentEdges(childNode);

            // Find edges that are incident to BOTH parent and child
            for (FeatureEdge edge : childIncidentEdges) {
                if (!parentIncidentEdges.contains(edge)) continue;

                int edgeType = edge.getType();

                if (edgeType == FeatureEdge.MANDATORY) {
                    isMandatory = true;
                } else if (edgeType == FeatureEdge.OR) {
                    orEdge = edge;
                } else if (edgeType == 16) {
                    // Type 16 = XOR/alternative groups (exactly 1)
                    xorEdge = edge;
                } else if (edgeType == FeatureEdge.MUTEX) {
                    // Type 4 = MUTEX groups (0..1)
                    mutexEdge = edge;
                }
                // HIERARCHY edges are optional by default
            }

            // Categorize the child
            if (orEdge != null) {
                orGroupEdges.computeIfAbsent(orEdge, k -> new HashSet<>()).add(childName);
                groupedChildren.add(childName);
            } else if (xorEdge != null) {
                xorGroupEdges.computeIfAbsent(xorEdge, k -> new HashSet<>()).add(childName);
                groupedChildren.add(childName);
            } else if (mutexEdge != null) {
                mutexGroupEdges.computeIfAbsent(mutexEdge, k -> new HashSet<>()).add(childName);
                groupedChildren.add(childName);
            } else if (isMandatory) {
                mandatoryChildren.add(buildFeatureTree(diagram, childName));
            } else {
                optionalChildren.add(buildFeatureTree(diagram, childName));
            }
        }

        // Build OR groups (each edge = separate group)
        List<Map<String, Object>> orGroups = new ArrayList<>();
        for (Set<String> members : orGroupEdges.values()) {
            List<Map<String, Object>> memberTrees = new ArrayList<>();
            for (String member : members) {
                memberTrees.add(buildFeatureTree(diagram, member));
            }
            Map<String, Object> group = new HashMap<>();
            group.put("type", "or");
            group.put("members", memberTrees);
            orGroups.add(group);
        }

        // Build XOR groups (each edge = separate group)
        List<Map<String, Object>> xorGroups = new ArrayList<>();
        for (Set<String> members : xorGroupEdges.values()) {
            List<Map<String, Object>> memberTrees = new ArrayList<>();
            for (String member : members) {
                memberTrees.add(buildFeatureTree(diagram, member));
            }
            Map<String, Object> group = new HashMap<>();
            group.put("type", "xor");
            group.put("members", memberTrees);
            xorGroups.add(group);
        }

        // Build MUTEX groups (each edge = separate group)
        List<Map<String, Object>> mutexGroups = new ArrayList<>();
        for (Set<String> members : mutexGroupEdges.values()) {
            List<Map<String, Object>> memberTrees = new ArrayList<>();
            for (String member : members) {
                memberTrees.add(buildFeatureTree(diagram, member));
            }
            Map<String, Object> group = new HashMap<>();
            group.put("type", "mutex");
            group.put("members", memberTrees);
            mutexGroups.add(group);
        }

        node.put("mandatory", mandatoryChildren);
        node.put("optional", optionalChildren);
        node.put("orGroups", orGroups);
        node.put("xorGroups", xorGroups);
        node.put("mutexGroups", mutexGroups);

        return node;
    }

}
