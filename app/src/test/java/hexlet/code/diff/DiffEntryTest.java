package hexlet.code.diff;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiffEntryTest {

    @Test
    void testConstructorAndGetters() {
        final String key = "timeout";
        final DiffStatus status = DiffStatus.CHANGED;
        final Object removedValue = 50;
        final Object addedValue = 100;

        DiffEntry entry = new DiffEntry(key, status, removedValue, addedValue);

        assertThat(entry.getKey()).isEqualTo(key);
        assertThat(entry.getStatus()).isEqualTo(status);
        assertThat(entry.getRemovedValue()).isEqualTo(removedValue);
        assertThat(entry.getAddedValue()).isEqualTo(addedValue);
    }

    @Test
    void testConstructorWithNullValues() {
        DiffEntry entry = new DiffEntry("proxy", DiffStatus.ADDED, null, "new-value");

        assertThat(entry.getKey()).isEqualTo("proxy");
        assertThat(entry.getStatus()).isEqualTo(DiffStatus.ADDED);
        assertThat(entry.getRemovedValue()).isNull();
        assertThat(entry.getAddedValue()).isEqualTo("new-value");
    }

    @Test
    void testConstructorWithAllNulls() {
        DiffEntry entry = new DiffEntry("key", DiffStatus.UNCHANGED, null, null);

        assertThat(entry.getKey()).isEqualTo("key");
        assertThat(entry.getStatus()).isEqualTo(DiffStatus.UNCHANGED);
        assertThat(entry.getRemovedValue()).isNull();
        assertThat(entry.getAddedValue()).isNull();
    }

    @Test
    void testConstructorWithStringValues() {
        DiffEntry entry = new DiffEntry("host", DiffStatus.CHANGED, "hexlet.io", "hexlet.ru");

        assertThat(entry.getKey()).isEqualTo("host");
        assertThat(entry.getStatus()).isEqualTo(DiffStatus.CHANGED);
        assertThat(entry.getRemovedValue()).isEqualTo("hexlet.io");
        assertThat(entry.getAddedValue()).isEqualTo("hexlet.ru");
    }

    @Test
    void testConstructorWithNumericValues() {
        final int port = 8080;

        DiffEntry entry = new DiffEntry("port", DiffStatus.ADDED, null, port);

        assertThat(entry.getKey()).isEqualTo("port");
        assertThat(entry.getStatus()).isEqualTo(DiffStatus.ADDED);
        assertThat(entry.getRemovedValue()).isNull();
        assertThat(entry.getAddedValue()).isEqualTo(port);
    }

    @Test
    void testConstructorWithBooleanValues() {
        DiffEntry entry = new DiffEntry("enabled", DiffStatus.CHANGED, true, false);

        assertThat(entry.getKey()).isEqualTo("enabled");
        assertThat(entry.getStatus()).isEqualTo(DiffStatus.CHANGED);
        assertThat(entry.getRemovedValue()).isEqualTo(true);
        assertThat(entry.getAddedValue()).isEqualTo(false);
    }

    @Test
    void testConstructorWithComplexValues() {
        List<String> removedList = List.of("a", "b", "c");
        List<String> addedList = List.of("a", "b");

        DiffEntry entry = new DiffEntry("tags", DiffStatus.CHANGED, removedList, addedList);

        assertThat(entry.getKey()).isEqualTo("tags");
        assertThat(entry.getStatus()).isEqualTo(DiffStatus.CHANGED);
        assertThat(entry.getRemovedValue()).isEqualTo(removedList);
        assertThat(entry.getAddedValue()).isEqualTo(addedList);
    }

    @Test
    void testEntryWithAllDiffStatuses() {
        DiffEntry addedEntry = new DiffEntry("key1", DiffStatus.ADDED, null, "value");
        DiffEntry removedEntry = new DiffEntry("key2", DiffStatus.REMOVED, "value", null);
        DiffEntry changedEntry = new DiffEntry("key3", DiffStatus.CHANGED, "old", "new");
        DiffEntry unchangedEntry = new DiffEntry("key4", DiffStatus.UNCHANGED, "same", "same");

        assertThat(addedEntry.getStatus())
                .isNotNull()
                .isEqualTo(DiffStatus.ADDED);
        assertThat(removedEntry.getStatus())
                .isNotNull()
                .isEqualTo(DiffStatus.REMOVED);
        assertThat(changedEntry.getStatus())
                .isNotNull()
                .isEqualTo(DiffStatus.CHANGED);
        assertThat(unchangedEntry.getStatus())
                .isNotNull()
                .isEqualTo(DiffStatus.UNCHANGED);
    }

    @Test
    void testEntryWithNumericValues() {
        final int removedIntegerValue = 1;
        final int addedIntegerValue = 2;
        final double removedDoubleValue = 1.5;
        final double addedDoubleValue = 2.5;
        final long removedLongValue = 100L;
        final long addedLongValue = 200L;

        DiffEntry intEntry = new DiffEntry("int", DiffStatus.CHANGED, removedIntegerValue, addedIntegerValue);
        DiffEntry doubleEntry = new DiffEntry("double", DiffStatus.CHANGED, removedDoubleValue, addedDoubleValue);
        DiffEntry longEntry = new DiffEntry("long", DiffStatus.CHANGED, removedLongValue, addedLongValue);

        assertThat(intEntry)
                .hasFieldOrPropertyWithValue("removedValue", removedIntegerValue)
                .hasFieldOrPropertyWithValue("addedValue", addedIntegerValue);
        assertThat(doubleEntry)
                .hasFieldOrPropertyWithValue("removedValue", removedDoubleValue)
                .hasFieldOrPropertyWithValue("addedValue", addedDoubleValue);
        assertThat(longEntry)
                .hasFieldOrPropertyWithValue("removedValue", removedLongValue)
                .hasFieldOrPropertyWithValue("addedValue", addedLongValue);
    }


    // DeepSeek generated tests (for coverage)
    @Test
    void testEqualsSameObject() {
        DiffEntry entry = new DiffEntry("key", DiffStatus.UNCHANGED, "value", "value");

        assertThat(entry.equals(entry)).isTrue();
    }

    @Test
    void testEqualsEqualObjects() {
        DiffEntry entry1 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);
        DiffEntry entry2 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);

        assertThat(entry1.equals(entry2)).isTrue();
        assertThat(entry2.equals(entry1)).isTrue();
    }

    @Test
    void testEqualsDifferentKey() {
        DiffEntry entry1 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);
        DiffEntry entry2 = new DiffEntry("timeout2", DiffStatus.CHANGED, 50, 100);

        assertThat(entry1.equals(entry2)).isFalse();
    }

    @Test
    void testEqualsDifferentStatus() {
        DiffEntry entry1 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);
        DiffEntry entry2 = new DiffEntry("timeout", DiffStatus.ADDED, 50, 100);

        assertThat(entry1.equals(entry2)).isFalse();
    }

    @Test
    void testEqualsDifferentRemovedValue() {
        DiffEntry entry1 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);
        DiffEntry entry2 = new DiffEntry("timeout", DiffStatus.CHANGED, 60, 100);

        assertThat(entry1.equals(entry2)).isFalse();
    }

    @Test
    void testEqualsDifferentAddedValue() {
        DiffEntry entry1 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);
        DiffEntry entry2 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 200);

        assertThat(entry1.equals(entry2)).isFalse();
    }

    @Test
    void testEqualsWithNullValues() {
        DiffEntry entry1 = new DiffEntry("proxy", DiffStatus.ADDED, null, "value");
        DiffEntry entry2 = new DiffEntry("proxy", DiffStatus.ADDED, null, "value");

        assertThat(entry1.equals(entry2)).isTrue();
    }

    @Test
    void testEqualsWithNullDifferent() {
        DiffEntry entry1 = new DiffEntry("proxy", DiffStatus.ADDED, null, "value");
        DiffEntry entry2 = new DiffEntry("proxy", DiffStatus.ADDED, "old", "value");

        assertThat(entry1.equals(entry2)).isFalse();
    }

    @Test
    void testEqualsWithNullObject() {
        DiffEntry entry = new DiffEntry("key", DiffStatus.UNCHANGED, "value", "value");

        assertThat(entry.equals(null)).isFalse();
    }

    @Test
    void testEqualsDifferentClass() {
        DiffEntry entry = new DiffEntry("key", DiffStatus.UNCHANGED, "value", "value");
        String notDiffEntry = "not a diff entry";

        assertThat(entry.equals(notDiffEntry)).isFalse();
    }

    @Test
    void testHashCodeEqualForEqualObjects() {
        DiffEntry entry1 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);
        DiffEntry entry2 = new DiffEntry("timeout", DiffStatus.CHANGED, 50, 100);

        assertThat(entry1.hashCode()).isEqualTo(entry2.hashCode());
    }
}
