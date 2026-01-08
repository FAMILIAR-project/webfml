package fr.inria.familiar.webfml.service;

import fr.familiar.variable.FeatureModelVariable;
import foreverse.ksynthesis.Heuristic;
import foreverse.ksynthesis.InteractiveFMSynthesizer;
import foreverse.ksynthesis.metrics.*;
import gsd.synthesis.FeatureEdge;
import gsd.synthesis.FeatureNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for interactive feature model synthesis
 */
@Service
public class KSynthesisService {

    private static final Logger log = LoggerFactory.getLogger(KSynthesisService.class);

    private final Map<String, InteractiveFMSynthesizer> synthesizersBySession = new ConcurrentHashMap<>();
    private final Map<String, String> variableIdsBySession = new ConcurrentHashMap<>();
    private final Map<String, Heuristic> heuristics = new LinkedHashMap<>();

    public KSynthesisService() {
        // Initialize available heuristics
        heuristics.put("AlwaysZero", new AlwaysZeroMetric());
        heuristics.put("Random", new RandomMetric());
        heuristics.put("SmithWaterman", new SmithWatermanMetric());
        heuristics.put("Levenshtein", new LevenshteinMetric());
        // Note: WuPalmer and PathLength require WordNet initialization
    }

    /**
     * Start interactive synthesis for a feature model
     */
    public Map<String, Object> startSynthesis(String sessionId, String variableId, FeatureModelVariable fmv) {
        log.info("Starting synthesis for session {} on variable {}", sessionId, variableId);

        Heuristic parentHeuristic = heuristics.get("SmithWaterman");
        Heuristic clusterHeuristic = heuristics.get("SmithWaterman");
        double clusterThreshold = 0.5;

        InteractiveFMSynthesizer synthesizer;
        try {
            synthesizer = new InteractiveFMSynthesizer(
                fmv, parentHeuristic, null, clusterHeuristic, clusterThreshold);
        } catch (ArrayIndexOutOfBoundsException e) {
            // BDD library bug during implication graph computation
            // This is a known issue with certain feature model structures
            log.error("BDD library error during synthesis initialization: {}", e.getMessage());
            throw new RuntimeException(
                "Unable to start synthesis due to BDD computation error. " +
                "This can happen with complex feature models. " +
                "Try with a simpler feature model or remove some constraints.", e);
        }

        synthesizersBySession.put(sessionId, synthesizer);
        variableIdsBySession.put(sessionId, variableId);

        return buildStateResponse(synthesizer, variableId);
    }

