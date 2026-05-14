package wonderpets.view;

import wonderpets.controller.SearchController;
import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main application window. Provides a search field with autocomplete, quick-tag
 * buttons for common symptoms, and access to the admin panel.
 */
public class SearchView extends JFrameView {

    private static final String[] QUICK_TAGS = {
        "Fatigue", "Bloating", "Brain fog", "Insomnia",
        "Anxiety", "Inflammation", "Joint pain", "Nausea"
    };

    private final SearchController searchCtrl;
    private final AdminView        adminView;
    private final SymptomIndex     index;

    // Search row
    private final JTextField searchField = new JTextField(28);
    private final JButton    searchBtn   = new JButton("Search");

    // Autocomplete popup
    private final DefaultListModel<String> suggestionModel = new DefaultListModel<>();
    private final JList<String>            suggestionList  = new JList<>(suggestionModel);
    private final JPopupMenu               popup           = new JPopupMenu();

    /**
     * Constructs the main search window.
     *
     * @param index      the symptom index to query
     * @param searchCtrl the search controller
     * @param adminView  the admin panel window (single shared instance)
     */
    public SearchView(SymptomIndex index, SearchController searchCtrl, AdminView adminView) {
        super("WonderPets — Dietary Symptom Guide", index);
        this.index      = index;
        this.searchCtrl = searchCtrl;
        this.adminView  = adminView;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUi();
        buildPopup();
        bindListeners();
        pack();
        setMinimumSize(new Dimension(640, 420));
        setLocationRelativeTo(null);
    }

    // ── UI construction ───────────────────────────────────────────────────────

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 10, 16));

        root.add(buildNorth(),  BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildSouth(),  BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildNorth() {
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("WonderPets", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Search a symptom to get dietary guidance", SwingConstants.CENTER);
        subtitle.setFont(subtitle.getFont().deriveFont(Font.ITALIC, 13f));
        subtitle.setForeground(Color.DARK_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        north.add(title);
        north.add(Box.createVerticalStrut(4));
        north.add(subtitle);
        north.add(Box.createVerticalStrut(14));
        north.add(buildSearchRow());
        north.add(Box.createVerticalStrut(10));
        north.add(buildTagRow());
        return north;
    }

    private JPanel buildSearchRow() {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        searchField.setFont(searchField.getFont().deriveFont(14f));
        searchField.putClientProperty("JTextField.placeholderText", "e.g. Fatigue");
        searchBtn.setFont(searchBtn.getFont().deriveFont(Font.BOLD, 13f));
        row.add(searchField, BorderLayout.CENTER);
        row.add(searchBtn,   BorderLayout.EAST);
        return row;
    }

    private JPanel buildTagRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        for (String tag : QUICK_TAGS) {
            JButton btn = new JButton(tag);
            btn.setFont(btn.getFont().deriveFont(11f));
            btn.setMargin(new Insets(3, 8, 3, 8));
            btn.addActionListener(e -> onQuickTag(tag));
            row.add(btn);
        }
        return row;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        JLabel hint = new JLabel("Type a symptom above or choose a quick tag");
        hint.setFont(hint.getFont().deriveFont(Font.ITALIC, 12f));
        hint.setForeground(new Color(160, 160, 160));
        center.add(hint);
        return center;
    }

    private JPanel buildSouth() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton adminBtn = new JButton("Admin Panel");
        adminBtn.setFont(adminBtn.getFont().deriveFont(11f));
        adminBtn.addActionListener(e -> {
            adminView.setVisible(true);
            adminView.toFront();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        right.add(adminBtn);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ── Autocomplete popup ────────────────────────────────────────────────────

    private void buildPopup() {
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setFixedCellHeight(24);
        suggestionList.setFont(searchField.getFont());
        suggestionList.setFocusable(true);

        JScrollPane scroll = new JScrollPane(suggestionList);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        popup.setBorderPainted(true);
        popup.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        popup.add(scroll);
    }

    // ── Listeners ─────────────────────────────────────────────────────────────

    private void bindListeners() {
        // Search button
        searchBtn.addActionListener(e -> commitSearch());

        // Keyboard handling on the text field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> {
                        hidePopup();
                        commitSearch();
                        e.consume();
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (popup.isVisible() && suggestionModel.size() > 0) {
                            suggestionList.requestFocusInWindow();
                            suggestionList.setSelectedIndex(0);
                        }
                        e.consume();
                    }
                    case KeyEvent.VK_ESCAPE -> {
                        hidePopup();
                        e.consume();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                if (code != KeyEvent.VK_ENTER && code != KeyEvent.VK_ESCAPE
                        && code != KeyEvent.VK_DOWN && code != KeyEvent.VK_UP) {
                    updateSuggestions();
                }
            }
        });

        // Keyboard handling inside the suggestion list
        suggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER -> selectSuggestion();
                    case KeyEvent.VK_ESCAPE -> {
                        hidePopup();
                        searchField.requestFocusInWindow();
                    }
                }
            }
        });

        // Mouse click on a suggestion
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectSuggestion();
            }
        });
    }

    // ── Autocomplete logic ────────────────────────────────────────────────────

    private void updateSuggestions() {
        List<SymptomEntry> suggestions = searchCtrl.handleSuggest(searchField.getText());
        suggestionModel.clear();

        if (suggestions.isEmpty()) {
            hidePopup();
            return;
        }

        suggestions.forEach(s -> suggestionModel.addElement(s.getSymptom()));

        int rowHeight = suggestionList.getFixedCellHeight();
        int height    = Math.min(suggestions.size() * rowHeight + 4, 160);
        popup.setPreferredSize(new Dimension(searchField.getWidth(), height));

        if (!popup.isVisible()) {
            popup.show(searchField, 0, searchField.getHeight());
        }
        // Return focus to field so typing continues
        searchField.requestFocusInWindow();
    }

    private void selectSuggestion() {
        String selected = suggestionList.getSelectedValue();
        if (selected != null) {
            searchField.setText(selected);
            hidePopup();
            searchField.requestFocusInWindow();
            commitSearch();
        }
    }

    private void hidePopup() {
        popup.setVisible(false);
    }

    // ── Search and navigation ─────────────────────────────────────────────────

    private void commitSearch() {
        doSearch(searchField.getText());
    }

    private void onQuickTag(String symptom) {
        searchField.setText(symptom);
        hidePopup();
        doSearch(symptom);
    }

    private void doSearch(String query) {
        SymptomEntry result = searchCtrl.handleSearch(query);
        if (result == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No results found for \"" + query.trim() + "\".",
                    "Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            ResultView rv = new ResultView(index, result);
            rv.setVisible(true);
        }
    }

    /**
     * No-op: the search view has no dynamic content that depends on the index state.
     */
    @Override
    protected void refresh() {}
}
