package fr.inria.familiar.webfml.service;

import fr.familiar.interpreter.FMLAssertionError;
import fr.familiar.interpreter.FMLBasicInterpreter;
import fr.familiar.interpreter.FMLFatalError;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage FAMILIAR interpreter instances per session
 */
@Slf4j
@Service
public class FamiliarInterpreterService {

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
        interpreter.reset();
        return interpreter.eval(command);
    }

    /**
     * Evaluate a prompt command (without resetting the environment)
     */
    public Variable evalPrompt(String sessionId, String command) throws FMLAssertionError, FMLFatalError {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        return interpreter.eval(command);
    }

    /**
     * Get a specific variable from the interpreter
     */
    public Variable getVariable(String sessionId, String variableId) {
        FMLBasicInterpreter interpreter = getInterpreter(sessionId);
        return interpreter.eval(variableId);
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
}
