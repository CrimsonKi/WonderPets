package wonderpets.controller;

import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

/**
 * Controller for privileged admin operations: authentication, and adding or
 * removing symptom entries. All mutating methods are auth-gated and throw
 * {@link IllegalStateException} when called without an active session.
 */
public class AdminController extends AbstractController {

    /** Password required to open an admin session. */
    public static final String ADMIN_PASS = "admin123";

    private final SymptomIndex index;
    private boolean loggedIn = false;

    /**
     * Constructs an {@code AdminController}.
     *
     * @param index the symptom index to administer; must not be {@code null}
     * @param view  the view this controller drives; must not be {@code null}
     */
    public AdminController(SymptomIndex index, Object view) {
        super(index, view);
        this.index = index;
    }

    /**
     * Attempts to start an admin session with the given password.
     *
     * @param password the password to check
     * @return {@code true} if authentication succeeded, {@code false} otherwise
     */
    public boolean authenticate(String password) {
        loggedIn = ADMIN_PASS.equals(password);
        return loggedIn;
    }

    /**
     * Ends the current admin session.
     */
    public void logout() {
        loggedIn = false;
    }

    /**
     * Returns {@code true} if an admin session is currently active.
     *
     * @return whether the admin is logged in
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Adds a symptom entry to the index.
     * Requires an active admin session. Rejects the entry if a symptom with
     * the same name already exists (case-insensitive).
     *
     * @param entry the entry to add; must not be {@code null}
     * @throws IllegalStateException    if no admin session is active
     * @throws IllegalArgumentException if the symptom already exists in the index
     */
    public void addSymptom(SymptomEntry entry) {
        requireLoggedIn();
        if (entry == null) throw new IllegalArgumentException("entry must not be null");
        if (index.search(entry.getSymptom()) != null) {
            throw new IllegalArgumentException(
                    "Symptom already exists: " + entry.getSymptom());
        }
        index.addEntry(entry);
    }

    /**
     * Removes the symptom with the given name from the index.
     * Requires an active admin session.
     *
     * @param symptom the symptom name to remove
     * @return {@code true} if the entry was found and removed, {@code false} otherwise
     * @throws IllegalStateException if no admin session is active
     */
    public boolean deleteSymptom(String symptom) {
        requireLoggedIn();
        return index.removeEntry(symptom);
    }

    /**
     * Dispatches a string action from the view.
     * Recognises {@code "Logout"} (case-insensitive) and ends the session.
     *
     * @param action the action identifier sent by the view
     */
    @Override
    public void operation(String action) {
        if ("logout".equalsIgnoreCase(action == null ? "" : action.trim())) {
            logout();
        }
    }

    private void requireLoggedIn() {
        if (!loggedIn) {
            throw new IllegalStateException("Admin not authenticated");
        }
    }
}
