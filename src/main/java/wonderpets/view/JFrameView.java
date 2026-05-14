package wonderpets.view;

import wonderpets.controller.AbstractController;
import wonderpets.model.AbstractModel;
import wonderpets.model.ModelEvent;
import wonderpets.model.ModelListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Base class for all top-level Swing views in the push-MVC pattern.
 * Registers itself as a {@link ModelListener} on construction and
 * ensures {@link #refresh()} is always called on the Event Dispatch Thread.
 */
public abstract class JFrameView extends JFrame implements ModelListener {

    /** The model this view observes. */
    protected final AbstractModel model;

    /** The controller that handles actions from this view. */
    protected AbstractController controller;

    /**
     * Constructs a {@code JFrameView}, registers it as a listener on {@code model},
     * and sets the default close operation to {@link JFrame#EXIT_ON_CLOSE}.
     *
     * @param title the window title
     * @param model the model to observe; must not be {@code null}
     */
    public JFrameView(String title, AbstractModel model) {
        super(title);
        if (model == null) throw new IllegalArgumentException("model must not be null");
        this.model = model;
        model.addModelListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets the controller that this view delegates actions to.
     *
     * @param controller the controller to use
     */
    public void setController(AbstractController controller) {
        this.controller = controller;
    }

    /**
     * Called by the model when its state changes.
     * Schedules {@link #refresh()} on the EDT if not already on it,
     * or calls it directly if already on the EDT.
     *
     * @param e the event identifying the model that changed
     */
    @Override
    public void modelChanged(ModelEvent e) {
        if (SwingUtilities.isEventDispatchThread()) {
            refresh();
        } else {
            SwingUtilities.invokeLater(this::refresh);
        }
    }

    /**
     * Refreshes this view to reflect the current model state.
     * Always called on the Event Dispatch Thread.
     */
    protected abstract void refresh();
}
