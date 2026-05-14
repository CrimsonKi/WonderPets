package wonderpets.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model holding a collection of {@link SymptomEntry} objects.
 * Supports exact-match lookup, prefix-based auto-suggestion, and listener notification
 * on mutation via the push-MVC pattern inherited from {@link AbstractModel}.
 */
public class SymptomIndex extends AbstractModel {

    private final List<SymptomEntry> entries = new ArrayList<>();

    /**
     * Adds an entry to the index and notifies listeners.
     *
     * @param entry the entry to add; must not be {@code null}
     */
    public void addEntry(SymptomEntry entry) {
        if (entry == null) throw new IllegalArgumentException("entry must not be null");
        entries.add(entry);
        notifyChanged();
    }

    /**
     * Removes the first entry whose symptom matches {@code symptom} case-insensitively.
     *
     * @param symptom the symptom name to remove
     * @return {@code true} if an entry was found and removed, {@code false} otherwise
     */
    public boolean removeEntry(String symptom) {
        String key = normalize(symptom);
        for (int i = 0; i < entries.size(); i++) {
            if (normalize(entries.get(i).getSymptom()).equals(key)) {
                entries.remove(i);
                notifyChanged();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the first entry whose symptom exactly matches {@code symptom}
     * (case-insensitive), or {@code null} if none is found.
     *
     * @param symptom the symptom name to look up
     * @return the matching {@link SymptomEntry}, or {@code null}
     */
    public SymptomEntry search(String symptom) {
        String key = normalize(symptom);
        for (SymptomEntry entry : entries) {
            if (normalize(entry.getSymptom()).equals(key)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Returns all entries whose symptom starts with {@code prefix} (case-insensitive).
     * Returns an empty list when {@code prefix} is {@code null}, empty, or blank.
     *
     * @param prefix the prefix to match against
     * @return unmodifiable list of matching entries
     */
    public List<SymptomEntry> autoSuggest(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return Collections.emptyList();
        }
        String key = normalize(prefix);
        List<SymptomEntry> matches = new ArrayList<>();
        for (SymptomEntry entry : entries) {
            if (normalize(entry.getSymptom()).startsWith(key)) {
                matches.add(entry);
            }
        }
        return Collections.unmodifiableList(matches);
    }

    /**
     * Returns an unmodifiable view of all entries in the index.
     *
     * @return all entries
     */
    public List<SymptomEntry> getAll() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Returns the number of entries in the index.
     *
     * @return entry count
     */
    public int size() {
        return entries.size();
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
