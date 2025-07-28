package com.quickstock.views;

import com.quickstock.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends javax.swing.JFrame {
    public LoginForm() {
        initComponents();
        setLocationRelativeTo(null); // Center the frame on startup
        setSize(400, 300); // Set a fixed size for the form
        setResizable(false); // Prevent resizing the form
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on exit
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField(15); // Set width of username field
        jLabel3 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField(15); // Set width of password field
        btnLogin = new javax.swing.JButton();

        setTitle("QuickStock - Login");

        jPanel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Center the title
        jLabel1.setText("QUICKSTOCK");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Center alignment
        jLabel1.setFont(new Font("Arial", Font.BOLD, 18)); // Change font and size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        jPanel1.add(jLabel1, gbc);

        jLabel2.setText("Username:");
        gbc.gridwidth = 1; // Reset to one column
        gbc.gridx = 0;
        gbc.gridy = 1;
        jPanel1.add(jLabel2, gbc);

        gbc.gridx = 1;
        jPanel1.add(txtUsername, gbc);

        jLabel3.setText("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPanel1.add(jLabel3, gbc);

        gbc.gridx = 1;
        jPanel1.add(txtPassword, gbc);

        btnLogin.setText("Login");
        btnLogin.addActionListener(evt -> btnLoginActionPerformed(evt));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Center the button across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        jPanel1.add(btnLogin, gbc);

        // Set layout for the main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.CENTER);

        pack();
    }

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txtUsername.getText();
        String password = String.valueOf(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username=? AND password=SHA2(?, 256) AND is_active=1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("user_id");
                String fullName = rs.getString("full_name");
                
                this.dispose();
                showDashboard(role, userId, fullName);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showDashboard(String role, int userId, String fullName) {
        switch (role) {
            case "owner":
                new OwnerDashboard(userId, fullName).setVisible(true);
                break;
            case "admin":
                new AdminDashboard(userId, fullName).setVisible(true);
                break;
            case "cashier":
                new CashierDashboard(userId, fullName).setVisible(true);
                break;
            case "warehouse":
                new WarehouseDashboard(userId, fullName).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid user role", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }

    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
}
