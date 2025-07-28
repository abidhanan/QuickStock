package com.quickstock.views;

import com.quickstock.db.DBConnection;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class OwnerDashboard extends BaseDashboard {

    private JTable transactionTable;
    private JTable stockTable;
    private TableRowSorter<TableModel> transactionSorter;
    private TableRowSorter<TableModel> stockSorter;

    public OwnerDashboard(int userId, String fullName) {
        super(userId, fullName);
        setTitle("QuickStock - Owner Dashboard (" + fullName + ")");
        initCommonComponents();
        initComponents();
        
        // Set the dashboard to maximized state but keep window decorations
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow closing the window

        // Load reports
        loadTransactionReports();
        loadStockReports();
    }
    
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // ================================
        // Transaction Reports Tab
        // ================================
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionTable = new JTable();
        JScrollPane transactionScroll = new JScrollPane(transactionTable);

        // Populate transaction table model
        DefaultTableModel transactionModel = new DefaultTableModel(
            new Object[] {"Transaction ID", "Date", "Cashier", "Total Amount", "Payment Method"}, 0);
        transactionTable.setModel(transactionModel);

        // Set column widths
        setColumnWidths(transactionTable, new int[]{100, 150, 150, 150, 150});

        // Align text to the left
        setLeftAlignment(transactionTable);

        // Add search panel for transaction table
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");

        // Add DocumentListener for search functionality
        transactionSorter = new TableRowSorter<>(transactionTable.getModel());
        transactionTable.setRowSorter(transactionSorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(transactionSorter, searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(transactionSorter, searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(transactionSorter, searchField.getText());
            }
        });

        // Add button listeners
        searchBtn.addActionListener(e -> search(transactionSorter, searchField.getText()));
        clearBtn.addActionListener(e -> {
            searchField.setText(""); // Clear search field
            transactionSorter.setRowFilter(null); // Reset the table view
        });

        refreshBtn.addActionListener(e -> loadTransactionReports()); // Reload transaction reports

        // Add components to search panel
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);
        searchPanel.add(refreshBtn); // Add refresh button

        // Add components to transaction panel
        transactionPanel.add(searchPanel, BorderLayout.NORTH);
        transactionPanel.add(transactionScroll, BorderLayout.CENTER);
        tabbedPane.addTab("Transaction Reports", transactionPanel);

        // ================================
        // Stock Reports Tab
        // ================================
        JPanel stockPanel = new JPanel(new BorderLayout());
        stockTable = new JTable();
        JScrollPane stockScroll = new JScrollPane(stockTable);
        
        // Populate stock table model
        DefaultTableModel stockModel = new DefaultTableModel(
            new Object[] {"Product ID", "Product Name", "Buy Price", "Sell Price", "Current Stock"}, 0);
        stockTable.setModel(stockModel);

        // Set column widths for stock table
        setColumnWidths(stockTable, new int[]{100, 200, 100, 100, 100});

        // Align text to the left for stock table
        setLeftAlignment(stockTable);

        // Add search panel for stock table
        JPanel stockSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField stockSearchField = new JTextField(15);
        JButton stockSearchBtn = new JButton("Search");
        JButton stockClearBtn = new JButton("Clear");
        JButton stockRefreshBtn = new JButton("Refresh");

        // Add DocumentListener for stock search field
        stockSorter = new TableRowSorter<>(stockTable.getModel());
        stockTable.setRowSorter(stockSorter);

        stockSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(stockSorter, stockSearchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(stockSorter, stockSearchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(stockSorter, stockSearchField.getText());
            }
        });

        stockSearchBtn.addActionListener(e -> search(stockSorter, stockSearchField.getText()));
        stockClearBtn.addActionListener(e -> {
            stockSearchField.setText(""); // Clear search field
            stockSorter.setRowFilter(null); // Reset the table view
        });

        stockRefreshBtn.addActionListener(e -> loadStockReports()); // Reload stock reports

        // Add components to stock search panel
        stockSearchPanel.add(new JLabel("Search:"));
        stockSearchPanel.add(stockSearchField);
        stockSearchPanel.add(stockSearchBtn);
        stockSearchPanel.add(stockClearBtn);
        stockSearchPanel.add(stockRefreshBtn); // Add refresh button

        // Add components to stock panel
        stockPanel.add(stockSearchPanel, BorderLayout.NORTH);
        stockPanel.add(stockScroll, BorderLayout.CENTER);
        tabbedPane.addTab("Stock Reports", stockPanel);
        
        // Add tabs to main frame
        getContentPane().add(tabbedPane);
    }

    // Helper methods to set column widths and align text
    private void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void setLeftAlignment(JTable table) {
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
    }

    // Load transaction reports from the database
    private void loadTransactionReports() {
        DefaultTableModel transactionModel = (DefaultTableModel) transactionTable.getModel();
        transactionModel.setRowCount(0); // Clear existing data
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT t.transaction_id, t.transaction_date, u.full_name AS cashier, t.total_amount, t.payment_method " +
                         "FROM transactions t JOIN users u ON t.user_id = u.user_id ORDER BY t.transaction_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transactionModel.addRow(new Object[]{
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("transaction_date"),
                    rs.getString("cashier"),
                    rs.getBigDecimal("total_amount"),
                    rs.getString("payment_method")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading transaction reports: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Load stock reports from the database
    private void loadStockReports() {
        DefaultTableModel stockModel = (DefaultTableModel) stockTable.getModel();
        stockModel.setRowCount(0); // Clear existing data
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT product_id, product_name, buying_price, selling_price, stock_quantity " +
                         "FROM products ORDER BY product_id ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                stockModel.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getBigDecimal("buying_price"),
                    rs.getBigDecimal("selling_price"),
                    rs.getInt("stock_quantity")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading stock reports: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Search method for filtering table rows
    private void search(TableRowSorter<TableModel> sorter, String query) {
        if (query.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query)); // Case-insensitive search
        }
    }
}
