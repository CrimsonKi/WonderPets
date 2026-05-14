package wonderpets.controller;

import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

import java.util.List;

/**
 * Controller for symptom search and auto-suggest operations.
 * Tracks the most recent query and whether a search has been performed,
 * so views can distinguish the initial "no query yet" state from a
 * "query returned no results" state.
 */
public class SearchController extends AbstractController {

    private final SymptomIndex index;

    private String  lastQuery       = "";
    private boolean searchPerformed = false;

    /**
     * Constructs a {@code SearchController}.
     *
     * @param index the symptom index to search; must not be {@code null}
     * @param view  the view this controller drives; must not be {@code null}
     */
    public SearchController(SymptomIndex index, Object view) {
        super(index, view);
        this.index = index;
    }

    /**
     * Performs an exact-match search against the index.
     * The query is trimmed and lowercased before being stored and dispatched.
     *
     * @param query the symptom name entered by the user
     * @return the matching {@link SymptomEntry}, or {@code null} if not found
     */
    public SymptomEntry handleSearch(String query) {
        lastQuery       = trim(query).toLowerCase();
        searchPerformed = true;
        return index.search(lastQuery);
    }

    /**
     * Returns prefix-matched suggestions for the given input.
     * The input is trimmed before being dispatched; blank input returns an empty list.
     *
     * @param input the partial symptom name typed by the user
     * @return unmodifiable list of matching entries
     */
    public List<SymptomEntry> handleSuggest(String input) {
        return index.autoSuggest(trim(input));
    }

    /**
     * Returns {@code true} if {@link #handleSearch} has been called at least once.
     *
     * @return whether a search has been performed
     */
    public boolean wasSearchPerformed() {
        return searchPerformed;
    }

    /**
     * Returns the last query string passed to {@link #handleSearch},
     * normalised (trimmed and lowercased). Returns an empty string before
     * any search has been performed.
     *
     * @return the last query, or {@code ""}
     */
    public String getLastQuery() {
        return lastQuery;
    }

    /**
     * Dispatches a string action from the view.
     * Delegates to {@link #handleSearch(String)} for the {@code "search"} action;
     * all other actions are ignored.
     *
     * @param action the action identifier sent by the view
     */
    @Override
    public void operation(String action) {
        if ("search".equalsIgnoreCase(trim(action))) {
            handleSearch(lastQuery);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
