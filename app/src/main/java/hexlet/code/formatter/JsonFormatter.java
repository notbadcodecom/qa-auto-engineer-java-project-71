package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.diff.DiffEntry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonFormatter implements Formatter {

    private static final String KEY_FIELD_NAME = "key";
    private static final String STATUS_FIELD_NAME = "status";
    private static final String REMOVED_FIELD_NAME = "oldValue";
    private static final String ADDED_FIELD_NAME = "newValue";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String format(final List<DiffEntry> diff) throws JsonProcessingException {
        List<Map<String, Object>> jsonDiff = diff.stream()
                .map(entry -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put(KEY_FIELD_NAME, entry.getKey());
                    map.put(STATUS_FIELD_NAME, entry.getStatus().toString().toLowerCase());
                    map.put(REMOVED_FIELD_NAME, entry.getRemovedValue());
                    map.put(ADDED_FIELD_NAME, entry.getAddedValue());
                    return map;
                })
                .toList();
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonDiff);
    }
}
