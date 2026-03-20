package hexlet.code.diff;

import java.util.Objects;

public final class DiffEntry {

    private final String key;
    private final DiffStatus status;
    private final Object removedValue;
    private final Object addedValue;

    public DiffEntry(
            final String diffKey,
            final DiffStatus diffStatus,
            final Object diffRemovedValue,
            final Object diffAddedValue
    ) {
        this.key = diffKey;
        this.status = diffStatus;
        this.removedValue = diffRemovedValue;
        this.addedValue = diffAddedValue;
    }

    public String getKey() {
        return key;
    }

    public DiffStatus getStatus() {
        return status;
    }

    public Object getRemovedValue() {
        return removedValue;
    }

    public Object getAddedValue() {
        return addedValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiffEntry diffEntry = (DiffEntry) o;
        return Objects.equals(key, diffEntry.key)
                && status == diffEntry.status
                && Objects.equals(removedValue, diffEntry.removedValue)
                && Objects.equals(addedValue, diffEntry.addedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, status, removedValue, addedValue);
    }
}
