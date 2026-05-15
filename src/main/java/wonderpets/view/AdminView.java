package wonderpets.view;

import wonderpets.controller.AdminController;
import wonderpets.model.SymptomEntry;
import wonderpets.model.SymptomIndex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin interface for managing the symptom index.
 * Presents a login card and, on successful authentication, an admin card with
 * an entry form and a table of existing entries. Disposed on close.
 */
public class AdminView extends JFrameView {

    private static final String LOGIN_CARD = "LOGIN";
    private static final String ADMIN_CARD = "ADMIN";
    private static final int    DELETE_COL = 5;
    private static final String[] COLUMNS  = {
        "Symptom", "Eat More", "Eat Less", "Increase Nutrients", "Monitor Nutrients", "Delete"
    };

    private final AdminController adminCtrl;
    private final SymptomIndex    index;

    // CardLayout
    private final CardLayout cards     = new CardLayout();
    private final JPanel     cardPanel = new JPanel(cards);

    // Login
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel         loginError    = new JLabel(" ");

    // Form
    private final JTextField fieldSymptom  = new JTextField(24);
    private final JTextField fieldEatMore  = new JTextField(32);
    private final JTextField fieldEatLess  = new JTextField(32);
    private final JTextField fieldIncrease = new JTextField(32);
    private final JTextField fieldMonitor  = new JTextField(32);
    private final JLabel     formMessage   = new JLabel(" ");

    // Table
    private DefaultTableModel tableModel;
    private JTable            table;

    /**
     * Constructs an {@code AdminView}.
     *
     * @param index     the symptom index to administer
     * @param adminCtrl the admin controller
     */
    public AdminView(SymptomIndex index, AdminController adminCtrl) {
        super("Admin Panel", index);
        this.index     = index;
        this.adminCtrl = adminCtrl;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUi();
        pack();
        setMinimumSize(new Dimension(860, 620));
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        cardPanel.add(buildLoginPanel(), LOGIN_CARD);
        cardPanel.add(buildAdminPanel(), ADMIN_CARD);
        setContentPane(cardPanel);
    }

    // ── Login card ────────────────────────────────────────────────────────────

