package hexlet.code.formatter;

import hexlet.code.differ.DiffEntry;
import hexlet.code.differ.DiffStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StylishFormatterTest {

    private static final int TIMEOUT_OLD_VALUE = 50;
    private static final int TIMEOUT_NEW_VALUE = 100;
    private static final int PORT_VALUE = 8080;

    private StylishFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new StylishFormatter();
    }

    @Test
    void testFormatWithUnchangedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io")
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace("{\n    host: hexlet.io\n}");
    }

    @Test
    void testFormatWithAddedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("verbose", DiffStatus.ADDED, null, true)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace("{\n  + verbose: true\n}");
    }

    @Test
    void testFormatWithRemovedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("proxy", DiffStatus.REMOVED, "123.234.53.22", null)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace("{\n  - proxy: 123.234.53.22\n}");
    }

    @Test
    void testFormatWithChangedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace(
                "{\n  - timeout: 50\n  + timeout: 100\n  }"
        );
    }

    @Test
    void testFormatWithMultipleEntries() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io"),
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE),
                new DiffEntry("verbose", DiffStatus.ADDED, null, true),
                new DiffEntry("proxy", DiffStatus.REMOVED, "123.234.53.22", null)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("    host: hexlet.io")
                .contains("  - timeout: 50")
                .contains("  + timeout: 100")
                .contains("  + verbose: true")
                .contains("  - proxy: 123.234.53.22");
    }

    @Test
    void testFormatWithStringValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED, "hexlet.io", "hexlet.ru")
        );

        String result = formatter.format(diff);

        assertThat(result)
                .isEqualToNormalizingPunctuationAndWhitespace("{\n  - host: hexlet.io\n  + host: hexlet.ru\n}");
    }

    @Test
    void testFormatWithNullValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED, "hexlet.io", null)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .isEqualToNormalizingPunctuationAndWhitespace("{\n  - host: hexlet.io\n  + host: null\n}");
    }

    @Test
    void testFormatWithComplexValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED,
                        List.of("hexlet.io", "hexlet.ru", "hexlet.com"),
                        List.of("hexlet.io", "hexlet.ru"))
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("  - host: [hexlet.io, hexlet.ru, hexlet.com]")
                .contains("  + host: [hexlet.io, hexlet.ru]");
    }

    @Test
    void testFormatWithNumberValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("port", DiffStatus.ADDED, null, PORT_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace("{\n  + port: 8080\n}");
    }

    @Test
    void testFormatWithBooleanValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("enabled", DiffStatus.ADDED, null, false)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace("{\n  + enabled: false\n}");
    }

    @Test
    void testFormatWithEmptyList() {
        List<DiffEntry> diff = List.of();

        String result = formatter.format(diff);

        assertThat(result).isEqualToNormalizingPunctuationAndWhitespace("{\n\n}");
    }

    @Test
    void testFormatWithMixedIndentation() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io"),
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("    host: hexlet.io")
                .contains("  - timeout: 50")
                .contains("  + timeout: 100");
    }
}
