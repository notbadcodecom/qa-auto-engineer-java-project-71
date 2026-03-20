package hexlet.code.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class ParserTest {

    @TempDir
    private Path tempDir;

    @Test
    void testParseJsonSimple() throws Exception {
        String json = """
                {
                  "host": "hexlet.io",
                  "timeout": 50,
                  "follow": false
                }
                """;
        final int timeoutValue = 50;
        final int resultSize = 3;

        Map<String, Object> result = Parser.parse(json, "json");

        assertThat(result)
                .containsEntry("host", "hexlet.io")
                .containsEntry("timeout", timeoutValue)
                .containsEntry("follow", false);
        assertThat(result).hasSize(resultSize);
    }

    @Test
    void testParseJsonWithNullValue() throws Exception {
        String json = """
                {
                  "host": null,
                  "timeout": 50
                }
                """;
        final int timeoutValue = 50;

        Map<String, Object> result = Parser.parse(json, "json");

        assertThat(result).containsEntry("host", null);
        assertThat(result).containsEntry("timeout", timeoutValue);
    }

    @Test
    void testParseJsonWithNumbers() throws Exception {
        String json = """
                {
                  "integer": 42,
                  "float": 3.14,
                  "negative": -10,
                  "zero": 0
                }
                """;
        final int integerValue = 42;
        final double doubleValue = 3.14;
        final int negativeIntegerValue = -10;

        Map<String, Object> result = Parser.parse(json, "json");

        assertThat(result).containsEntry("integer", integerValue);
        assertThat(result).containsEntry("float", doubleValue);
        assertThat(result).containsEntry("negative", negativeIntegerValue);
        assertThat(result).containsEntry("zero", 0);
    }

    @Test
    void testParseYamlSimple() throws Exception {
        String yaml = """
                host: hexlet.io
                timeout: 50
                follow: false
                """;
        final int timeoutValue = 50;
        final int resultSize = 3;

        Map<String, Object> result = Parser.parse(yaml, "yml");

        assertThat(result)
                .containsEntry("host", "hexlet.io")
                .containsEntry("timeout", timeoutValue)
                .containsEntry("follow", false);
        assertThat(result).hasSize(resultSize);
    }

    @Test
    void testParseYamlWithNullValue() throws Exception {
        String yaml = """
                host: null
                timeout: 50
                """;
        final int timeoutValue = 50;

        Map<String, Object> result = Parser.parse(yaml, "yml");

        assertThat(result).containsEntry("host", null);
        assertThat(result).containsEntry("timeout", timeoutValue);
    }

    @Test
    void testParseYamlWithNumbers() throws Exception {
        String yaml = """
                integer: 42
                float: 3.14
                negative: -10
                zero: 0
                """;
        final int integerValue = 42;
        final double doubleValue = 3.14;
        final int negativeIntegerValue = -10;

        Map<String, Object> result = Parser.parse(yaml, "yml");

        assertThat(result).containsEntry("integer", integerValue);
        assertThat(result).containsEntry("float", doubleValue);
        assertThat(result).containsEntry("negative", negativeIntegerValue);
        assertThat(result).containsEntry("zero", 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"json", "yml", "yaml"})
    void testParseWithDifferentExtensions(final String extension) throws Exception {
        String content = "{\"key\": \"value\"}";

        if (extension.equals("yml") || extension.equals("yaml")) {
            content = "key: value";
        }

        Map<String, Object> result = Parser.parse(content, extension);

        assertThat(result).containsEntry("key", "value");
    }

    @Test
    void testParseEmptyJson() throws Exception {
        String json = "{}";

        Map<String, Object> result = Parser.parse(json, "json");

        assertThat(result).isEmpty();
    }

    @Test
    void testParseEmptyYaml() throws Exception {
        String yaml = "";

        Map<String, Object> result = Parser.parse(yaml, "yml");

        assertThat(result).isEmpty();
    }

    @Test
    void testParseInvalidJson() {
        String invalidJson = """
                {
                  "host": "hexlet.io",
                  timeout: 50,
                }
                """;

        assertThatThrownBy(() -> Parser.parse(invalidJson, "json"))
                .isInstanceOf(JsonProcessingException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"txt", "xml", "html", "csv"})
    void testParseUnsupportedExtensions(final String extension) {
        String content = "some content";

        assertThatThrownBy(() -> Parser.parse(content, extension))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported file: " + extension);
    }

    @Test
    void testParseJsonWithCharacters() throws Exception {
        String json = """
                {
                  "string_with_quotes": "He said \\"Hello\\"",
                  "string_with_unicode": "Привет мир",
                  "string_with_escape": "Line1\\nLine2"
                }
                """;

        Map<String, Object> result = Parser.parse(json, "json");

        assertThat(result).containsEntry("string_with_quotes", "He said \"Hello\"");
        assertThat(result).containsEntry("string_with_unicode", "Привет мир");
        assertThat(result).containsEntry("string_with_escape", "Line1\nLine2");
    }

    @Test
    void testParseYamlWithCharacters() throws Exception {
        String yaml = """
                string_with_quotes: 'He said "Hello"'
                string_with_unicode: Привет мир
                string_with_escape: "Line1\\nLine2"
                """;

        Map<String, Object> result = Parser.parse(yaml, "yml");

        assertThat(result).containsEntry("string_with_quotes", "He said \"Hello\"");
        assertThat(result).containsEntry("string_with_unicode", "Привет мир");
        assertThat(result).containsEntry("string_with_escape", "Line1\nLine2");
    }

    @Test
    void testParseJsonWithBooleanValues() throws Exception {
        String json = """
                {
                  "true_value": true,
                  "false_value": false,
                  "null_value": null
                }
                """;

        Map<String, Object> result = Parser.parse(json, "json");

        assertThat(result).containsEntry("true_value", true);
        assertThat(result).containsEntry("false_value", false);
        assertThat(result).containsEntry("null_value", null);
    }

    @Test
    void testParseYamlWithBooleanValues() throws Exception {
        String yaml = """
                true_value: true
                false_value: false
                null_value: null
                """;

        Map<String, Object> result = Parser.parse(yaml, "yml");

        assertThat(result).containsEntry("true_value", true);
        assertThat(result).containsEntry("false_value", false);
        assertThat(result).containsEntry("null_value", null);
    }

    @Test
    void testParseJsonFile() throws Exception {
        Path jsonFile = tempDir.resolve("test.json");
        String jsonContent = "{\"key\": \"value\", \"number\": 1}";
        Files.writeString(jsonFile, jsonContent);

        String content = Files.readString(jsonFile);
        Map<String, Object> result = Parser.parse(content, "json");

        assertThat(result).containsEntry("key", "value");
        assertThat(result).containsEntry("number", 1);
    }

    @Test
    void testParseYamlFile() throws Exception {
        Path yamlFile = tempDir.resolve("test.yml");
        String yamlContent = "key: value\nnumber: 2";
        Files.writeString(yamlFile, yamlContent);

        String content = Files.readString(yamlFile);
        Map<String, Object> result = Parser.parse(content, "yml");

        assertThat(result).containsEntry("key", "value");
        assertThat(result).containsEntry("number", 2);
    }
}
