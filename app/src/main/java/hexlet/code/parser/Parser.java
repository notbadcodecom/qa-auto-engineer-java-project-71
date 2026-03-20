package hexlet.code.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.util.Map;

public final class Parser {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();

    private Parser() {
    }

    public static Map<String, Object> parse(
            final String content,
            final String extension
    ) throws JsonProcessingException {
        if (content.isEmpty()) {
            return Map.of();
        }
        return switch (extension) {
            case "json" -> JSON_MAPPER.readValue(content, new TypeReference<>() { });
            case "yml", "yaml" -> YAML_MAPPER.readValue(content, new TypeReference<>() { });
            default -> throw new IllegalArgumentException("Unsupported file: " + extension);
        };
    }
}
