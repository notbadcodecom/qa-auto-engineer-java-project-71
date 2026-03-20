package hexlet.code.formatter;

import hexlet.code.diff.DiffEntry;
import hexlet.code.diff.DiffStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class PlainFormatterTest {

    private static final int TIMEOUT_OLD_VALUE = 50;
    private static final int TIMEOUT_NEW_VALUE = 100;
    private static final int PORT_VALUE = 8080;

    private PlainFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new PlainFormatter();
    }

    @Test
    void testFormatWithAddedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("verbose", DiffStatus.ADDED, null, true)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'verbose' was added with value: true");
    }

    @Test
    void testFormatWithRemovedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("proxy", DiffStatus.REMOVED, "123.234.53.22", null)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'proxy' was removed");
    }

    @Test
    void testFormatWithChangedEntry() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'timeout' was updated. From 50 to 100");
    }

    @Test
    void testFormatWithMultipleEntries() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE),
                new DiffEntry("verbose", DiffStatus.ADDED, null, true),
                new DiffEntry("proxy", DiffStatus.REMOVED, "123.234.53.22", null),
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io")
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("Property 'timeout' was updated. From 50 to 100")
                .contains("Property 'verbose' was added with value: true")
                .contains("Property 'proxy' was removed")
                .doesNotContain("host");
    }

    @Test
    void testFormatWithStringValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED, "hexlet.io", "hexlet.ru")
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'host' was updated. From 'hexlet.io' to 'hexlet.ru'");
    }

    @Test
    void testFormatWithNullValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED, "hexlet.io", null)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'host' was updated. From 'hexlet.io' to null");
    }

    @Test
    void testFormatWithComplexValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED,
                        List.of("hexlet.io", "hexlet.ru"),
                        List.of("hexlet.io"))
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'host' was updated. From [complex value] to [complex value]");
    }

    @Test
    void testFormatWithMapValue() {
        Map<String, Object> mapValue = Map.of("key", "value");
        List<DiffEntry> diff = List.of(
                new DiffEntry("config", DiffStatus.ADDED, null, mapValue)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'config' was added with value: [complex value]");
    }

    @Test
    void testFormatWithNumberValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("port", DiffStatus.ADDED, null, PORT_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'port' was added with value: 8080");
    }

    @Test
    void testFormatWithBooleanValue() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("enabled", DiffStatus.ADDED, null, false)
        );

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("Property 'enabled' was added with value: false");
    }

    @Test
    void testFormatWithEmptyList() {
        List<DiffEntry> diff = List.of();

        String result = formatter.format(diff);

        assertThat(result).isEmpty();
    }

    @Test
    void testFormatWithOnlyUnchangedEntries() {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io"),
                new DiffEntry("port", DiffStatus.UNCHANGED, PORT_VALUE, PORT_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result).isEmpty();
    }
}
