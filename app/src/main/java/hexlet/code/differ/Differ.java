package hexlet.code.differ;

import hexlet.code.formatter.Formatter;
import hexlet.code.formatter.FormatType;
import hexlet.code.formatter.FormatterFactory;
import hexlet.code.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static hexlet.code.differ.DiffStatus.ADDED;
import static hexlet.code.differ.DiffStatus.CHANGED;
import static hexlet.code.differ.DiffStatus.REMOVED;
import static hexlet.code.differ.DiffStatus.UNCHANGED;

public final class Differ {

    private static final int NOT_EXISTENT_DOT_INDEX = 0;
    private static final int INDEX_SHIFT = 1;
    private static final String NOT_FOUND_PATTERN = "File not found: %s";
    private static final String NO_EXTENSION_PATTERN = "File '%s' has not extension";
    private static final String DOT = ".";

    private Differ() {
    }

    public static String generate(
            final String filepath1,
            final String filepath2,
            final FormatType formatType
    ) throws IOException {
        Map<String, Object> parsedFile1 = Parser.parse(readFile(filepath1), parseExtension(filepath1));
        Map<String, Object> parsedFile2 = Parser.parse(readFile(filepath2), parseExtension(filepath2));
        List<DiffEntry> diff = build(parsedFile1, parsedFile2);
        Formatter formatter = FormatterFactory.getFormatter(formatType);
        return formatter.format(diff);
    }

    private static String readFile(final String filepath) throws IOException {
        Path path = Paths.get(filepath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(String.format(NOT_FOUND_PATTERN, filepath));
        }
        return Files.readString(path);
    }

    private static String parseExtension(final String filepath) {
        int lastDotIndex = filepath.lastIndexOf(DOT) + INDEX_SHIFT;
        if (lastDotIndex == NOT_EXISTENT_DOT_INDEX) {
            throw new IllegalArgumentException(String.format(NO_EXTENSION_PATTERN, filepath));
        }
        return filepath.substring(lastDotIndex).toLowerCase();
    }

    private static List<DiffEntry> build(
            final Map<String, Object> parsedFile1,
            final Map<String, Object> parsedFile2
    ) {
        List<DiffEntry> diff = new ArrayList<>();
        Set<String> allKeys = new TreeSet<>(parsedFile1.keySet());
        allKeys.addAll(parsedFile2.keySet());

        for (String key : allKeys) {
            boolean inFirst = parsedFile1.containsKey(key);
            boolean inSecond = parsedFile2.containsKey(key);
            Object firstValue = parsedFile1.get(key);
            Object secondValue = parsedFile2.get(key);
            DiffEntry entry;

            if (inFirst && inSecond && firstValue.equals(secondValue)) {
                entry = new DiffEntry(key, UNCHANGED, firstValue, secondValue);
            } else if (inFirst && inSecond) {
                entry = new DiffEntry(key, CHANGED, firstValue, secondValue);
            } else if (inFirst) {
                entry = new DiffEntry(key, REMOVED, firstValue, null);
            } else {
                entry = new DiffEntry(key, ADDED, null, secondValue);
            }
            diff.add(entry);
        }
        return diff;
    }
}