    /**
     * Select a parent for a cluster of features
     */
    public Map<String, Object> selectParent(String sessionId, List<String> children, String parent) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        Set<String> childrenSet = new HashSet<>(children);
        synthesizer.selectParentOfCluster(childrenSet, parent);

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Ignore a suggested parent
     */
    public Map<String, Object> ignoreParent(String sessionId, String child, String parent) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        synthesizer.ignoreParent(child, parent);

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Set the root feature
     */
    public Map<String, Object> setRoot(String sessionId, String root) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        synthesizer.setRoot(root);

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Complete the feature model automatically
     */
    public Map<String, Object> completeFM(String sessionId) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        synthesizer.computeCompleteFeatureModel();

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Undo last action
     */
    public Map<String, Object> undo(String sessionId) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        synthesizer.undo();

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Redo last undone action
     */
    public Map<String, Object> redo(String sessionId) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        synthesizer.redo();

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Get the synthesized feature model
     */
    public FeatureModelVariable getSynthesizedFM(String sessionId) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            return null;
        }
        return synthesizer.getFeatureModelVariable();
    }

    /**
     * Get available heuristics
     */
    public Map<String, Object> getAvailableHeuristics() {
        Map<String, Object> result = new HashMap<>();
        result.put("heuristics", new ArrayList<>(heuristics.keySet()));
        result.put("defaultRankingHeuristic", "SmithWaterman");
        result.put("defaultClusteringHeuristic", "SmithWaterman");
        result.put("defaultThreshold", 0.5);
        return result;
    }

    /**
     * Set ranking list heuristic
     */
    public Map<String, Object> setRankingHeuristic(String sessionId, String heuristicName) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        Heuristic heuristic = heuristics.getOrDefault(heuristicName, new AlwaysZeroMetric());
        synthesizer.setParentSimilarityMetric(heuristic);

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Set clustering parameters
     */
    public Map<String, Object> setClusteringParameters(String sessionId, String heuristicName, double threshold) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            throw new IllegalStateException("No active synthesis session");
        }

        Heuristic heuristic = heuristics.getOrDefault(heuristicName, new AlwaysZeroMetric());
        synthesizer.setClusteringParameters(heuristic, threshold);

        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    /**
     * Get current synthesis state
     */
    public Map<String, Object> getState(String sessionId) {
        InteractiveFMSynthesizer synthesizer = getSynthesizer(sessionId);
        if (synthesizer == null) {
            return null;
        }
        return buildStateResponse(synthesizer, variableIdsBySession.get(sessionId));
    }

    private InteractiveFMSynthesizer getSynthesizer(String sessionId) {
        return synthesizersBySession.get(sessionId);
    }

    /**
     * Build a JSON-compatible response with synthesis state
     */
    private Map<String, Object> buildStateResponse(InteractiveFMSynthesizer synthesizer, String variableId) {
        Map<String, Object> result = new HashMap<>();
        result.put("variableId", variableId);

        // Feature model structure
        FeatureModelVariable fm = synthesizer.getFeatureModelVariable();
        result.put("fm", buildFMJson(fm));

        // Ranking lists (parent candidates for each feature)
        List<Map<String, Object>> rankingLists = new ArrayList<>();
        var parentCandidates = synthesizer.getParentCandidates();
        var originalParentCandidates = synthesizer.getOriginalParentCandidates();

        Set<String> possibleRoots = new HashSet<>();
        try {
            var roots = synthesizer.getImplicationGraph().reduceCliques().roots();
            if (roots.iterator().hasNext()) {
                possibleRoots.addAll(roots.iterator().next());
            }
        } catch (Exception e) {
            log.warn("Could not get possible roots: {}", e.getMessage());
        }

        for (var pc : parentCandidates) {
            Map<String, Object> item = new HashMap<>();
            item.put("feature", pc.getKey());
            item.put("parents", new ArrayList<>(pc.getValue()));
            item.put("parentInFM", getParent(pc.getKey(), fm));
            item.put("isPossibleRoot", possibleRoots.contains(pc.getKey()));

            // Original parents
            for (var opc : originalParentCandidates) {
                if (opc.getKey().equals(pc.getKey())) {
                    item.put("originalParents", new ArrayList<>(opc.getValue()));
                    break;
                }
            }

            rankingLists.add(item);
        }
        result.put("rankingLists", rankingLists);

        // Clusters
        List<List<Map<String, Object>>> clusters = new ArrayList<>();
        for (var cluster : synthesizer.getSimilarityClusters()) {
            List<Map<String, Object>> clusterItems = new ArrayList<>();
            for (String feature : cluster) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", feature);
                item.put("parentInFM", getParent(feature, fm));
                clusterItems.add(item);
            }
            clusters.add(clusterItems);
        }
        result.put("clusters", clusters);

        // Cliques
        List<List<Map<String, Object>>> cliques = new ArrayList<>();
        for (var clique : synthesizer.getCliques()) {
            List<Map<String, Object>> cliqueItems = new ArrayList<>();
            for (String feature : clique) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", feature);
                item.put("parentInFM", getParent(feature, fm));
                cliqueItems.add(item);
            }
            cliques.add(cliqueItems);
        }
        result.put("cliques", cliques);

        return result;
    }

    /**
     * Build JSON representation of a feature model
     */
    private Map<String, Object> buildFMJson(FeatureModelVariable fm) {
        Map<String, Object> result = new HashMap<>();
        var diagram = fm.getFm().getDiagram();

        // Nodes (features)
        List<String> nodes = new ArrayList<>();
        for (var v : diagram.vertices()) {
            if (!v.isTop() && !v.isBottom()) {
                nodes.add(v.getFeature());
            }
        }
        result.put("nodes", nodes);

        // Edges (parent-child relationships)
        List<Map<String, String>> edges = new ArrayList<>();
        for (var e : diagram.edges()) {
            if (e.getType() == FeatureEdge.HIERARCHY) {
                var source = diagram.getSource(e);
                var target = diagram.getTarget(e);
                if (!source.isTop() && !source.isBottom() && !target.isTop() && !target.isBottom()) {
                    Map<String, String> edge = new HashMap<>();
                    edge.put("source", source.getFeature());
                    edge.put("target", target.getFeature());
                    edges.add(edge);
                }
            }
        }
        result.put("edges", edges);

        return result;
    }

    /**
     * Get the parent of a feature in the FM
     */
    private String getParent(String feature, FeatureModelVariable fm) {
        try {
            var diagram = fm.getFm().getDiagram();
            var vertex = diagram.findVertex(feature);
            if (vertex == null) return null;

            var parents = diagram.parents(vertex);
            if (parents.size() == 1) {
                FeatureNode<String> parent = parents.iterator().next();
                if (!parent.isTop()) {
                    return parent.getFeature();
                }
            }
        } catch (Exception e) {
            log.warn("Could not get parent for {}: {}", feature, e.getMessage());
        }
        return null;
    }
}
