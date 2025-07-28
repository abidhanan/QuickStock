package com.quickstock.views;

import com.quickstock.db.DBConnection;
import java.awt.*;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.RowFilter;

public class AdminDashboard extends BaseDashboard {
    private JTable productsTable;
    private JTable usersTable;
    private JTable suppliersTable;
    
    private TableRowSorter<DefaultTableModel> productsSorter;
    private TableRowSorter<DefaultTableModel> usersSorter;
    private TableRowSorter<DefaultTableModel> suppliersSorter;

    public AdminDashboard(int userId, String fullName) {
        super(userId, fullName);
        setTitle("QuickStock - Admin Dashboard (" + fullName + ")");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initCommonComponents();
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Products Management Tab
        JPanel productsPanel = new JPanel(new BorderLayout());
        productsTable = new JTable();
        initProductsTable();
        productsPanel.add(new JScrollPane(productsTable), BorderLayout.CENTER);
        productsPanel.add(createProductsToolbar(), BorderLayout.NORTH);
        
        // User Management Tab
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersTable = new JTable();
        initUsersTable();
        usersPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        usersPanel.add(createUsersToolbar(), BorderLayout.NORTH);
        usersPanel.add(createUserButtonsPanel(), BorderLayout.SOUTH);
        
        // Supplier Management Tab
        JPanel suppliersPanel = new JPanel(new BorderLayout());
        suppliersTable = new JTable();
        initSuppliersTable();
        suppliersPanel.add(new JScrollPane(suppliersTable), BorderLayout.CENTER);
        suppliersPanel.add(createSuppliersToolbar(), BorderLayout.NORTH);
        suppliersPanel.add(createSupplierButtonsPanel(), BorderLayout.SOUTH);
        
        tabbedPane.addTab("Products Management", productsPanel);
        tabbedPane.addTab("User Management", usersPanel);
        tabbedPane.addTab("Supplier Management", suppliersPanel);
        
        getContentPane().add(tabbedPane);
    }

