package wonderpets.model;

/**
 * Observer interface for the push-MVC pattern.
 * Implement this interface to receive change notifications from a model.
 */
public interface ModelListener {

    /**
     * Called by the model when its state has changed.
     *
     * @param e the event identifying the model that changed
     */
    void modelChanged(ModelEvent e);
}
