package fr.inria.familiar.webfml.service;

import fr.inria.familiar.webfml.dto.TemplateError;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Template engine for processing conditional blocks in project files.
 *
 * Supported syntax:
 * - {{#if FEATURE}} ... {{/if}}
 * - {{#if FEATURE}} ... {{#else}} ... {{/if}}
 * - {{#unless FEATURE}} ... {{/unless}}
 * - Nested conditionals are fully supported
 */
@Service
public class TemplateEngine {

    // Token patterns
    private static final Pattern IF_START = Pattern.compile("\\{\\{#if\\s+(\\w+)\\s*\\}\\}");
    private static final Pattern UNLESS_START = Pattern.compile("\\{\\{#unless\\s+(\\w+)\\s*\\}\\}");
    private static final Pattern ELSE_TOKEN = Pattern.compile("\\{\\{#else\\s*\\}\\}");
    private static final Pattern IF_END = Pattern.compile("\\{\\{/if\\s*\\}\\}");
    private static final Pattern UNLESS_END = Pattern.compile("\\{\\{/unless\\s*\\}\\}");

    // Combined pattern for any token
    private static final Pattern ANY_TOKEN = Pattern.compile(
        "\\{\\{#if\\s+(\\w+)\\s*\\}\\}|" +
        "\\{\\{#unless\\s+(\\w+)\\s*\\}\\}|" +
        "\\{\\{#else\\s*\\}\\}|" +
        "\\{\\{/if\\s*\\}\\}|" +
        "\\{\\{/unless\\s*\\}\\}"
    );

    /**
     * Process a template with given feature configuration.
     *
     * @param content The template content
     * @param selectedFeatures Features that are selected (included)
     * @param deselectedFeatures Features that are deselected (excluded)
     * @return Processed content with conditionals resolved
     */
    public String process(String content, Set<String> selectedFeatures, Set<String> deselectedFeatures) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        List<Token> tokens = tokenize(content);
        StringBuilder result = new StringBuilder();
        int[] position = {0}; // Using array to allow modification in recursive calls

        processTokens(tokens, position, result, selectedFeatures, deselectedFeatures, null);

        return result.toString();
    }

    /**
     * Extract all feature names used in template conditionals.
     */
    public Set<String> extractFeatureNames(String content) {
        Set<String> features = new HashSet<>();

        Matcher ifMatcher = IF_START.matcher(content);
        while (ifMatcher.find()) {
            features.add(ifMatcher.group(1));
        }

        Matcher unlessMatcher = UNLESS_START.matcher(content);
        while (unlessMatcher.find()) {
            features.add(unlessMatcher.group(1));
        }

        return features;
    }

    /**
     * Check if content contains any template conditionals.
     */
    public boolean hasConditionals(String content) {
        return ANY_TOKEN.matcher(content).find();
    }

    /**
     * Validate template syntax and return any errors.
     */
    public List<TemplateError> validate(String content, String filePath) {
        List<TemplateError> errors = new ArrayList<>();

        if (content == null || content.isEmpty()) {
            return errors;
        }

        List<Token> tokens = tokenize(content);
        Stack<Token> blockStack = new Stack<>();

        for (Token token : tokens) {
            switch (token.type) {
                case IF_START:
                case UNLESS_START:
                    blockStack.push(token);
                    break;

                case IF_END:
                    if (blockStack.isEmpty() || blockStack.peek().type != TokenType.IF_START) {
                        errors.add(new TemplateError(filePath, token.lineNumber,
                            "Unexpected {{/if}} - no matching {{#if}}", "SYNTAX"));
                    } else {
                        blockStack.pop();
                    }
                    break;

                case UNLESS_END:
                    if (blockStack.isEmpty() || blockStack.peek().type != TokenType.UNLESS_START) {
                        errors.add(new TemplateError(filePath, token.lineNumber,
                            "Unexpected {{/unless}} - no matching {{#unless}}", "SYNTAX"));
                    } else {
                        blockStack.pop();
                    }
                    break;

                case ELSE:
                    if (blockStack.isEmpty() || blockStack.peek().type != TokenType.IF_START) {
                        errors.add(new TemplateError(filePath, token.lineNumber,
                            "{{#else}} must be inside {{#if}} block", "SYNTAX"));
                    }
                    break;

                default:
                    break;
            }
        }

        // Check for unclosed blocks
        while (!blockStack.isEmpty()) {
            Token unclosed = blockStack.pop();
            String blockType = unclosed.type == TokenType.IF_START ? "{{#if}}" : "{{#unless}}";
            errors.add(new TemplateError(filePath, unclosed.lineNumber,
                "Unclosed " + blockType + " block for feature: " + unclosed.featureName, "UNCLOSED_BLOCK"));
        }

        return errors;
    }

    // Token types
    private enum TokenType {
        TEXT,
        IF_START,
        UNLESS_START,
        ELSE,
        IF_END,
        UNLESS_END
    }

    // Token class
    private static class Token {
        TokenType type;
        String content;
        String featureName;
        int lineNumber;

        Token(TokenType type, String content, String featureName, int lineNumber) {
            this.type = type;
            this.content = content;
            this.featureName = featureName;
            this.lineNumber = lineNumber;
        }
    }

    /**
     * Tokenize content into a list of tokens.
     */
    private List<Token> tokenize(String content) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = ANY_TOKEN.matcher(content);

        int lastEnd = 0;
        int lineNumber = 1;

        while (matcher.find()) {
            // Add text before this token
            if (matcher.start() > lastEnd) {
                String text = content.substring(lastEnd, matcher.start());
                lineNumber += countNewlines(content.substring(lastEnd, matcher.start()));
                tokens.add(new Token(TokenType.TEXT, text, null, lineNumber));
            }

            String match = matcher.group();
            int tokenLine = lineNumber + countNewlines(content.substring(lastEnd, matcher.start()));

            // Determine token type
            if (match.startsWith("{{#if")) {
                Matcher m = IF_START.matcher(match);
                m.find();
                tokens.add(new Token(TokenType.IF_START, match, m.group(1), tokenLine));
            } else if (match.startsWith("{{#unless")) {
                Matcher m = UNLESS_START.matcher(match);
                m.find();
                tokens.add(new Token(TokenType.UNLESS_START, match, m.group(1), tokenLine));
            } else if (match.startsWith("{{#else")) {
                tokens.add(new Token(TokenType.ELSE, match, null, tokenLine));
            } else if (match.startsWith("{{/if")) {
                tokens.add(new Token(TokenType.IF_END, match, null, tokenLine));
            } else if (match.startsWith("{{/unless")) {
                tokens.add(new Token(TokenType.UNLESS_END, match, null, tokenLine));
            }

            lastEnd = matcher.end();
        }

        // Add remaining text
        if (lastEnd < content.length()) {
            tokens.add(new Token(TokenType.TEXT, content.substring(lastEnd), null, lineNumber));
        }

        return tokens;
    }

    /**
     * Process tokens recursively.
     */
    private void processTokens(List<Token> tokens, int[] position, StringBuilder result,
                               Set<String> selected, Set<String> deselected,
                               TokenType endToken) {
        while (position[0] < tokens.size()) {
            Token token = tokens.get(position[0]);

            // Check if we've reached the end of current block
            if (endToken != null && token.type == endToken) {
                return;
            }

            switch (token.type) {
                case TEXT:
                    result.append(token.content);
                    position[0]++;
                    break;

                case IF_START:
                    position[0]++;
                    processIfBlock(tokens, position, result, selected, deselected, token.featureName, false);
                    break;

                case UNLESS_START:
                    position[0]++;
                    processIfBlock(tokens, position, result, selected, deselected, token.featureName, true);
                    break;

                case ELSE:
                    // Else is handled within processIfBlock
                    return;

                case IF_END:
                case UNLESS_END:
                    position[0]++;
                    return;

                default:
                    position[0]++;
                    break;
            }
        }
    }

    /**
     * Process an if/unless block.
     */
    private void processIfBlock(List<Token> tokens, int[] position, StringBuilder result,
                                Set<String> selected, Set<String> deselected,
                                String featureName, boolean isUnless) {
        // Determine if condition is true
        boolean featureSelected = selected.contains(featureName);
        boolean conditionTrue = isUnless ? !featureSelected : featureSelected;

        // Collect content for then-branch and else-branch
        StringBuilder thenContent = new StringBuilder();
        StringBuilder elseContent = new StringBuilder();
        boolean inElseBranch = false;
        int depth = 1;

        while (position[0] < tokens.size() && depth > 0) {
            Token token = tokens.get(position[0]);

            if (token.type == TokenType.IF_START || token.type == TokenType.UNLESS_START) {
                depth++;
                if (inElseBranch) {
                    elseContent.append(processNestedBlock(tokens, position, selected, deselected));
                } else {
                    thenContent.append(processNestedBlock(tokens, position, selected, deselected));
                }
            } else if (token.type == TokenType.IF_END || token.type == TokenType.UNLESS_END) {
                depth--;
                if (depth == 0) {
                    position[0]++;
                    break;
                }
            } else if (token.type == TokenType.ELSE && depth == 1) {
                inElseBranch = true;
                position[0]++;
            } else if (token.type == TokenType.TEXT) {
                if (inElseBranch) {
                    elseContent.append(token.content);
                } else {
                    thenContent.append(token.content);
                }
                position[0]++;
            } else {
                position[0]++;
            }
        }

        // Append the appropriate branch
        if (conditionTrue) {
            result.append(thenContent);
        } else {
            result.append(elseContent);
        }
    }

    /**
     * Process a nested block and return its content.
     */
    private String processNestedBlock(List<Token> tokens, int[] position,
                                      Set<String> selected, Set<String> deselected) {
        Token startToken = tokens.get(position[0]);
        String featureName = startToken.featureName;
        boolean isUnless = startToken.type == TokenType.UNLESS_START;

        position[0]++;

        StringBuilder thenContent = new StringBuilder();
        StringBuilder elseContent = new StringBuilder();
        boolean inElseBranch = false;
        int depth = 1;

        while (position[0] < tokens.size() && depth > 0) {
            Token token = tokens.get(position[0]);

            if (token.type == TokenType.IF_START || token.type == TokenType.UNLESS_START) {
                depth++;
                String nested = processNestedBlock(tokens, position, selected, deselected);
                if (inElseBranch) {
                    elseContent.append(nested);
                } else {
                    thenContent.append(nested);
                }
            } else if (token.type == TokenType.IF_END || token.type == TokenType.UNLESS_END) {
                depth--;
                position[0]++;
                if (depth == 0) {
                    break;
                }
            } else if (token.type == TokenType.ELSE && depth == 1) {
                inElseBranch = true;
                position[0]++;
            } else if (token.type == TokenType.TEXT) {
                if (inElseBranch) {
                    elseContent.append(token.content);
                } else {
                    thenContent.append(token.content);
                }
                position[0]++;
            } else {
                position[0]++;
            }
        }

        boolean featureSelected = selected.contains(featureName);
        boolean conditionTrue = isUnless ? !featureSelected : featureSelected;

        return conditionTrue ? thenContent.toString() : elseContent.toString();
    }

    /**
     * Count newlines in a string.
     */
    private int countNewlines(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == '\n') count++;
        }
        return count;
    }
}
