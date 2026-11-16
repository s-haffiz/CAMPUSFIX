import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// GUI code for the project
public class Frame extends JFrame {

    private final ServicesDAO dao = new ServicesDAO();

    // Input fields
    private final JTextField txtServiceName = new JTextField(15);
    private final JTextField txtCustomerName = new JTextField(15);
    private final JTextField txtCustomerEmail = new JTextField(15);
    private final JTextField txtCustomerPhone = new JTextField(15);
    private final JTextField txtDeviceName = new JTextField(15);
    private final JTextField txtDeviceBrand = new JTextField(15);
    private final JTextField txtDateReceived = new JTextField(15);
    private final JTextField txtCost = new JTextField(15);
    private final JTextArea txtIssue = new JTextArea(3, 15);
    private final JComboBox<String> cmbStatus = new JComboBox<>(new String[] {
        "Received", "Diagnosing", "In Progress", "Ready for Collection", "Collected"
    });

    // Table
    private final String[] columns = {
        "ID", "Service", "Customer", "Email", "Phone", "Device",
        "Brand", "Issue", "Date Received", "Status", "Cost"
    };
    private DefaultTableModel tableModel;
    private JTable table;

    // ID of the row currently selected (-1 means nothing selected)
    private int selectedId = -1;

    // Search and filter
private final JTextField txtSearch = new JTextField(15);
private final JComboBox<String> cmbFilterStatus = new JComboBox<>(new String[] {
    "All", "Received", "Diagnosing", "In Progress", "Ready for Collection", "Collected"
});
private List<Services> allServices = new ArrayList<>();
private final JLabel lblSummary = new JLabel();

    public Frame() {
        setTitle("CampusFix Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top: title + form
        JPanel top = new JPanel(new BorderLayout());
        top.add(buildTitle(), BorderLayout.NORTH);
        top.add(buildForm(), BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // Middle: table
        JPanel center = new JPanel(new BorderLayout());
        center.add(buildSearchPanel(), BorderLayout.NORTH);
        center.add(buildTable(), BorderLayout.CENTER);
        center.add(lblSummary, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
        lblSummary.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));



        // Bottom: buttons
        add(buildButtons(), BorderLayout.SOUTH);

        clearForm();
        loadTable();
    }

    // ---------- Building the screen ----------

    private JLabel buildTitle() {
        JLabel title = new JLabel("CampusFix Management", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return title;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        txtIssue.setLineWrap(true);
        txtIssue.setWrapStyleWord(true);

        addField(form, "Service Name *", txtServiceName, 0, 0);
        addField(form, "Customer Name *", txtCustomerName, 1, 0);
        addField(form, "Customer Email", txtCustomerEmail, 0, 1);
        addField(form, "Customer Phone *", txtCustomerPhone, 1, 1);
        addField(form, "Device Name *", txtDeviceName, 0, 2);
        addField(form, "Device Brand", txtDeviceBrand, 1, 2);
        addField(form, "Date (yyyy-MM-dd) *", txtDateReceived, 0, 3);
        addField(form, "Status", cmbStatus, 1, 3);
        addField(form, "Cost *", txtCost, 0, 4);
        addField(form, "Issue Description *", new JScrollPane(txtIssue), 1, 4);

        return form;
    }

    // Places a label + component in the grid (col 0 or 1, given row)
    private void addField(JPanel panel, String label, JComponent comp, int col, int row) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = col * 2;
        g.gridy = row;
        panel.add(new JLabel(label), g);

        g.gridx = col * 2 + 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        panel.add(comp, g);
    }

    private JScrollPane buildTable() {
        // Cells are read-only: edits happen through the form
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        int[] widths = {40, 140, 130, 170, 100, 80, 80, 200, 100, 130, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // When a row is clicked, show its details in the form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                fillFormFromRow(table.getSelectedRow());
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        scroll.setPreferredSize(new Dimension(1000, 300));
        return scroll;
    }

    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addService());
        btnUpdate.addActionListener(e -> updateService());
        btnDelete.addActionListener(e -> deleteService());
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        return panel;
    }

    private JPanel buildSearchPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

    panel.add(new JLabel("Search (customer, device or service):"));
    panel.add(txtSearch);
    panel.add(new JLabel("Status:"));
    panel.add(cmbFilterStatus);

