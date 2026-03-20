package hexlet.code.formatter;

public final class FormatterFactory {

    private static final Formatter STYLISH_FORMATTER = new StylishFormatter();
    private static final Formatter PLAIN_FORMATTER = new PlainFormatter();
    private static final Formatter JSON_FORMATTER = new JsonFormatter();

    private FormatterFactory() {
    }

    public static Formatter getFormatter(final FormatType formatType) {
        return switch (formatType) {
            case FormatType.STYLISH -> STYLISH_FORMATTER;
            case FormatType.PLAIN -> PLAIN_FORMATTER;
            case FormatType.JSON -> JSON_FORMATTER;
        };
    }
}
