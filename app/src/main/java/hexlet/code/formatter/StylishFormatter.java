package hexlet.code.formatter;

import hexlet.code.differ.DiffEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StylishFormatter implements Formatter {

    private static final String RESULT_PATTERN = "{%n%s%n}";
    private static final String DIFF_PATTERN = "  %s%s: %s";
    private static final String INDENT = "  ";

    public String format(final List<DiffEntry> diff) {
        String result = diff.stream()
                .map(this::formatEntry)
                .collect(Collectors.joining("\n"));
        return String.format(RESULT_PATTERN, result);
    }

    private String formatEntry(final DiffEntry entry) {
        return switch (entry.getStatus()) {
            case UNCHANGED -> String.format(DIFF_PATTERN, INDENT, entry.getKey(), formatValue(entry.getRemovedValue()));
            case ADDED -> String.format(DIFF_PATTERN, "+ ", entry.getKey(), formatValue(entry.getAddedValue()));
            case REMOVED -> String.format(DIFF_PATTERN, "- ", entry.getKey(), formatValue(entry.getRemovedValue()));
            case CHANGED -> String.format(
                    DIFF_PATTERN + "%n" + DIFF_PATTERN,
                    "- ", entry.getKey(), formatValue(entry.getRemovedValue()),
                    "+ ", entry.getKey(), formatValue(entry.getAddedValue())
            );
        };
    }

    private String formatValue(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String string) {
            return string;
        }
        if (value instanceof List || value instanceof Map) {
            return value.toString();
        }
        return String.valueOf(value);
    }
}
