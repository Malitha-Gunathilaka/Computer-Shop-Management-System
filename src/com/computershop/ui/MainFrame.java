package com.computershop.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Computer Shop Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JButton customerManagementButton = new JButton("Customer Management");
        JButton productManagementButton = new JButton("Product Management");
        JButton quotationManagementButton = new JButton("Quotation Management");
        JButton customerQuotationButton = new JButton("Customer Quotation");
        JButton settingsButton = new JButton("Settings");
        JButton logoutButton = new JButton("Logout");

        mainPanel.add(customerManagementButton);
        mainPanel.add(productManagementButton);
        mainPanel.add(quotationManagementButton);
        mainPanel.add(customerQuotationButton);
        mainPanel.add(settingsButton);
        mainPanel.add(logoutButton);

        setContentPane(mainPanel);

        customerManagementButton.addActionListener(e -> new CustomerManagementForm().setVisible(true));
        productManagementButton.addActionListener(e -> new ProductManagementForm().setVisible(true));
        quotationManagementButton.addActionListener(e -> new QuotationManagementForm().setVisible(true));
        customerQuotationButton.addActionListener(e -> new CustomerQuotationForm().setVisible(true));
        settingsButton.addActionListener(e -> new SettingsForm().setVisible(true));
        logoutButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