    // Filter as the user types
    txtSearch.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) { applyFilter(); }
        @Override
        public void removeUpdate(DocumentEvent e) { applyFilter(); }
        @Override
        public void changedUpdate(DocumentEvent e) { applyFilter(); }
    });

    // Filter when a status is picked
    cmbFilterStatus.addActionListener(e -> applyFilter());

    return panel;
}

    // ---------- Button actions ----------

    private void addService() {
        Services s = readForm();
        if (s == null) {
            return; // validation failed, message already shown
        }
        try {
            dao.addService(s);
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Service added successfully.");
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void updateService() {
        if (selectedId == -1) {
            warn("Select a row in the table first.");
            return;
        }
        Services s = readForm();
        if (s == null) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Save changes to this service?",
                "Confirm Update", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) {
                        return;
                    }
        s.setServiceId(selectedId);
        try {
            dao.updateService(s);
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Service updated successfully.");
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void deleteService() {
        if (selectedId == -1) {
            warn("Select a row in the table first.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this service?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.deleteService(selectedId);
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Service deleted successfully.");
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // ---------- Helpers ----------

    // Get fresh data from the database, then show it
private void loadTable() {
    try {
        allServices = dao.getAllServices();
        updateSummary();
        applyFilter();
    } catch (SQLException ex) {
        showError(ex);
    }
}

// Show only the services that match the search text and status
private void applyFilter() {
    String keyword = txtSearch.getText().trim().toLowerCase();
    String status = (String) cmbFilterStatus.getSelectedItem();

    tableModel.setRowCount(0);
    for (Services s : allServices) {
        boolean matchesText = keyword.isEmpty()
            || s.getCustomerName().toLowerCase().contains(keyword)
            || s.getDeviceName().toLowerCase().contains(keyword)
            || s.getServiceName().toLowerCase().contains(keyword);
        boolean matchesStatus = status.equals("All") || s.getStatus().equals(status);

        if (matchesText && matchesStatus) {
            tableModel.addRow(new Object[] {
                s.getServiceId(), s.getServiceName(), s.getCustomerName(),
                s.getCustomerEmail(), s.getCustomerPhone(), s.getDeviceName(),
                s.getDeviceBrand(), s.getIssueDescription(), s.getDateReceived(),
                s.getStatus(), s.getCost()
            });
        }
    }
}

// Count the services by status and show the totals
private void updateSummary() {
    int received = 0, diagnosing = 0, inProgress = 0, ready = 0, collected = 0;

    for (Services s : allServices) {
        switch (s.getStatus()) {
            case "Received":             received++;   break;
            case "Diagnosing":           diagnosing++; break;
            case "In Progress":          inProgress++; break;
            case "Ready for Collection": ready++;      break;
            case "Collected":            collected++;  break;
        }
    }

    lblSummary.setText("Total: " + allServices.size()
        + "  |  Received: " + received
        + "  |  Diagnosing: " + diagnosing
        + "  |  In Progress: " + inProgress
        + "  |  Ready for Collection: " + ready
        + "  |  Collected: " + collected);
}

    // Copy the selected table row into the form fields
    private void fillFormFromRow(int row) {
        selectedId = Integer.parseInt(text(tableModel.getValueAt(row, 0)));
        txtServiceName.setText(text(tableModel.getValueAt(row, 1)));
        txtCustomerName.setText(text(tableModel.getValueAt(row, 2)));
        txtCustomerEmail.setText(text(tableModel.getValueAt(row, 3)));
        txtCustomerPhone.setText(text(tableModel.getValueAt(row, 4)));
        txtDeviceName.setText(text(tableModel.getValueAt(row, 5)));
        txtDeviceBrand.setText(text(tableModel.getValueAt(row, 6)));
        txtIssue.setText(text(tableModel.getValueAt(row, 7)));
        txtDateReceived.setText(text(tableModel.getValueAt(row, 8)));
        cmbStatus.setSelectedItem(text(tableModel.getValueAt(row, 9)));
        txtCost.setText(text(tableModel.getValueAt(row, 10)));
    }

    // Read the form into a Services object, or return null if something is invalid
    private Services readForm() {
        String serviceName = txtServiceName.getText().trim();
        String customerName = txtCustomerName.getText().trim();
        String email = txtCustomerEmail.getText().trim();
        String phone = txtCustomerPhone.getText().trim();
        String deviceName = txtDeviceName.getText().trim();
        String deviceBrand = txtDeviceBrand.getText().trim();
        String issue = txtIssue.getText().trim();
        String date = txtDateReceived.getText().trim();
        String costText = txtCost.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();

        // Required fields
        if (serviceName.isEmpty() || customerName.isEmpty() || phone.isEmpty()
                || deviceName.isEmpty() || issue.isEmpty() || date.isEmpty()
                || costText.isEmpty()) {
            warn("Please fill in all fields marked with *.");
            return null;
        }

        // Phone: digits, spaces and + only
        if (!phone.matches("[0-9+ ]{7,20}")) {
            warn("Enter a valid phone number (digits only, 7 to 20 characters).");
            return null;
        }

        // Email is optional, but if given it must look like an email
        if (!email.isEmpty() && !email.contains("@")) {
            warn("Enter a valid email address.");
            return null;
        }

        // Date must be yyyy-MM-dd
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            warn("Enter the date in the format yyyy-MM-dd, for example 2026-11-15.");
            return null;
        }

        // Cost must be a number that is not negative
        double cost;
        try {
            cost = Double.parseDouble(costText);
        } catch (NumberFormatException ex) {
            warn("Cost must be a number.");
            return null;
        }
        if (cost < 0) {
            warn("Cost cannot be negative.");
            return null;
        }

        return new Services(serviceName, customerName, email, phone, deviceName,
                            deviceBrand, issue, date, status, cost);
    }

    private void clearForm() {
        txtServiceName.setText("");
        txtCustomerName.setText("");
        txtCustomerEmail.setText("");
        txtCustomerPhone.setText("");
        txtDeviceName.setText("");
        txtDeviceBrand.setText("");
        txtIssue.setText("");
        txtDateReceived.setText(LocalDate.now().toString());
        txtCost.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
        selectedId = -1;
    }

    // Turns null (e.g. an empty email) into an empty string
    private String text(Object value) {
        return value == null ? "" : value.toString();
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }

}