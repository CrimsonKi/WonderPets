package wonderpets.view;

import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Displays dietary guidance for a single {@link SymptomEntry} as four colour-coded
 * cards: Eat More, Eat Less, Increase Nutrients, and Monitor.
 * Disposed rather than exiting the application on close.
 */
public class ResultView extends JFrameView {

    private static final Color GREEN  = new Color(198, 239, 206);
    private static final Color RED    = new Color(255, 199, 206);
    private static final Color BLUE   = new Color(189, 215, 238);
    private static final Color YELLOW = new Color(255, 235, 156);

    private static final String DISCLAIMER =
            "For educational purposes only. Always consult a qualified veterinarian before changing your pet's diet.";

    private final SymptomEntry entry;

    /**
     * Constructs a {@code ResultView} for the given symptom entry.
     *
     * @param index the symptom index (used as the observed model)
     * @param entry the symptom entry whose guidance is displayed
     */
    public ResultView(SymptomIndex index, SymptomEntry entry) {
        super("Results: " + entry.getSymptom(), index);
        this.entry = entry;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUi();
        pack();
        setMinimumSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        root.add(buildTitle(), BorderLayout.NORTH);
        root.add(buildGrid(),  BorderLayout.CENTER);
        root.add(buildDisclaimer(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("Symptom: " + entry.getSymptom(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return title;
    }

    private JPanel buildGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));
        grid.add(buildCard("Eat More",           entry.getEatMore(),           GREEN));
        grid.add(buildCard("Eat Less",            entry.getEatLess(),           RED));
        grid.add(buildCard("Increase Nutrients",  entry.getIncreaseNutrients(), BLUE));
        grid.add(buildCard("Monitor",             entry.getMonitorNutrients(),  YELLOW));
        return grid;
    }

    private JPanel buildCard(String title, List<String> items, Color background) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(background);
        card.setOpaque(true);

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(background.darker(), 1), title);
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD, 13f));
        card.setBorder(border);

        DefaultListModel<String> model = new DefaultListModel<>();
        if (items.isEmpty()) {
            model.addElement("—"); // em dash
        } else {
            items.forEach(model::addElement);
        }

        JList<String> list = new JList<>(model);
        list.setBackground(background);
        list.setOpaque(true);
        list.setFocusable(false);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JLabel buildDisclaimer() {
        JLabel label = new JLabel(DISCLAIMER, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 11f));
        label.setForeground(Color.DARK_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        return label;
    }

    /**
     * No-op: the entry is immutable and the display does not change after construction.
     */
    @Override
    protected void refresh() {}
}
