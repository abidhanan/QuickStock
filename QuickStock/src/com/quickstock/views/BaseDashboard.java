package com.quickstock.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

public class BaseDashboard extends JFrame {
    protected int userId;
    protected String fullName;
    protected JMenuBar menuBar;
    protected JMenu fileMenu;

    public BaseDashboard(int userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        initCommonComponents();
    }

    protected void initCommonComponents() {
        // Create menu bar
        menuBar = new JMenuBar();
        
        // File Menu
        fileMenu = new JMenu("Other Menu");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this::showAboutDialog);
        fileMenu.add(aboutItem);
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(this::logoutAction);
        fileMenu.add(logoutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        
        setJMenuBar(menuBar);
    }

    protected void logoutAction(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }

    protected void showAboutDialog(ActionEvent e) {
        JOptionPane.showMessageDialog(
            this,
            "QuickStock Inventory Management System\nVersion 1.0",
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    protected int showInputDialog(JPanel panel, String title) {
        return JOptionPane.showOptionDialog(
            this,
            panel,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            null
        );
    }

    // Utility method to create standardized buttons
    protected JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    // Method to refresh tables
    protected void refreshTable(JTable table, DefaultTableModel model) {
        table.setModel(model);
        table.revalidate();
        table.repaint();
    }
}
