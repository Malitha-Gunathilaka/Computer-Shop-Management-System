package com.computershop.ui;

import com.computershop.dao.CustomerDAO;
import com.computershop.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerManagementForm extends JFrame {
    private JPanel mainPanel;
    private JTable customerTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private CustomerDAO customerDAO;

    public CustomerManagementForm() {
        setTitle("Customer Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        customerDAO = new CustomerDAO();

        // Initialize components
        mainPanel = new JPanel();
        customerTable = new JTable();
        addButton = new JButton("Add Customer");
        editButton = new JButton("Edit Customer");
        deleteButton = new JButton("Delete Customer");

        // Layout and add components
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Load data
        loadCustomerData();

        // Add action listeners for buttons
        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
    }

    private void loadCustomerData() {
        List<Customer> customers = customerDAO.getAllCustomers();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Phone");
        for (Customer customer : customers) {
            model.addRow(new Object[]{customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone()});
        }
        customerTable.setModel(model);
    }

    private void addCustomer() {
        String name = JOptionPane.showInputDialog(this, "Enter name:");
        String email = JOptionPane.showInputDialog(this, "Enter email:");
        String phone = JOptionPane.showInputDialog(this, "Enter phone:");
        
        if (name != null && email != null && phone != null) {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhone(phone);
            
            try {
                customerDAO.addCustomer(customer);
                JOptionPane.showMessageDialog(this, "Customer added successfully.");
                loadCustomerData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage());
            }
        }
    }

    private void editCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer to edit.");
            return;
        }

        int customerId = (int) customerTable.getValueAt(selectedRow, 0);
        String name = JOptionPane.showInputDialog(this, "Enter new name:");
        String email = JOptionPane.showInputDialog(this, "Enter new email:");
        String phone = JOptionPane.showInputDialog(this, "Enter new phone:");
        
        if (name != null && email != null && phone != null) {
            Customer customer = new Customer();
            customer.setId(customerId);
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhone(phone);
            
            try {
                customerDAO.updateCustomer(customer);
                JOptionPane.showMessageDialog(this, "Customer updated successfully.");
                loadCustomerData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating customer: " + e.getMessage());
            }
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?");
        if (confirm == JOptionPane.YES_OPTION) {
            int customerId = (int) customerTable.getValueAt(selectedRow, 0);
            try {
                customerDAO.deleteCustomer(customerId);
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
                loadCustomerData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerManagementForm().setVisible(true);
        });
    }
}
