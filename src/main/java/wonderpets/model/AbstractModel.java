package wonderpets.model;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base class for all models in the push-MVC pattern.
 * Manages listener registration and change notification.
 * {@link CopyOnWriteArrayList} makes listener iteration safe under concurrent add/remove.
 */
public abstract class AbstractModel {

    private final CopyOnWriteArrayList<ModelListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Registers a listener to be notified when this model changes.
     *
     * @param listener the listener to add; ignored if already registered
     */
    public void addModelListener(ModelListener listener) {
        listeners.addIfAbsent(listener);
    }

    /**
     * Removes a previously registered listener.
     *
     * @param listener the listener to remove; ignored if not registered
     */
    public void removeModelListener(ModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that this model has changed.
     * Subclasses call this after mutating state.
     */
    protected void notifyChanged() {
        ModelEvent event = new ModelEvent(this);
        for (ModelListener listener : listeners) {
            listener.modelChanged(event);
        }
    }
}
