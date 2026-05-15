package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SymptomIndexTest {

    private SymptomIndex index;

    @BeforeEach
    void setUp() {
        index = new SymptomIndex();
    }

    // ── addEntry ──────────────────────────────────────────────────────────────

    @Test
    void addEntry_singleEntry_sizeIsOne() {
        index.addEntry(sample("Fatigue"));
        assertEquals(1, index.size());
    }

    @Test
    void addEntry_multipleEntries_sizeMatchesCount() {
        index.addEntry(sample("Fatigue"));
        index.addEntry(sample("Bloating"));
        index.addEntry(sample("Anxiety"));
        assertEquals(3, index.size());
    }

    @Test
    void addEntry_null_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> index.addEntry(null));
    }

    // ── search ────────────────────────────────────────────────────────────────

    @Test
    void search_exactMatch_returnsEntry() {
        index.addEntry(sample("Fatigue"));
        SymptomEntry result = index.search("Fatigue");
        assertNotNull(result);
        assertEquals("Fatigue", result.getSymptom());
    }

    @Test
    void search_lowercase_returnsEntry() {
        index.addEntry(sample("Fatigue"));
        assertNotNull(index.search("fatigue"));
    }

    @Test
    void search_uppercase_returnsEntry() {
        index.addEntry(sample("Fatigue"));
        assertNotNull(index.search("FATIGUE"));
    }

    @Test
    void search_mixedCase_returnsEntry() {
        index.addEntry(sample("Fatigue"));
        assertNotNull(index.search("fAtIgUe"));
    }

    @Test
    void search_leadingAndTrailingWhitespace_returnsEntry() {
        index.addEntry(sample("Fatigue"));
        assertNotNull(index.search("  Fatigue  "));
    }

    @Test
    void search_unknownSymptom_returnsNull() {
        index.addEntry(sample("Fatigue"));
        assertNull(index.search("Nausea"));
    }

    @Test
    void search_emptyIndex_returnsNull() {
        assertNull(index.search("Fatigue"));
    }

    @Test
    void search_returnsFirstMatchingEntry() {
        SymptomEntry first = sample("Bloating");
        index.addEntry(first);
        index.addEntry(sample("Fatigue"));
        assertSame(first, index.search("bloating"));
    }

    // ── removeEntry ───────────────────────────────────────────────────────────

    @Test
    void removeEntry_existingSymptom_returnsTrue() {
        index.addEntry(sample("Fatigue"));
        assertTrue(index.removeEntry("Fatigue"));
    }

    @Test
    void removeEntry_existingSymptom_decreasesSize() {
        index.addEntry(sample("Fatigue"));
        index.addEntry(sample("Bloating"));
        index.removeEntry("Fatigue");
        assertEquals(1, index.size());
    }

    @Test
    void removeEntry_existingSymptom_entryNoLongerFound() {
        index.addEntry(sample("Fatigue"));
        index.removeEntry("Fatigue");
        assertNull(index.search("Fatigue"));
    }

    @Test
    void removeEntry_caseInsensitive_returnsTrue() {
        index.addEntry(sample("Fatigue"));
        assertTrue(index.removeEntry("FATIGUE"));
    }

    @Test
    void removeEntry_unknownSymptom_returnsFalse() {
        index.addEntry(sample("Fatigue"));
        assertFalse(index.removeEntry("Nausea"));
    }

    @Test
    void removeEntry_emptyIndex_returnsFalse() {
        assertFalse(index.removeEntry("Fatigue"));
    }

    @Test
    void removeEntry_doesNotAffectOtherEntries() {
        index.addEntry(sample("Fatigue"));
        index.addEntry(sample("Bloating"));
        index.removeEntry("Fatigue");
        assertNotNull(index.search("Bloating"));
    }

    // ── autoSuggest ───────────────────────────────────────────────────────────

    @Test
    void autoSuggest_matchingPrefix_returnsEntries() {
        index.addEntry(sample("Fatigue"));
        index.addEntry(sample("Bloating"));
        List<SymptomEntry> results = index.autoSuggest("Fa");
        assertEquals(1, results.size());
        assertEquals("Fatigue", results.get(0).getSymptom());
    }

    @Test
    void autoSuggest_prefixMatchesMultiple_returnsAll() {
        index.addEntry(sample("Brain fog"));
        index.addEntry(sample("Bloating"));
        index.addEntry(sample("Blood pressure"));
        List<SymptomEntry> results = index.autoSuggest("Bl");
        assertEquals(2, results.size());
    }

    @Test
    void autoSuggest_caseInsensitivePrefix_returnsEntry() {
        index.addEntry(sample("Fatigue"));
        assertFalse(index.autoSuggest("fa").isEmpty());
        assertFalse(index.autoSuggest("FA").isEmpty());
        assertFalse(index.autoSuggest("fA").isEmpty());
    }

    @Test
    void autoSuggest_noMatch_returnsEmptyList() {
        index.addEntry(sample("Fatigue"));
        assertTrue(index.autoSuggest("xyz").isEmpty());
    }

    @Test
    void autoSuggest_emptyPrefix_returnsEmptyList() {
        index.addEntry(sample("Fatigue"));
        assertTrue(index.autoSuggest("").isEmpty());
    }

    @Test
    void autoSuggest_blankPrefix_returnsEmptyList() {
        index.addEntry(sample("Fatigue"));
        assertTrue(index.autoSuggest("   ").isEmpty());
    }

    @Test
    void autoSuggest_nullPrefix_returnsEmptyList() {
        index.addEntry(sample("Fatigue"));
        assertTrue(index.autoSuggest(null).isEmpty());
    }

    @Test
    void autoSuggest_fullSymbolName_returnsSingleEntry() {
        index.addEntry(sample("Fatigue"));
        index.addEntry(sample("Bloating"));
        List<SymptomEntry> results = index.autoSuggest("Fatigue");
        assertEquals(1, results.size());
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private static SymptomEntry sample(String symptom) {
        return new SymptomEntry(
                symptom,
                List.of("Food A"),
                List.of("Food B"),
                List.of("Nutrient A"),
                List.of("Nutrient B")
        );
    }
}
