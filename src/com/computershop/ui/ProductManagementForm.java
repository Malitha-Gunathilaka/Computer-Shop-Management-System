package com.computershop.ui;

import com.computershop.dao.ProductDAO;
import com.computershop.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProductManagementForm extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private ProductDAO productDAO;

    public ProductManagementForm() {
        setTitle("Product Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        productDAO = new ProductDAO();

        // Initialize components
        mainPanel = new JPanel();
        productTable = new JTable();
        addButton = new JButton("Add Product");
        editButton = new JButton("Edit Product");
        deleteButton = new JButton("Delete Product");

        // Layout and add components
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Load data
        loadProductData();

        // Add action listeners for buttons
        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
    }

    private void loadProductData() {
        List<Product> products = productDAO.getAllProducts();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Category");
        model.addColumn("Price");
        model.addColumn("Stock");
        for (Product product : products) {
            model.addRow(new Object[]{product.getId(), product.getName(), product.getCategory(), product.getPrice(), product.getStock()});
        }
        productTable.setModel(model);
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog(this, "Enter product name:");
        String category = JOptionPane.showInputDialog(this, "Enter product category:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter product price:");
        String stockStr = JOptionPane.showInputDialog(this, "Enter product stock:");

        if (name != null && category != null && priceStr != null && stockStr != null) {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setStock(stock);

            try {
                productDAO.addProduct(product);
                JOptionPane.showMessageDialog(this, "Product added successfully.");
                loadProductData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage());
            }
        }
    }

    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to edit.");
            return;
        }

        int productId = (int) productTable.getValueAt(selectedRow, 0);
        String name = JOptionPane.showInputDialog(this, "Enter new product name:");
        String category = JOptionPane.showInputDialog(this, "Enter new product category:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter new product price:");
        String stockStr = JOptionPane.showInputDialog(this, "Enter new product stock:");

        if (name != null && category != null && priceStr != null && stockStr != null) {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            Product product = new Product();
            product.setId(productId);
            product.setName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setStock(stock);

            try {
                productDAO.updateProduct(product);
                JOptionPane.showMessageDialog(this, "Product updated successfully.");
                loadProductData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?");
        if (confirm == JOptionPane.YES_OPTION) {
            int productId = (int) productTable.getValueAt(selectedRow, 0);
            try {
                productDAO.deleteProduct(productId);
                JOptionPane.showMessageDialog(this, "Product deleted successfully.");
                loadProductData(); // Refresh table
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductManagementForm().setVisible(true));
    }
}
