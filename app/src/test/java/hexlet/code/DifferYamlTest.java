package hexlet.code;

import hexlet.code.diff.DiffStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class DifferYamlTest {

    @TempDir
    private Path tempDir;

    private Path filepath1;
    private Path filepath2;

    @BeforeEach
    void setUp() {
        filepath1 = tempDir.resolve("file1.yml");
        filepath2 = tempDir.resolve("file2.yml");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(filepath1);
        Files.deleteIfExists(filepath2);
    }

    @Test
    void testEqualFilesStylishFormatter() throws IOException {
        String yaml1 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "stylish");

        assertThat(result)
                .contains("host: hexlet.io", "timeout: 50", "proxy: 123.234.53.22", "follow: false")
                .doesNotContain("  -", "  +");
    }

    @Test
    void testAddedKeyStylishFormatter() throws IOException {
        String yaml1 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                verbose: true
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "stylish");

        assertThat(result)
                .contains("host: hexlet.io", "timeout: 50", "proxy: 123.234.53.22", "follow: false")
                .contains("  + verbose: true");
    }

    @Test
    void testRemovedKeyStylishFormatter() throws IOException {
        String yaml1 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                verbose: true
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "stylish");

        assertThat(result)
                .contains("host: hexlet.io", "timeout: 50", "proxy: 123.234.53.22", "follow: false")
                .contains("  - verbose: true");
    }

    @Test
    void testChangedValueStylishFormatter() throws IOException {
        String yaml1 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                follow: false
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host: hexlet.io
                timeout: 50
                proxy: 234.345.64.11
                follow: true
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "stylish");

        assertThat(result)
                .contains("host: hexlet.io", "timeout: 50")
                .contains("  - proxy: 123.234.53.22")
                .contains("  + proxy: 234.345.64.11")
                .contains("  - follow: false")
                .contains("  + follow: true");
    }

    @Test
    void testArrayValueStylishFormatter() throws IOException {
        String yaml1 = """
                host:
                  - hexlet.io
                  - hexlet.ru
                  - hexlet.com
                timeout:
                  - 50
                  - 100
                  - 150
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host:
                  - hexlet.io
                  - hexlet.ru
                timeout:
                  - 50
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "stylish");

        assertThat(result)
                .contains("  + host: [hexlet.io, hexlet.ru]")
                .contains("  - host: [hexlet.io, hexlet.ru, hexlet.com]")
                .contains("  + timeout: [50]")
                .contains("  - timeout: [50, 100, 150]");
    }

    @Test
    void testNullValuesStylishFormatter() throws IOException {
        String yaml1 = """
                host: hexlet.io
                timeout: 50
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host: null
                timeout: null
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "stylish");

        assertThat(result)
                .contains("  - host: hexlet.io")
                .contains("  + host: null")
                .contains("  - timeout: 50")
                .contains("  + timeout: null");
    }

    @Test
    void testPlainFormatter() throws IOException {
        String yaml1 = """
                host: hexlet.io
                timeout: 50
                proxy: 123.234.53.22
                verbose: true
                """;
        Files.writeString(filepath1, yaml1);
        String yaml2 = """
                host: hexlet.io
                timeout: 100
                proxy: 234.345.64.11
                follow: true
                """;
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "plain");

        assertThat(result)
                .doesNotContain("host")
                .contains("Property 'timeout' was updated. From 50 to 100")
                .contains("Property 'verbose' was removed")
                .contains("Property 'follow' was added with value: true");
    }

    @ParameterizedTest
    @MethodSource(value = "provideSecondYamlFiles")
    void testJsonFormatter(
            final String yaml2,
            final String expectedKey,
            final String expectedStatus,
            final String expectedOldValue,
            final String expectedNewValue
    )  throws IOException {
        String yaml1 = "host: hexlet.io";
        Files.writeString(filepath1, yaml1);
        Files.writeString(filepath2, yaml2);

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "json");

        assertThat(result)
                .contains(String.format("\"key\" : \"%s\"", expectedKey))
                .contains(String.format("\"status\" : \"%s\"", expectedStatus))
                .contains(String.format("\"oldValue\" : %s", formatWithQuotasIfNotNull(expectedOldValue)))
                .contains(String.format("\"newValue\" : %s", formatWithQuotasIfNotNull(expectedNewValue)));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testBoolValue(final boolean boolValue) throws IOException {
        String yaml = "bool: %s";
        Files.writeString(filepath1, String.format(yaml, boolValue));
        Files.writeString(filepath2, String.format(yaml, boolValue));

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "json");

        assertThat(result)
                .contains(String.format("\"key\" : \"%s\"", "bool"))
                .contains(String.format("\"status\" : \"%s\"", DiffStatus.UNCHANGED.name().toLowerCase()))
                .contains(String.format("\"oldValue\" : %s", boolValue))
                .contains(String.format("\"newValue\" : %s", boolValue));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void testIntValue(final int intValue) throws IOException {
        String yaml = "int: %s";
        Files.writeString(filepath1, String.format(yaml, intValue));
        Files.writeString(filepath2, String.format(yaml, intValue));

        String result = Differ.generate(filepath1.toString(), filepath2.toString(), "json");

        assertThat(result)
                .contains(String.format("\"key\" : \"%s\"", "int"))
                .contains(String.format("\"status\" : \"%s\"", DiffStatus.UNCHANGED.name().toLowerCase()))
                .contains(String.format("\"oldValue\" : %s", intValue))
                .contains(String.format("\"newValue\" : %s", intValue));
    }

    @Test
    void testFileNotFound() {
        String nonExistentFile = "nonexistent.yml";

        assertThatThrownBy(() -> Differ.generate(nonExistentFile, nonExistentFile, "stylish"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File not found");
    }

    private String formatWithQuotasIfNotNull(final String value) {
        return (value == null) ? null : String.format("\"%s\"", value);
    }

    private static Stream<Arguments> provideSecondYamlFiles() {
        return Stream.of(
                Arguments.of(
                        "host: hexlet.io",
                        "host",
                        DiffStatus.UNCHANGED.name().toLowerCase(),
                        "hexlet.io",
                        "hexlet.io"
            ),
                Arguments.of(
                        "host: hexlet.ru",
                        "host",
                        DiffStatus.CHANGED.name().toLowerCase(),
                        "hexlet.io",
                        "hexlet.ru"
            ),
                Arguments.of(
                        "host: hexlet.io\nproxy: 234.345.64.11",
                        "proxy",
                        DiffStatus.ADDED.name().toLowerCase(),
                        null,
                        "234.345.64.11"
            ),
                Arguments.of(
                        "proxy: 234.345.64.11",
                        "host",
                        DiffStatus.REMOVED.name().toLowerCase(),
                        "hexlet.io",
                        null
            )
        );
    }
}
