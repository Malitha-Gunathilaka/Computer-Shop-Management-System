package com.computershop.ui;

import com.computershop.dao.QuotationDAO;
import com.computershop.dao.CustomerDAO;
import com.computershop.model.Quotation;
import com.computershop.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class QuotationManagementForm extends JFrame {
    private JPanel mainPanel;
    private JTable quotationTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private QuotationDAO quotationDAO;
    private CustomerDAO customerDAO;

    public QuotationManagementForm() {
        setTitle("Quotation Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        quotationDAO = new QuotationDAO();
        customerDAO = new CustomerDAO();

        // Initialize components
        mainPanel = new JPanel();
        quotationTable = new JTable();
        addButton = new JButton("Add Quotation");
        editButton = new JButton("Edit Quotation");
        deleteButton = new JButton("Delete Quotation");

        // Layout and add components
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(quotationTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Load data
        loadQuotationData();

        // Add action listeners for buttons
        addButton.addActionListener(e -> addQuotation());
        editButton.addActionListener(e -> editQuotation());
        deleteButton.addActionListener(e -> deleteQuotation());
    }

    private void loadQuotationData() {
        List<Quotation> quotations = quotationDAO.getAllQuotations();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Customer ID");
        model.addColumn("Total");
        model.addColumn("Date");
        for (Quotation quotation : quotations) {
            model.addRow(new Object[]{quotation.getId(), quotation.getCustomerId(), quotation.getTotal(), quotation.getDate()});
        }
        quotationTable.setModel(model);
    }

    private void addQuotation() {
        String customerIdStr = JOptionPane.showInputDialog(this, "Enter customer ID:");
        String totalStr = JOptionPane.showInputDialog(this, "Enter total amount:");

        if (customerIdStr != null && totalStr != null) {
            int customerId = Integer.parseInt(customerIdStr);
            double total = Double.parseDouble(totalStr);

            Quotation quotation = new Quotation();
            quotation.setCustomerId(customerId);
            quotation.setTotal(total);

            try {
                quotationDAO.addQuotation(quotation);
                JOptionPane.showMessageDialog(this, "Quotation added successfully.");
                loadQuotationData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding quotation: " + e.getMessage());
            }
        }
    }

    private void editQuotation() {
        int selectedRow = quotationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a quotation to edit.");
            return;
        }

        int quotationId = (int) quotationTable.getValueAt(selectedRow, 0);
        String customerIdStr = JOptionPane.showInputDialog(this, "Enter new customer ID:");
        String totalStr = JOptionPane.showInputDialog(this, "Enter new total amount:");

        if (customerIdStr != null && totalStr != null) {
            int customerId = Integer.parseInt(customerIdStr);
            double total = Double.parseDouble(totalStr);

            Quotation quotation = new Quotation();
            quotation.setId(quotationId);
            quotation.setCustomerId(customerId);
            quotation.setTotal(total);

            try {
                quotationDAO.updateQuotation(quotation);
                JOptionPane.showMessageDialog(this, "Quotation updated successfully.");
                loadQuotationData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating quotation: " + e.getMessage());
            }
        }
    }

    private void deleteQuotation() {
        int selectedRow = quotationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a quotation to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this quotation?");
        if (confirm == JOptionPane.YES_OPTION) {
            int quotationId = (int) quotationTable.getValueAt(selectedRow, 0);
            try {
                quotationDAO.deleteQuotation(quotationId);
                JOptionPane.showMessageDialog(this, "Quotation deleted successfully.");
                loadQuotationData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting quotation: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuotationManagementForm().setVisible(true));
    }
}
