package hexlet.code.formatter;

import hexlet.code.differ.DiffEntry;
import hexlet.code.differ.DiffStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class JsonFormatterTest {

    private static final int TIMEOUT_OLD_VALUE = 50;
    private static final int TIMEOUT_NEW_VALUE = 100;

    private JsonFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new JsonFormatter();
    }

    @Test
    void testFormatWithUnchangedEntry() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io")
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"key\" : \"host\"")
                .contains("\"status\" : \"unchanged\"")
                .contains("\"oldValue\" : \"hexlet.io\"")
                .contains("\"newValue\" : \"hexlet.io\"");
    }

    @Test
    void testFormatWithAddedEntry() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("verbose", DiffStatus.ADDED, null, true)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"key\" : \"verbose\"")
                .contains("\"status\" : \"added\"")
                .contains("\"oldValue\" : null")
                .contains("\"newValue\" : true");
    }

    @Test
    void testFormatWithRemovedEntry() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("proxy", DiffStatus.REMOVED, "123.234.53.22", null)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"key\" : \"proxy\"")
                .contains("\"status\" : \"removed\"")
                .contains("\"oldValue\" : \"123.234.53.22\"")
                .contains("\"newValue\" : null");
    }

    @Test
    void testFormatWithChangedEntry() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"key\" : \"timeout\"")
                .contains("\"status\" : \"changed\"")
                .contains("\"oldValue\" : 50")
                .contains("\"newValue\" : 100");
    }

    @Test
    void testFormatWithMultipleEntries() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.UNCHANGED, "hexlet.io", "hexlet.io"),
                new DiffEntry("timeout", DiffStatus.CHANGED, TIMEOUT_OLD_VALUE, TIMEOUT_NEW_VALUE),
                new DiffEntry("verbose", DiffStatus.ADDED, null, true),
                new DiffEntry("proxy", DiffStatus.REMOVED, "123.234.53.22", null)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"key\" : \"host\"", "\"key\" : \"timeout\"",
                        "\"key\" : \"verbose\"", "\"key\" : \"proxy\"")
                .contains("\"status\" : \"unchanged\"", "\"status\" : \"changed\"",
                        "\"status\" : \"added\"", "\"status\" : \"removed\"");
    }

    @Test
    void testFormatWithComplexValue() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("host", DiffStatus.CHANGED,
                        List.of("hexlet.io", "hexlet.ru", "hexlet.com"),
                        List.of("hexlet.io", "hexlet.ru"))
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"key\" : \"host\"")
                .contains("\"status\" : \"changed\"");
    }

    @Test
    void testFormatWithEmptyList() throws Exception {
        List<DiffEntry> diff = List.of();

        String result = formatter.format(diff);

        assertThat(result).isEqualTo("[ ]");
    }

    @Test
    void testFormatWithNullValues() throws Exception {
        List<DiffEntry> diff = List.of(
                new DiffEntry("nullKey", DiffStatus.CHANGED, null, null)
        );

        String result = formatter.format(diff);

        assertThat(result)
                .contains("\"oldValue\" : null")
                .contains("\"newValue\" : null");
    }
}
