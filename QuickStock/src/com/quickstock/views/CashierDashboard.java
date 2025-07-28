package com.quickstock.views;

import com.quickstock.db.DBConnection;
import com.quickstock.util.ReceiptPrinter;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class CashierDashboard extends BaseDashboard {
    private DefaultTableModel cartModel;
    private JTable cartTable;
    private JTextField txtTotal;
    private JTextField productSearchField;
    private JTextField transactionSearchField;
    private TableRowSorter<TableModel> productSorter;
    private TableRowSorter<TableModel> transactionSorter;

    public CashierDashboard(int userId, String fullName) {
        super(userId, fullName);
        setTitle("QuickStock - Cashier Dashboard (" + fullName + ")");
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab New Transaction
        JPanel transactionPanel = createTransactionPanel();
        tabbedPane.addTab("New Transaction", transactionPanel);

        // Tab Transaction History
        JPanel historyPanel = createHistoryPanel();
        tabbedPane.addTab("Transaction History", historyPanel);

        getContentPane().add(tabbedPane);
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Product Table
        DefaultTableModel productsModel = new DefaultTableModel(
            new Object[] {"ID", "Product Name", "Price", "Stock"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 -> Integer.class;
                    case 2 -> Double.class;
                    case 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };
        
        JTable productsTable = new JTable(productsModel);
        productSorter = new TableRowSorter<>(productsModel);
        productsTable.setRowSorter(productSorter);
        setupTable(productsTable, new int[]{100, 300, 150, 100});

        // Search Components
        JPanel searchPanel = new JPanel();
        productSearchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");

        setupSearchFunctionality(productSearchField, productSorter);
        searchBtn.addActionListener(e -> search());
        clearBtn.addActionListener(e -> {
            productSearchField.setText("");
            productSorter.setRowFilter(null);
        });

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(productSearchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);

        JScrollPane scrollPane = new JScrollPane(productsTable);
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.add(searchPanel, BorderLayout.NORTH);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        // Shopping Cart
        JPanel cartPanel = createCartPanel();
        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    addToCart(productsTable);
                }
            }
        });

        panel.add(productPanel, BorderLayout.CENTER);
        panel.add(cartPanel, BorderLayout.EAST);

        loadProducts(productsModel);
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Shopping Cart"));

        cartModel = new DefaultTableModel(
            new Object[] {"ID", "Product Name", "Price", "Qty", "Subtotal"}, 0) {
            @Override public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 -> Integer.class;
                    case 2, 4 -> Double.class;
                    case 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };

        cartTable = new JTable(cartModel);
        setupTable(cartTable, new int[]{100, 300, 150, 100, 150});

        JButton processBtn = new JButton("Process Transaction");
        JButton clearBtn = new JButton("Clear Cart");
        txtTotal = new JTextField("0.00", 10);
        txtTotal.setEditable(false);

        processBtn.addActionListener(e -> processTransaction());
        clearBtn.addActionListener(e -> clearCart());

        JPanel summaryPanel = new JPanel();
        summaryPanel.add(new JLabel("Total:"));
        summaryPanel.add(txtTotal);
        summaryPanel.add(processBtn);
        summaryPanel.add(clearBtn);

        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel historyModel = new DefaultTableModel(
            new Object[] {"Transaction ID", "Date", "Total Amount", "Payment Method"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 -> Integer.class;
                    case 2 -> Double.class;
                    default -> String.class;
                };
            }
        };

        JTable historyTable = new JTable(historyModel);
        transactionSorter = new TableRowSorter<>(historyModel);
        historyTable.setRowSorter(transactionSorter);
        setupTable(historyTable, new int[]{100, 200, 150, 150});

        // Search Components
        JPanel searchPanel = new JPanel();
        transactionSearchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");

        setupSearchFunctionality(transactionSearchField, transactionSorter);
        searchBtn.addActionListener(e -> historySearch());
        clearBtn.addActionListener(e -> {
            transactionSearchField.setText("");
            transactionSorter.setRowFilter(null);
        });
        refreshBtn.addActionListener(e -> loadTransactionHistory(historyModel));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(transactionSearchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);
        searchPanel.add(refreshBtn);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        
        loadTransactionHistory(historyModel);
        return panel;
    }

    private void setupSearchFunctionality(JTextField searchField, TableRowSorter<TableModel> sorter) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            
            private void filter() {
                String text = searchField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    } catch (PatternSyntaxException ex) {
                        // Silently ignore invalid patterns
                    }
                }
            }
        });
    }

    private void setupTable(JTable table, int[] columnWidths) {
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
    }

    // Database Operations
    private void loadProducts(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT product_id, product_name, selling_price, stock_quantity " +
                 "FROM products WHERE stock_quantity > 0")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getBigDecimal("selling_price").doubleValue(),
                    rs.getInt("stock_quantity")
                });
            }
        } catch (SQLException e) {
            showError("Error loading products", e);
        }
    }

    private void loadTransactionHistory(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT t.transaction_id, t.transaction_date, t.total_amount, t.payment_method " +
                 "FROM transactions t WHERE t.user_id = ? ORDER BY t.transaction_date DESC")) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("transaction_date"),
                    rs.getDouble("total_amount"),
                    rs.getString("payment_method")
                });
            }
        } catch (SQLException e) {
            showError("Error loading transaction history", e);
        }
    }

    // Cart Operations
    private void addToCart(JTable productsTable) {
        int row = productsTable.getSelectedRow();
        if (row < 0) return;

        int productId = (int) productsTable.getValueAt(row, 0);
        String productName = (String) productsTable.getValueAt(row, 1);
        double price = (double) productsTable.getValueAt(row, 2);
        int stock = (int) productsTable.getValueAt(row, 3);

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if ((int) cartModel.getValueAt(i, 0) == productId) {
                int currentQty = (int) cartModel.getValueAt(i, 3);
                if (currentQty < stock) {
                    cartModel.setValueAt(currentQty + 1, i, 3);
                    cartModel.setValueAt((currentQty + 1) * price, i, 4);
                    updateTotal();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Cannot exceed available stock", 
                        "Stock Limit", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }
        }

        cartModel.addRow(new Object[]{productId, productName, price, 1, price});
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += (double) cartModel.getValueAt(i, 4);
        }
        txtTotal.setText(String.format("%,.2f", total));
    }

    private void clearCart() {
        cartModel.setRowCount(0);
        txtTotal.setText("0.00");
    }

    private void processTransaction() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Cart is empty", 
                "No Items", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[] options = {"Cash", "QRIS"};
        int choice = JOptionPane.showOptionDialog(this,
            "Select payment method:",
            "Payment Method",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) return;

        String paymentMethod = (choice == 0) ? "cash" : "qris";
        processPayment(paymentMethod);
    }

    private void processPayment(String paymentMethod) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Create transaction
            int transactionId = createTransaction(conn, paymentMethod);
            
            // Add transaction details
            addTransactionDetails(conn, transactionId);
            
            conn.commit();
            
            JOptionPane.showMessageDialog(this,
                "Transaction #" + transactionId + " completed!\n" +
                "Total: " + txtTotal.getText(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearCart();
            ReceiptPrinter.printReceipt(transactionId);
            
        } catch (SQLException e) {
            showError("Transaction failed", e);
            try {
                DBConnection.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private int createTransaction(Connection conn, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, total_amount, payment_method) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, Double.parseDouble(txtTotal.getText().replace(",", "")));
            stmt.setString(3, paymentMethod);
            
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to generate transaction ID");
    }

    private void addTransactionDetails(Connection conn, int transactionId) throws SQLException {
        String detailSql = "INSERT INTO transaction_details (transaction_id, product_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
        String stockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
        
        try (PreparedStatement detailStmt = conn.prepareStatement(detailSql);
             PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
            
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                int productId = (int) cartModel.getValueAt(i, 0);
                int quantity = (int) cartModel.getValueAt(i, 3);
                double price = (double) cartModel.getValueAt(i, 2);
                
                // Add transaction detail
                detailStmt.setInt(1, transactionId);
                detailStmt.setInt(2, productId);
                detailStmt.setInt(3, quantity);
                detailStmt.setDouble(4, price);
                detailStmt.setDouble(5, quantity * price);
                detailStmt.addBatch();
                
                // Update stock
                stockStmt.setInt(1, quantity);
                stockStmt.setInt(2, productId);
                stockStmt.addBatch();
            }
            
            detailStmt.executeBatch();
            stockStmt.executeBatch();
        }
    }

    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, 
            message + ": " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Search Implementations
    private void search() {
        String query = productSearchField.getText();
        if (query.trim().isEmpty()) {
            productSorter.setRowFilter(null);
        } else {
            try {
                productSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            } catch (PatternSyntaxException e) {
                // Invalid pattern - silent ignore
            }
        }
    }

    private void historySearch() {
        String query = transactionSearchField.getText();
        if (query.trim().isEmpty()) {
            transactionSorter.setRowFilter(null);
        } else {
            try {
                transactionSorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            } catch (PatternSyntaxException e) {
                // Invalid pattern - silent ignore
            }
        }
    }
}