    // Products Components
    private JPanel createProductsToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");
        JButton reportBtn = new JButton("Low Stock Report");
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterProducts(searchField.getText()); }
            public void removeUpdate(DocumentEvent e) { filterProducts(searchField.getText()); }
            public void changedUpdate(DocumentEvent e) { filterProducts(searchField.getText()); }
        });
        
        searchBtn.addActionListener(e -> filterProducts(searchField.getText()));
        clearBtn.addActionListener(e -> searchField.setText(""));
        refreshBtn.addActionListener(e -> refreshProducts());
        reportBtn.addActionListener(e -> generateLowStockReport());
        
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(clearBtn);
        toolbar.add(refreshBtn);
        toolbar.add(reportBtn);
        
        return toolbar;
    }

    private void initProductsTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Product Name", "Category", "Buy Price", "Sell Price", "Stock", "Min Stock"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        loadProductsData(model);
        productsTable.setModel(model);
        
        productsSorter = new TableRowSorter<>(model);
        productsTable.setRowSorter(productsSorter);
    }

    private void loadProductsData(DefaultTableModel model) {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM products");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getBigDecimal("buying_price"),
                    rs.getBigDecimal("selling_price"),
                    rs.getInt("stock_quantity"),
                    rs.getInt("min_stock_level")
                });
            }
        } catch (SQLException e) {
            showError("Error loading products: " + e.getMessage());
        }
    }

    // Users Components
    private JPanel createUsersToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterUsers(searchField.getText()); }
            public void removeUpdate(DocumentEvent e) { filterUsers(searchField.getText()); }
            public void changedUpdate(DocumentEvent e) { filterUsers(searchField.getText()); }
        });
        
        searchBtn.addActionListener(e -> filterUsers(searchField.getText()));
        clearBtn.addActionListener(e -> searchField.setText(""));
        refreshBtn.addActionListener(e -> refreshUsers());
        
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(clearBtn);
        toolbar.add(refreshBtn);
        
        return toolbar;
    }

    private JPanel createUserButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createAddUserButton());
        buttonPanel.add(createEditUserButton());
        buttonPanel.add(createDeleteUserButton());
        return buttonPanel;
    }

    private void initUsersTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Username", "Full Name", "Role", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        loadUsersData(model);
        usersTable.setModel(model);
        
        usersSorter = new TableRowSorter<>(model);
        usersTable.setRowSorter(usersSorter);
    }

    private void loadUsersData(DefaultTableModel model) {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getBoolean("is_active") ? "Active" : "Inactive"
                });
            }
        } catch (SQLException e) {
            showError("Error loading users: " + e.getMessage());
        }
    }

    // Supplier Components
    private JPanel createSuppliersToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterSuppliers(searchField.getText()); }
            public void removeUpdate(DocumentEvent e) { filterSuppliers(searchField.getText()); }
            public void changedUpdate(DocumentEvent e) { filterSuppliers(searchField.getText()); }
        });
        
        searchBtn.addActionListener(e -> filterSuppliers(searchField.getText()));
        clearBtn.addActionListener(e -> searchField.setText(""));
        refreshBtn.addActionListener(e -> refreshSuppliers());
        
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(clearBtn);
        toolbar.add(refreshBtn);
        
        return toolbar;
    }

    private JPanel createSupplierButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createAddSupplierButton());
        buttonPanel.add(createEditSupplierButton());
        buttonPanel.add(createDeleteSupplierButton());
        return buttonPanel;
    }

    private void initSuppliersTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Supplier Name", "Contact Person", "Phone", "Address"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        loadSuppliersData(model);
        suppliersTable.setModel(model);
        
        suppliersSorter = new TableRowSorter<>(model);
        suppliersTable.setRowSorter(suppliersSorter);
    }

    private void loadSuppliersData(DefaultTableModel model) {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM suppliers");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("contact_person"),
                    rs.getString("phone"),
                    rs.getString("address")
                });
            }
        } catch (SQLException e) {
            showError("Error loading suppliers: " + e.getMessage());
        }
    }

    // Filter Methods
    private void filterProducts(String query) {
        if (query.trim().isEmpty()) {
            productsSorter.setRowFilter(null);
        } else {
            try {
                productsSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            } catch (Exception e) {
                showError("Invalid search pattern: " + e.getMessage());
            }
        }
    }

    private void filterUsers(String query) {
        if (query.trim().isEmpty()) {
            usersSorter.setRowFilter(null);
        } else {
            try {
                usersSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            } catch (Exception e) {
                showError("Invalid search pattern: " + e.getMessage());
            }
        }
    }

    private void filterSuppliers(String query) {
        if (query.trim().isEmpty()) {
            suppliersSorter.setRowFilter(null);
        } else {
            try {
                suppliersSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            } catch (Exception e) {
                showError("Invalid search pattern: " + e.getMessage());
            }
        }
    }

    // CRUD Methods for Users
    private JButton createAddUserButton() {
        JButton button = new JButton("Add User");
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
            JTextField txtUserId = new JTextField();
            JTextField txtUsername = new JTextField();
            JTextField txtFullName = new JTextField();
            JComboBox<String> cmbRole = new JComboBox<>(new String[]{"owner", "admin", "cashier", "warehouse"});
            JPasswordField txtPassword = new JPasswordField();
            JCheckBox chkActive = new JCheckBox("Active", true);

            panel.add(new JLabel("User ID:"));
            panel.add(txtUserId);
            panel.add(new JLabel("Username:")); 
            panel.add(txtUsername);
            panel.add(new JLabel("Full Name:"));
            panel.add(txtFullName);
            panel.add(new JLabel("Role:"));
            panel.add(cmbRole);
            panel.add(new JLabel("Password:"));
            panel.add(txtPassword);
            panel.add(new JLabel("Status:"));
            panel.add(chkActive);

            int result = JOptionPane.showConfirmDialog(this, panel, "Add New User", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "INSERT INTO users (user_id, username, full_name, role, password, is_active) " +
                               "VALUES (?, ?, ?, ?, SHA2(?, 256), ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(txtUserId.getText()));
                    stmt.setString(2, txtUsername.getText());
                    stmt.setString(3, txtFullName.getText());
                    stmt.setString(4, cmbRole.getSelectedItem().toString());
                    stmt.setString(5, new String(txtPassword.getPassword()));
                    stmt.setBoolean(6, chkActive.isSelected());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User added successfully!");
                    refreshUsers();
                } catch (SQLException ex) {
                    showError("Error adding user: " + ex.getMessage());
                }
            }
        });
        return button;
    }

    private JButton createEditUserButton() {
        JButton button = new JButton("Edit User");
        button.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("Please select a user to edit");
                return;
            }

            int userId = (int) usersTable.getValueAt(selectedRow, 0);
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT * FROM users WHERE user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
                    JTextField txtUserId = new JTextField(String.valueOf(rs.getInt("user_id")));
                    JTextField txtUsername = new JTextField(rs.getString("username"));
                    JTextField txtFullName = new JTextField(rs.getString("full_name"));
                    JComboBox<String> cmbRole = new JComboBox<>(new String[]{"owner", "admin", "cashier", "warehouse"});
                    cmbRole.setSelectedItem(rs.getString("role"));
                    JPasswordField txtPassword = new JPasswordField();
                    JCheckBox chkActive = new JCheckBox("Active", rs.getBoolean("is_active"));

                    panel.add(new JLabel("User ID:"));
                    panel.add(txtUserId);
                    panel.add(new JLabel("Username:"));
                    panel.add(txtUsername);
                    panel.add(new JLabel("Full Name:")); 
                    panel.add(txtFullName);
                    panel.add(new JLabel("Role:"));
                    panel.add(cmbRole);
                    panel.add(new JLabel("Password (leave blank to keep current):"));
                    panel.add(txtPassword);
                    panel.add(new JLabel("Status:"));
                    panel.add(chkActive);

                    int result = JOptionPane.showConfirmDialog(this, panel, "Edit User", 
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String updateSql = "UPDATE users SET user_id=?, username=?, full_name=?, role=?, is_active=?" +
                                         (txtPassword.getPassword().length > 0 ? ", password=SHA2(?, 256)" : "") + 
                                         " WHERE user_id=?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setInt(1, Integer.parseInt(txtUserId.getText()));
                        updateStmt.setString(2, txtUsername.getText());
                        updateStmt.setString(3, txtFullName.getText());
                        updateStmt.setString(4, cmbRole.getSelectedItem().toString());
                        updateStmt.setBoolean(5, chkActive.isSelected());
                        
                        int paramIndex = 6;
                        if (txtPassword.getPassword().length > 0) {
                            updateStmt.setString(paramIndex++, new String(txtPassword.getPassword()));
                        }
                        
                        updateStmt.setInt(paramIndex, userId);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "User updated successfully!");
                        refreshUsers();
                    }
                }
            } catch (SQLException ex) {
                showError("Error editing user: " + ex.getMessage());
            }
        });
        return button;
    }

    private JButton createDeleteUserButton() {
        JButton button = new JButton("Delete User"); 
        button.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("Please select a user to delete");
                return;
            }

            int userId = (int) usersTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this user?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM users WHERE user_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    refreshUsers();
                } catch (SQLException ex) {
                    showError("Error deleting user: " + ex.getMessage());
                }
            }
        });
        return button;
    }

    // CRUD Methods for Suppliers 
    private JButton createAddSupplierButton() {
        JButton button = new JButton("Add Supplier");
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            JTextField txtSupplierId = new JTextField();
            JTextField txtName = new JTextField();
            JTextField txtContact = new JTextField();
            JTextField txtPhone = new JTextField();
            JTextField txtAddress = new JTextField();

            panel.add(new JLabel("Supplier ID:"));
            panel.add(txtSupplierId);
            panel.add(new JLabel("Supplier Name:"));
            panel.add(txtName);
            panel.add(new JLabel("Contact Person:"));
            panel.add(txtContact);
            panel.add(new JLabel("Phone:"));
            panel.add(txtPhone);
            panel.add(new JLabel("Address:"));
            panel.add(txtAddress);

            int result = JOptionPane.showConfirmDialog(this, panel, "Add New Supplier", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "INSERT INTO suppliers (supplier_id, supplier_name, contact_person, phone, address) " +
                               "VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(txtSupplierId.getText()));
                    stmt.setString(2, txtName.getText());
                    stmt.setString(3, txtContact.getText());
                    stmt.setString(4, txtPhone.getText());
                    stmt.setString(5, txtAddress.getText());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Supplier added successfully!");
                    refreshSuppliers();
                } catch (SQLException ex) {
                    showError("Error adding supplier: " + ex.getMessage());
                }
            }
        });
        return button;
    }

    private JButton createEditSupplierButton() {
        JButton button = new JButton("Edit Supplier");
        button.addActionListener(e -> {
            int selectedRow = suppliersTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("Please select a supplier to edit");
                return;
            }

            int supplierId = (int) suppliersTable.getValueAt(selectedRow, 0);
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, supplierId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
                    JTextField txtSupplierId = new JTextField(String.valueOf(rs.getInt("supplier_id")));
                    JTextField txtName = new JTextField(rs.getString("supplier_name"));
                    JTextField txtContact = new JTextField(rs.getString("contact_person"));
                    JTextField txtPhone = new JTextField(rs.getString("phone"));
                    JTextField txtAddress = new JTextField(rs.getString("address"));

                    panel.add(new JLabel("Supplier ID:"));
                    panel.add(txtSupplierId);
                    panel.add(new JLabel("Supplier Name:"));
                    panel.add(txtName);
                    panel.add(new JLabel("Contact Person:"));
                    panel.add(txtContact);
                    panel.add(new JLabel("Phone:"));
                    panel.add(txtPhone);
                    panel.add(new JLabel("Address:"));
                    panel.add(txtAddress);

                    int result = JOptionPane.showConfirmDialog(this, panel, "Edit Supplier",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String updateSql = "UPDATE suppliers SET supplier_id=?, supplier_name=?, contact_person=?, phone=?, address=? " +
                                         "WHERE supplier_id=?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setInt(1, Integer.parseInt(txtSupplierId.getText()));
                        updateStmt.setString(2, txtName.getText());
                        updateStmt.setString(3, txtContact.getText());
                        updateStmt.setString(4, txtPhone.getText());
                        updateStmt.setString(5, txtAddress.getText());
                        updateStmt.setInt(6, supplierId);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
                        refreshSuppliers();
                    }
                }
            } catch (SQLException ex) {
                showError("Error editing supplier: " + ex.getMessage());
            }
        });
        return button;
    }

    private JButton createDeleteSupplierButton() {
        JButton button = new JButton("Delete Supplier");
        button.addActionListener(e -> {
            int selectedRow = suppliersTable.getSelectedRow();
            if (selectedRow == -1) {
                showError("Please select a supplier to delete");
                return;
            }

            int supplierId = (int) suppliersTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this supplier?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, supplierId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Supplier deleted successfully!");
                    refreshSuppliers();
                } catch (SQLException ex) {
                    showError("Error deleting supplier: " + ex.getMessage());
                }
            }
        });
        return button;
    }

    // Report Methods
    private void generateLowStockReport() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT product_name, category, stock_quantity, min_stock_level " +
                         "FROM products WHERE stock_quantity < min_stock_level " +
                         "ORDER BY (min_stock_level - stock_quantity) DESC";
            ResultSet rs = conn.createStatement().executeQuery(query);
            
            StringBuilder report = new StringBuilder();
            report.append("LOW STOCK ALERT REPORT\n");
            report.append("Generated on: ").append(new Date()).append("\n\n");
            report.append("Products that need restocking:\n\n");
            report.append(String.format("%-30s %-20s %-10s %-10s %-10s\n", 
                "Product", "Category", "Stock", "Min", "Need"));
            
            while(rs.next()) {
                int need = rs.getInt("min_stock_level") - rs.getInt("stock_quantity");
                report.append(String.format("%-30s %-20s %-10d %-10d %-10d\n",
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getInt("stock_quantity"),
                    rs.getInt("min_stock_level"),
                    need));
            }
            
            displayReport(report.toString());
        } catch (SQLException e) {
            showError("Error generating low stock report: " + e.getMessage());
        }
    }

    private void displayReport(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Low Stock Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // Refresh Methods
    private void refreshProducts() {
        DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
        model.setRowCount(0);
        loadProductsData(model);
    }

    private void refreshUsers() {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setRowCount(0);
        loadUsersData(model);
    }

    private void refreshSuppliers() {
        DefaultTableModel model = (DefaultTableModel) suppliersTable.getModel();
        model.setRowCount(0);
        loadSuppliersData(model);
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
