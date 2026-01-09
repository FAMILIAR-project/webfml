package fr.inria.familiar.webfml.service;

import fr.inria.familiar.webfml.dto.TemplateError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TemplateEngineTest {

    private TemplateEngine engine;

    @BeforeEach
    void setUp() {
        engine = new TemplateEngine();
    }

    @Nested
    class BasicConditionals {
        @Test
        void simpleIfSelected() {
            String content = "{{#if FEATURE}}included{{/if}}";
            Set<String> selected = Set.of("FEATURE");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("included", result);
        }

        @Test
        void simpleIfDeselected() {
            String content = "{{#if FEATURE}}included{{/if}}";
            Set<String> selected = Set.of();
            Set<String> deselected = Set.of("FEATURE");

            String result = engine.process(content, selected, deselected);

            assertEquals("", result);
        }

        @Test
        void simpleIfUnselected() {
            // When feature is not in selected set, condition is false (content removed)
            String content = "{{#if FEATURE}}included{{/if}}";
            Set<String> selected = Set.of();
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("", result);
        }

        @Test
        void simpleUnlessDeselected() {
            String content = "{{#unless FEATURE}}included{{/unless}}";
            Set<String> selected = Set.of();
            Set<String> deselected = Set.of("FEATURE");

            String result = engine.process(content, selected, deselected);

            assertEquals("included", result);
        }

        @Test
        void simpleUnlessSelected() {
            String content = "{{#unless FEATURE}}included{{/unless}}";
            Set<String> selected = Set.of("FEATURE");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("", result);
        }
    }

    @Nested
    class ElseClauses {
        @Test
        void ifElseSelectedBranch() {
            String content = "{{#if FEATURE}}yes{{#else}}no{{/if}}";
            Set<String> selected = Set.of("FEATURE");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("yes", result);
        }

        @Test
        void ifElseDeselectedBranch() {
            String content = "{{#if FEATURE}}yes{{#else}}no{{/if}}";
            Set<String> selected = Set.of();
            Set<String> deselected = Set.of("FEATURE");

            String result = engine.process(content, selected, deselected);

            assertEquals("no", result);
        }

        @Test
        void unlessElseSelectedBranch() {
            String content = "{{#unless FEATURE}}no{{#else}}yes{{/unless}}";
            Set<String> selected = Set.of("FEATURE");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("yes", result);
        }

        @Test
        void unlessElseDeselectedBranch() {
            String content = "{{#unless FEATURE}}yes{{#else}}no{{/unless}}";
            Set<String> selected = Set.of();
            Set<String> deselected = Set.of("FEATURE");

            String result = engine.process(content, selected, deselected);

            assertEquals("yes", result);
        }
    }

    @Nested
    class NestedConditionals {
        @Test
        void nestedIfs() {
            String content = "{{#if A}}outer{{#if B}}inner{{/if}}{{/if}}";
            Set<String> selected = Set.of("A", "B");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("outerinner", result);
        }

        @Test
        void nestedIfOuterDeselected() {
            String content = "{{#if A}}outer{{#if B}}inner{{/if}}{{/if}}";
            Set<String> selected = Set.of("B");
            Set<String> deselected = Set.of("A");

            String result = engine.process(content, selected, deselected);

            assertEquals("", result);
        }

        @Test
        void nestedIfInnerDeselected() {
            String content = "{{#if A}}outer{{#if B}}inner{{/if}}end{{/if}}";
            Set<String> selected = Set.of("A");
            Set<String> deselected = Set.of("B");

            String result = engine.process(content, selected, deselected);

            assertEquals("outerend", result);
        }

        @Test
        void deeplyNestedConditionals() {
            String content = "{{#if A}}{{#if B}}{{#if C}}deep{{/if}}{{/if}}{{/if}}";
            Set<String> selected = Set.of("A", "B", "C");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertEquals("deep", result);
        }

        @Test
        void nestedIfWithElse() {
            String content = "{{#if A}}{{#if B}}both{{#else}}justA{{/if}}{{/if}}";
            Set<String> selected = Set.of("A");
            Set<String> deselected = Set.of("B");

            String result = engine.process(content, selected, deselected);

            assertEquals("justA", result);
        }
    }

    @Nested
    class MultipleConditionals {
        @Test
        void consecutiveConditionals() {
            String content = "{{#if A}}A{{/if}}{{#if B}}B{{/if}}{{#if C}}C{{/if}}";
            Set<String> selected = Set.of("A", "C");
            Set<String> deselected = Set.of("B");

            String result = engine.process(content, selected, deselected);

            assertEquals("AC", result);
        }

        @Test
        void mixedConditionalsWithText() {
            String content = "Start {{#if A}}A{{/if}} middle {{#unless B}}notB{{/unless}} end";
            Set<String> selected = Set.of("A");
            Set<String> deselected = Set.of("B");

            String result = engine.process(content, selected, deselected);

            assertEquals("Start A middle notB end", result);
        }
    }

    @Nested
    class MultilineContent {
        @Test
        void multilineTemplateContent() {
            String content = """
                Header
                {{#if FEATURE}}
                Feature content
                {{/if}}
                Footer
                """;
            Set<String> selected = Set.of("FEATURE");
            Set<String> deselected = Set.of();

            String result = engine.process(content, selected, deselected);

            assertTrue(result.contains("Header"));
            assertTrue(result.contains("Feature content"));
            assertTrue(result.contains("Footer"));
        }

        @Test
        void multilineDeselected() {
            String content = """
                Header
                {{#if FEATURE}}
                Feature content
                {{/if}}
                Footer
                """;
            Set<String> selected = Set.of();
            Set<String> deselected = Set.of("FEATURE");

            String result = engine.process(content, selected, deselected);

            assertTrue(result.contains("Header"));
            assertFalse(result.contains("Feature content"));
            assertTrue(result.contains("Footer"));
        }
    }

    @Nested
    class FeatureNameExtraction {
        @Test
        void extractSingleFeature() {
            String content = "{{#if FEATURE}}content{{/if}}";

            Set<String> features = engine.extractFeatureNames(content);

            assertEquals(Set.of("FEATURE"), features);
        }

        @Test
        void extractMultipleFeatures() {
            String content = "{{#if A}}{{/if}}{{#unless B}}{{/unless}}{{#if C}}{{/if}}";

            Set<String> features = engine.extractFeatureNames(content);

            assertEquals(Set.of("A", "B", "C"), features);
        }

        @Test
        void extractNestedFeatures() {
            String content = "{{#if A}}{{#if B}}{{/if}}{{/if}}";

            Set<String> features = engine.extractFeatureNames(content);

            assertEquals(Set.of("A", "B"), features);
        }

        @Test
        void noFeaturesInPlainText() {
            String content = "No templates here";

            Set<String> features = engine.extractFeatureNames(content);

            assertTrue(features.isEmpty());
        }
    }

    @Nested
    class HasConditionals {
        @Test
        void detectIfConditional() {
            assertTrue(engine.hasConditionals("{{#if A}}x{{/if}}"));
        }

        @Test
        void detectUnlessConditional() {
            assertTrue(engine.hasConditionals("{{#unless A}}x{{/unless}}"));
        }

        @Test
        void noConditionals() {
            assertFalse(engine.hasConditionals("plain text"));
        }
    }

    @Nested
    class Validation {
        @Test
        void validTemplate() {
            String content = "{{#if A}}x{{/if}}";

            List<TemplateError> errors = engine.validate(content, "test.txt");

            assertTrue(errors.isEmpty());
        }

        @Test
        void unclosedIf() {
            String content = "{{#if A}}content";

            List<TemplateError> errors = engine.validate(content, "test.txt");

            assertFalse(errors.isEmpty());
            assertTrue(errors.get(0).getMessage().contains("Unclosed"));
        }

        @Test
        void mismatchedClose() {
            String content = "{{#if A}}content{{/unless}}";

            List<TemplateError> errors = engine.validate(content, "test.txt");

            assertFalse(errors.isEmpty());
        }

        @Test
        void extraClose() {
            String content = "{{#if A}}{{/if}}{{/if}}";

            List<TemplateError> errors = engine.validate(content, "test.txt");

            assertFalse(errors.isEmpty());
        }
    }

    @Nested
    class EdgeCases {
        @Test
        void emptyContent() {
            String result = engine.process("", Set.of(), Set.of());
            assertEquals("", result);
        }

        @Test
        void noConditionals() {
            String content = "plain text without conditionals";
            String result = engine.process(content, Set.of(), Set.of());
            assertEquals(content, result);
        }

        @Test
        void emptyConditionalBlock() {
            String content = "before{{#if A}}{{/if}}after";
            String result = engine.process(content, Set.of("A"), Set.of());
            assertEquals("beforeafter", result);
        }

        @Test
        void featureNamesWithUnderscores() {
            String content = "{{#if MY_FEATURE}}x{{/if}}";
            Set<String> selected = Set.of("MY_FEATURE");

            String result = engine.process(content, selected, Set.of());

            assertEquals("x", result);
        }

        @Test
        void featureNamesWithNumbers() {
            String content = "{{#if FEATURE123}}x{{/if}}";
            Set<String> selected = Set.of("FEATURE123");

            String result = engine.process(content, selected, Set.of());

            assertEquals("x", result);
        }
    }

    @Nested
    class RealWorldExamples {
        @Test
        void javaMethodConditional() {
            String content = """
                public class Main {
                    public static void main(String[] args) {
                        {{#if English}}
                        String greeting = "Hello";
                        {{/if}}
                        {{#if French}}
                        String greeting = "Bonjour";
                        {{/if}}
                        System.out.println(greeting);
                    }
                }
                """;

            Set<String> selected = Set.of("English");
            Set<String> deselected = Set.of("French");

            String result = engine.process(content, selected, deselected);

            assertTrue(result.contains("\"Hello\""));
            assertFalse(result.contains("\"Bonjour\""));
        }

        @Test
        void htmlTemplateConditional() {
            String content = """
                <html>
                <body>
                {{#if DarkMode}}
                <style>body { background: #000; color: #fff; }</style>
                {{#else}}
                <style>body { background: #fff; color: #000; }</style>
                {{/if}}
                </body>
                </html>
                """;

            Set<String> selected = Set.of("DarkMode");

            String result = engine.process(content, selected, Set.of());

            assertTrue(result.contains("background: #000"));
            assertFalse(result.contains("background: #fff"));
        }

        @Test
        void configFileConditional() {
            String content = """
                # Configuration
                server.port=8080
                {{#if Debug}}
                logging.level=DEBUG
                {{#else}}
                logging.level=INFO
                {{/if}}
                {{#if Caching}}
                cache.enabled=true
                {{/if}}
                """;

            Set<String> selected = Set.of("Caching");
            Set<String> deselected = Set.of("Debug");

            String result = engine.process(content, selected, deselected);

            assertTrue(result.contains("logging.level=INFO"));
            assertFalse(result.contains("logging.level=DEBUG"));
            assertTrue(result.contains("cache.enabled=true"));
        }
    }
}
