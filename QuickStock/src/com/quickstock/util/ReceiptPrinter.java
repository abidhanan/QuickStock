// ReceiptPrinter.java
package com.quickstock.util;

import com.quickstock.db.DBConnection;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ReceiptPrinter {

    public static void printReceipt(int transactionId) {
        JFrame receiptFrame = new JFrame("Receipt #" + transactionId);
        receiptFrame.setSize(350, 600);
        receiptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea receiptText = new JTextArea();
        receiptText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptText.setEditable(false);

        generateReceiptText(receiptText, transactionId);

        panel.add(new JScrollPane(receiptText), BorderLayout.CENTER);
        receiptFrame.add(panel);
        receiptFrame.setVisible(true);

        // Auto-scroll to top
        receiptText.setCaretPosition(0);
        
        // Print dialog
        try {
            boolean complete = receiptText.print();
            if (complete) {
                System.out.println("Printing completed");
            } else {
                System.out.println("Printing cancelled");
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
            System.out.println("Printing failed: " + ex.getMessage());
        }
    }

    private static void generateReceiptText(JTextArea receiptText, int transactionId) {
        try {
            Connection conn = DBConnection.getConnection();

            // Get transaction details
            PreparedStatement transStmt = conn.prepareStatement(
                "SELECT t.*, u.full_name FROM transactions t JOIN users u ON t.user_id = u.user_id " +
                "WHERE t.transaction_id = ?");
            transStmt.setInt(1, transactionId);
            ResultSet transRs = transStmt.executeQuery();

            if (!transRs.next()) {
                receiptText.setText("Transaction not found!");
                return;
            }

            // Get transaction items
            PreparedStatement itemsStmt = conn.prepareStatement(
                "SELECT td.*, p.product_name FROM transaction_details td " +
                "JOIN products p ON td.product_id = p.product_id " +
                "WHERE td.transaction_id = ?");
            itemsStmt.setInt(1, transactionId);
            ResultSet itemsRs = itemsStmt.executeQuery();

            // Prepare receipt text
            StringBuilder sb = new StringBuilder();
            sb.append("   QUICKSTOCK INVENTORY SYSTEM\n");
            sb.append("   --------------------------\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(String.format("   Date: %s\n", sdf.format(new Date())));
            sb.append(String.format("   Receipt #: %d\n", transactionId));
            sb.append(String.format("   Cashier: %s\n\n", transRs.getString("full_name")));
            
            sb.append("   --------------------------\n");
            sb.append("   Qty  Item              Price\n");
            sb.append("   --------------------------\n");

            while (itemsRs.next()) {
                String productName = itemsRs.getString("product_name");
                if (productName.length() > 15) {
                    productName = productName.substring(0, 12) + "...";
                }
                
                sb.append(String.format("   %-3d  %-15s  %6.2f\n",
                    itemsRs.getInt("quantity"),
                    productName,
                    itemsRs.getDouble("price")));
            }

            sb.append("   --------------------------\n");
            sb.append(String.format("   TOTAL:               %6.2f\n", transRs.getDouble("total_amount")));
            sb.append(String.format("   Payment Method: %s\n", transRs.getString("payment_method")));
            sb.append("   --------------------------\n");
            sb.append("   Thank you for shopping!\n");
            sb.append("   Please come again\n");

            receiptText.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            receiptText.setText("Error generating receipt: " + e.getMessage());
        }
    }
}
