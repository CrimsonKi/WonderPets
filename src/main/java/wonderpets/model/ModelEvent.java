package wonderpets.model;

/**
 * Represents a change notification fired by a model object.
 * Observers receive a {@code ModelEvent} to identify which model changed.
 */
public class ModelEvent {

    private final Object source;

    /**
     * Constructs a new {@code ModelEvent}.
     *
     * @param source the model object that changed; must not be {@code null}
     */
    public ModelEvent(Object source) {
        this.source = source;
    }

    /**
     * Returns the model object that changed.
     *
     * @return the source model
     */
    public Object getSource() {
        return source;
    }
}
