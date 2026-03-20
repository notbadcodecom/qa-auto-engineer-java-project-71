package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import hexlet.code.differ.DiffEntry;

import java.util.List;

public interface Formatter {
    String format(List<DiffEntry> diff) throws JsonProcessingException;
}
