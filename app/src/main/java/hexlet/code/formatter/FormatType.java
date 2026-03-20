package hexlet.code.formatter;

public enum FormatType {
    STYLISH,
    PLAIN,
    JSON;

    public static FormatType fromString(final String format) {
        try {
            return FormatType.valueOf(format.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Unsupported format: %s", format));
        }
    }
}
