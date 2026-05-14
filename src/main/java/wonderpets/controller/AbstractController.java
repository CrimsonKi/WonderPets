package wonderpets.controller;

import wonderpets.model.AbstractModel;

/**
 * Base class for all controllers in the push-MVC pattern.
 * Holds references to the model and view, and defines a uniform
 * string-based dispatch point for view-initiated actions.
 */
public abstract class AbstractController {

    /** The model this controller coordinates. */
    protected final AbstractModel model;

    /** The view this controller drives. */
    protected final Object view;

    /**
     * Constructs an {@code AbstractController}.
     *
     * @param model the model to coordinate; must not be {@code null}
     * @param view  the view to drive; must not be {@code null}
     */
    public AbstractController(AbstractModel model, Object view) {
        if (model == null) throw new IllegalArgumentException("model must not be null");
        if (view  == null) throw new IllegalArgumentException("view must not be null");
        this.model = model;
        this.view  = view;
    }

    /**
     * Dispatches a string action from the view to this controller.
     * Subclasses interpret {@code action} and update the model or view accordingly.
     *
     * @param action the action identifier sent by the view
     */
    public abstract void operation(String action);
}
