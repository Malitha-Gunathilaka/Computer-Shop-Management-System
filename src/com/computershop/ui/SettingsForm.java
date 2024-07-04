package com.computershop.ui;

import com.computershop.dao.SettingsDAO;
import com.computershop.model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class SettingsForm extends JFrame {
    private JTextField taxRateField;
    private JTextField discountRateField;
    private SettingsDAO settingsDAO;

    public SettingsForm() {
        settingsDAO = new SettingsDAO();

        setTitle("Settings");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        setContentPane(panel);

        panel.add(new JLabel("Tax Rate (%):"));
        taxRateField = new JTextField();
        panel.add(taxRateField);

        panel.add(new JLabel("Discount Rate (%):"));
        discountRateField = new JTextField();
        panel.add(discountRateField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveSettings);
        panel.add(saveButton);

        loadSettings();
    }

    private void loadSettings() {
        try {
            Settings settings = settingsDAO.getSettings();
            if (settings != null) {
                taxRateField.setText(String.valueOf(settings.getTaxRate()));
                discountRateField.setText(String.valueOf(settings.getDiscountRate()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading settings: " + e.getMessage());
        }
    }

    private void saveSettings(ActionEvent e) {
        try {
            double taxRate = Double.parseDouble(taxRateField.getText());
            double discountRate = Double.parseDouble(discountRateField.getText());

            Settings settings = new Settings();
            settings.setTaxRate(taxRate);
            settings.setDiscountRate(discountRate);

            settingsDAO.saveSettings(settings);
            JOptionPane.showMessageDialog(this, "Settings saved successfully.");
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error saving settings: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SettingsForm form = new SettingsForm();
            form.setVisible(true);
        });
    }
}