    private JPanel buildLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(28, 48, 28, 48)));

        JLabel heading = new JLabel("Admin Login");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 16f));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(passLabel.getFont().deriveFont(Font.BOLD, 13f));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.addActionListener(e -> onLogin());

        JButton loginBtn = new JButton("Log In");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> onLogin());

        loginError.setForeground(Color.RED);
        loginError.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(heading);
        inner.add(Box.createVerticalStrut(16));
        inner.add(passLabel);
        inner.add(Box.createVerticalStrut(4));
        inner.add(passwordField);
        inner.add(Box.createVerticalStrut(12));
        inner.add(loginBtn);
        inner.add(Box.createVerticalStrut(8));
        inner.add(loginError);

        outer.add(inner);
        return outer;
    }

    // ── Admin card ────────────────────────────────────────────────────────────

    private JPanel buildAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(buildTopBar(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(buildForm(),       BorderLayout.NORTH);
        content.add(buildTablePanel(), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        JLabel title = new JLabel("WonderPets Admin");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> onLogout());

        bar.add(title,     BorderLayout.WEST);
        bar.add(logoutBtn, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new BorderLayout(4, 4));
        wrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Add New Symptom"));

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints lc = labelGbc();
        GridBagConstraints fc = fieldGbc();

        addFormRow(grid, lc, fc, 0, "Symptom Name:",        fieldSymptom);
        addFormRow(grid, lc, fc, 1, "Eat More (CSV):",      fieldEatMore);
        addFormRow(grid, lc, fc, 2, "Eat Less (CSV):",      fieldEatLess);
        addFormRow(grid, lc, fc, 3, "Increase Nutrients (CSV):", fieldIncrease);
        addFormRow(grid, lc, fc, 4, "Monitor Nutrients (CSV):",  fieldMonitor);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> onAdd());

        formMessage.setFont(formMessage.getFont().deriveFont(11f));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        btnRow.add(addBtn);
        btnRow.add(Box.createHorizontalStrut(12));
        btnRow.add(formMessage);

        wrapper.add(grid,   BorderLayout.CENTER);
        wrapper.add(btnRow, BorderLayout.SOUTH);
        return wrapper;
    }

    private void addFormRow(JPanel grid, GridBagConstraints lc,
                            GridBagConstraints fc, int row,
                            String label, JTextField field) {
        lc.gridy = row;
        fc.gridy = row;
        grid.add(new JLabel(label), lc);
        grid.add(field, fc);
    }

    private GridBagConstraints labelGbc() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx  = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(3, 6, 3, 6);
        return c;
    }

    private GridBagConstraints fieldGbc() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx   = 1;
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets  = new Insets(3, 0, 3, 6);
        return c;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);

        // Render the Delete column as a clickable button
        table.getColumnModel().getColumn(DELETE_COL).setCellRenderer(
                (tbl, val, selected, focus, row, col) -> {
                    JButton btn = new JButton("Delete");
                    btn.setMargin(new Insets(2, 8, 2, 8));
                    return btn;
                });
        table.getColumnModel().getColumn(DELETE_COL).setMaxWidth(80);
        table.getColumnModel().getColumn(DELETE_COL).setMinWidth(80);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == DELETE_COL) {
                    onDelete(row);
                }
            }
        });

        return new JScrollPane(table);
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    private void onLogin() {
        String password = new String(passwordField.getPassword());
        passwordField.setText("");
        if (adminCtrl.authenticate(password)) {
            loginError.setText(" ");
            rebuildTable();
            cards.show(cardPanel, ADMIN_CARD);
        } else {
            loginError.setText("Incorrect password. Please try again.");
        }
    }

    private void onLogout() {
        adminCtrl.operation("Logout");
        cards.show(cardPanel, LOGIN_CARD);
    }

    private void onAdd() {
        try {
            SymptomEntry entry = new SymptomEntry(
                    fieldSymptom.getText().trim(),
                    parseCsv(fieldEatMore.getText()),
                    parseCsv(fieldEatLess.getText()),
                    parseCsv(fieldIncrease.getText()),
                    parseCsv(fieldMonitor.getText())
            );
            adminCtrl.addSymptom(entry);
            clearForm();
            showFormMessage("Symptom \"" + entry.getSymptom() + "\" added successfully.", false);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showFormMessage(ex.getMessage(), true);
        }
    }

    private void onDelete(int row) {
        String symptom = (String) tableModel.getValueAt(row, 0);
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete symptom \"" + symptom + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            adminCtrl.deleteSymptom(symptom);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private List<String> parseCsv(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private void clearForm() {
        fieldSymptom.setText("");
        fieldEatMore.setText("");
        fieldEatLess.setText("");
        fieldIncrease.setText("");
        fieldMonitor.setText("");
    }

    private void showFormMessage(String text, boolean error) {
        formMessage.setText(text);
        formMessage.setForeground(error ? Color.RED : new Color(0, 128, 0));
    }

    private void rebuildTable() {
        tableModel.setRowCount(0);
        for (SymptomEntry e : index.getAll()) {
            tableModel.addRow(new Object[]{
                e.getSymptom(),
                String.join(", ", e.getEatMore()),
                String.join(", ", e.getEatLess()),
                String.join(", ", e.getIncreaseNutrients()),
                String.join(", ", e.getMonitorNutrients()),
                "Delete"
            });
        }
    }

    /**
     * Rebuilds the entry table when the index changes, but only while logged in
     * (the login card has no table to update).
     */
    @Override
    protected void refresh() {
        if (adminCtrl.isLoggedIn()) {
            rebuildTable();
        }
    }
}
