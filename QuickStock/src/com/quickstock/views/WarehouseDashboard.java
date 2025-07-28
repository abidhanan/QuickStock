package com.quickstock.views;

import com.quickstock.db.DBConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class WarehouseDashboard extends BaseDashboard {
    private DefaultTableModel stockModel; // Declare stockModel at the class level

    public WarehouseDashboard(int userId, String fullName) {
        super(userId, fullName);
        setTitle("QuickStock - Warehouse Dashboard (" + fullName + ")");
        initCommonComponents();
        initComponents();
        
        // Set the dashboard to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow closing the window
        setUndecorated(false); // Keep window decorations for minimize and close buttons
    }
    
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Manage Stock Tab
        JPanel stockPanel = new JPanel(new BorderLayout());
        JTable stockTable = new JTable();
        JScrollPane stockScroll = new JScrollPane(stockTable);
        JButton btnAdd = new JButton("Add Product");
        JButton btnEdit = new JButton("Edit Product");
        JButton btnDelete = new JButton("Delete Product");
        JButton btnSearch = new JButton("Search");
        JButton btnClear = new JButton("Clear");
        JButton btnRefresh = new JButton("Refresh"); // Refresh button
        JTextField searchField = new JTextField(15);
        
        stockModel = new DefaultTableModel(
            new Object[] {"ID", "Product Name", "Category", "Buy Price", "Sell Price", "Stock Quantity", "Min Stock"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only stock quantity is editable
            }
        };
        
        refreshStockTable(stockModel);
        
        stockTable.setModel(stockModel);
        setColumnWidths(stockTable, new int[]{50, 150, 100, 100, 100, 100, 100}); // Set column widths
        setLeftAlignment(stockTable); // Align text to the left
        
        // Create a panel for search and clear buttons
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClear);
        searchPanel.add(btnRefresh); // Add refresh button to the search panel
        
        stockPanel.add(searchPanel, BorderLayout.NORTH); // Add search panel to the top
        stockPanel.add(stockScroll, BorderLayout.CENTER); // Add stock table
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        stockPanel.add(buttonPanel, BorderLayout.SOUTH); // Add action buttons at the bottom
        
        tabbedPane.addTab("Manage Stock", stockPanel);
        
        // Add tabs to main frame
        getContentPane().add(tabbedPane);
        
        // Event Listeners
        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct(stockTable, stockModel));
        btnDelete.addActionListener(e -> deleteProduct(stockTable, stockModel));
        
        // Search functionality
        btnSearch.addActionListener(e -> searchProduct(searchField.getText()));
        btnClear.addActionListener(e -> {
            searchField.setText("");
            refreshStockTable(stockModel); // Refresh the table to show all products
        });
        
        // Refresh functionality
        btnRefresh.addActionListener(e -> refreshStockTable(stockModel)); // Refresh stock data

        // Add DocumentListener for live search filtering
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(stockModel);
        stockTable.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(sorter, searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(sorter, searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(sorter, searchField.getText());
            }

            private void filterTable(TableRowSorter<TableModel> sorter, String text) {
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // Case-insensitive search
                }
            }
        });
    }

    private void addProduct() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField category = new JTextField();
        JTextField buyPrice = new JTextField();
        JTextField sellPrice = new JTextField();
        JTextField stockQuantity = new JTextField();
        JTextField minStock = new JTextField();
        
        panel.add(new JLabel("Product ID:"));
        panel.add(id);
        panel.add(new JLabel("Product Name:"));
        panel.add(name);
        panel.add(new JLabel("Category:"));
        panel.add(category);
        panel.add(new JLabel("Buy Price:"));
        panel.add(buyPrice);
        panel.add(new JLabel("Sell Price:"));
        panel.add(sellPrice);
        panel.add(new JLabel("Stock Quantity:"));
        panel.add(stockQuantity);
        panel.add(new JLabel("Min Stock:"));
        panel.add(minStock);
        
        int result = showInputDialog(panel, "Add New Product");
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO products (product_id, product_name, category, buying_price, selling_price, stock_quantity, min_stock_level) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(id.getText()));
                stmt.setString(2, name.getText());
                stmt.setString(3, category.getText());
                stmt.setDouble(4, Double.parseDouble(buyPrice.getText()));
                stmt.setDouble(5, Double.parseDouble(sellPrice.getText()));
                stmt.setInt(6, Integer.parseInt(stockQuantity.getText()));
                stmt.setInt(7, Integer.parseInt(minStock.getText()));
                stmt.executeUpdate();
                showMessage("Product added successfully!");
                refreshStockTable(stockModel); // Refresh the stock table after adding
            } catch (SQLException ex) {
                showError("Error adding product: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values for ID, prices, and quantities.");
            }
        }
    }

    private void editProduct(JTable stockTable, DefaultTableModel stockModel) {
        int row = stockTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a product first");
            return;
        }
        
        int productId = (int) stockModel.getValueAt(row, 0);
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5)); // Increased rows to 8 for ID
        JTextField idField = new JTextField(String.valueOf(productId)); // Editable ID field
        JTextField name = new JTextField(stockModel.getValueAt(row, 1) != null ? stockModel.getValueAt(row, 1).toString() : "");
        JTextField category = new JTextField(stockModel.getValueAt(row, 2) != null ? stockModel.getValueAt(row, 2).toString() : "");
        JTextField buyPrice = new JTextField(stockModel.getValueAt(row, 3) != null ? stockModel.getValueAt(row, 3).toString() : "");
        JTextField sellPrice = new JTextField(stockModel.getValueAt(row, 4) != null ? stockModel.getValueAt(row, 4).toString() : "");
        JTextField stockQuantity = new JTextField(stockModel.getValueAt(row, 5) != null ? stockModel.getValueAt(row, 5).toString() : "");
        JTextField minStock = new JTextField(stockModel.getValueAt(row, 6) != null ? stockModel.getValueAt(row, 6).toString() : "");
        
        panel.add(new JLabel("Product ID:")); // Added ID label
        panel.add(idField); // Added ID field
        panel.add(new JLabel("Product Name:"));
        panel.add(name);
        panel.add(new JLabel("Category:"));
        panel.add(category);
        panel.add(new JLabel("Buy Price:"));
        panel.add(buyPrice);
        panel.add(new JLabel("Sell Price:"));
        panel.add(sellPrice);
        panel.add(new JLabel("Stock Quantity:"));
        panel.add(stockQuantity);
        panel.add(new JLabel("Min Stock:"));
        panel.add(minStock);
        
        int result = showInputDialog(panel, "Edit Product");
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE products SET product_id=?, product_name=?, category=?, buying_price=?, selling_price=?, stock_quantity=?, min_stock_level=? WHERE product_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(idField.getText())); // Update ID
                stmt.setString(2, name.getText());
                stmt.setString(3, category.getText());
                stmt.setDouble(4, Double.parseDouble(buyPrice.getText()));
                stmt.setDouble(5, Double.parseDouble(sellPrice.getText()));
                stmt.setInt(6, Integer.parseInt(stockQuantity.getText()));
                stmt.setInt(7, Integer.parseInt(minStock.getText()));
                stmt.setInt(8, productId); // Original ID for WHERE clause
                stmt.executeUpdate();
                showMessage("Product updated successfully!");
                refreshStockTable(stockModel);
            } catch (SQLException ex) {
                showError("Error updating product: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values for prices and quantities.");
            }
        }
    }

    private void deleteProduct(JTable stockTable, DefaultTableModel stockModel) {
        int row = stockTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a product first");
            return;
        }
        
        int productId = (int) stockModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM products WHERE product_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, productId);
                stmt.executeUpdate();
                showMessage("Product deleted successfully!");
                refreshStockTable(stockModel);
            } catch (SQLException ex) {
                showError("Error deleting product: " + ex.getMessage());
            }
        }
    }
    
    private void refreshStockTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT product_id, product_name, category, buying_price, selling_price, stock_quantity, min_stock_level FROM products");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[] {
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading stock data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchProduct(String searchText) {
        if (searchText.isEmpty()) {
            refreshStockTable(stockModel); // Refresh to show all products if search text is empty
            return;
        }

        DefaultTableModel filteredModel = new DefaultTableModel(
            new Object[] {"ID", "Product Name", "Category", "Buy Price", "Sell Price", "Stock Quantity", "Min Stock"}, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT product_id, product_name, category, buying_price, selling_price, stock_quantity, min_stock_level " +
                "FROM products WHERE product_name LIKE ? OR category LIKE ?");
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                filteredModel.addRow(new Object[] {
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("buying_price"),
                    rs.getDouble("selling_price"),
                    rs.getInt("stock_quantity"),
                    rs.getInt("min_stock_level")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        stockModel.setRowCount(0); // Clear the original model
        for (int i = 0; i < filteredModel.getRowCount(); i++) {
            stockModel.addRow(new Object[] {
                filteredModel.getValueAt(i, 0),
                filteredModel.getValueAt(i, 1),
                filteredModel.getValueAt(i, 2),
                filteredModel.getValueAt(i, 3),
                filteredModel.getValueAt(i, 4),
                filteredModel.getValueAt(i, 5),
                filteredModel.getValueAt(i, 6)
            });
        }
    }

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
}
