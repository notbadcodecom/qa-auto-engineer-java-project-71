package hexlet.code.formatter;

import hexlet.code.diff.DiffEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hexlet.code.diff.DiffStatus.UNCHANGED;

public final class PlainFormatter implements Formatter {

    private static final String ADDED_INFO_PATTERN = "Property '%s' was added with value: %s";
    private static final String REMOVED_INFO_PATTERN = "Property '%s' was removed";
    private static final String CHANGED_INFO_PATTERN = "Property '%s' was updated. From %s to %s";
    private static final String UNSUPPORTED_DIFF_PATTERN = "Unsupported diff status: %s";
    private static final String QUOTAS_STRING_PATTERN = "'%s'";

    public String format(final List<DiffEntry> diff) {
        return diff.stream()
                .filter(entry -> entry.getStatus() != UNCHANGED)
                .map(this::formatEntry)
                .collect(Collectors.joining("\n"));
    }

    private String formatEntry(final DiffEntry entry) {
        return switch (entry.getStatus()) {
            case ADDED -> String.format(ADDED_INFO_PATTERN, entry.getKey(), formatValue(entry.getAddedValue()));
            case REMOVED -> String.format(REMOVED_INFO_PATTERN, entry.getKey());
            case CHANGED -> String.format(
                    CHANGED_INFO_PATTERN,
                    entry.getKey(),
                    formatValue(entry.getRemovedValue()),
                    formatValue(entry.getAddedValue())
            );
            default -> throw new IllegalArgumentException(String.format(UNSUPPORTED_DIFF_PATTERN, entry.getStatus()));
        };
    }

    private String formatValue(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return String.format(QUOTAS_STRING_PATTERN, value);
        }
        if (value instanceof List || value instanceof Map) {
            return "[complex value]";
        }
        return String.valueOf(value);
    }
}
