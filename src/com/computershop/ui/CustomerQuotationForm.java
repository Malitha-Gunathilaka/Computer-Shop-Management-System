package com.computershop.ui;

import com.computershop.dao.ProductDAO;
import com.computershop.dao.QuotationDAO;
import com.computershop.dao.QuotationItemDAO;
import com.computershop.model.Product;
import com.computershop.model.Quotation;
import com.computershop.model.QuotationItem;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerQuotationForm extends JFrame {
    private JPanel mainPanel;
    private JTable productTable;
    private JTable quotationTable;
    private JButton addButton;
    private JButton removeButton;
    private JButton generateBillButton;
    private JLabel totalLabel;
    private List<QuotationItem> quotationItems;
    private ProductDAO productDAO;
    private QuotationDAO quotationDAO;
    private QuotationItemDAO quotationItemDAO;

    public CustomerQuotationForm() {
        setTitle("Customer Quotation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        productDAO = new ProductDAO();
        quotationDAO = new QuotationDAO();
        quotationItemDAO = new QuotationItemDAO();
        quotationItems = new ArrayList<>();

        // Initialize components
        mainPanel = new JPanel();
        productTable = new JTable();
        quotationTable = new JTable();
        addButton = new JButton("Add to Quotation");
        removeButton = new JButton("Remove from Quotation");
        generateBillButton = new JButton("Generate Bill");
        totalLabel = new JLabel("Total: $0.00");

        // Layout and add components
        mainPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JScrollPane(quotationTable), BorderLayout.CENTER);
        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setLayout(new BoxLayout(bottomRightPanel, BoxLayout.Y_AXIS));
        bottomRightPanel.add(removeButton);
        bottomRightPanel.add(generateBillButton);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        bottomPanel.add(totalLabel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Load data
        loadProductData();
        loadQuotationData();

        // Add action listeners for buttons
        addButton.addActionListener(e -> addToQuotation());
        removeButton.addActionListener(e -> removeFromQuotation());
        generateBillButton.addActionListener(e -> generateBill());
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

    private void loadQuotationData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Product ID");
        model.addColumn("Name");
        model.addColumn("Quantity");
        model.addColumn("Price");
        for (QuotationItem item : quotationItems) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product != null) {
                model.addRow(new Object[]{item.getProductId(), product.getName(), item.getQuantity(), item.getPrice()});
            }
        }
        quotationTable.setModel(model);
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (QuotationItem item : quotationItems) {
            total += item.getPrice();
        }
        totalLabel.setText("Total: $" + total);
    }

    private void addToQuotation() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to add.");
            return;
        }

        int productId = (int) productTable.getValueAt(selectedRow, 0);
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (quantityStr != null) {
            int quantity = Integer.parseInt(quantityStr);
            Product product = productDAO.getProductById(productId);
            double price = product.getPrice() * quantity;

            QuotationItem item = new QuotationItem();
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setPrice(price);
            quotationItems.add(item);
            loadQuotationData();
        }
    }

    private void removeFromQuotation() {
        int selectedRow = quotationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to remove.");
            return;
        }

        quotationItems.remove(selectedRow);
        loadQuotationData();
    }

    private void generateBill() {
        if (quotationItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the quotation.");
            return;
        }

        int customerId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter customer ID:"));
        double total = quotationItems.stream().mapToDouble(QuotationItem::getPrice).sum();

        Quotation quotation = new Quotation();
        quotation.setCustomerId(customerId);
        quotation.setTotal(total);

        try {
            quotationDAO.addQuotation(quotation);
            int quotationId = quotationDAO.getLastInsertedId();

            for (QuotationItem item : quotationItems) {
                item.setQuotationId(quotationId);
                quotationItemDAO.addQuotationItem(item);
            }

            JOptionPane.showMessageDialog(this, "Quotation generated successfully.");
            createPdfBill(quotationId);
            quotationItems.clear();
            loadQuotationData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating quotation: " + e.getMessage());
        }
    }

    private void createPdfBill(int quotationId) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("quotation_" + quotationId + ".pdf"));
            document.open();
            Quotation quotation = quotationDAO.getQuotationById(quotationId);
            List<QuotationItem> items = quotationItemDAO.getQuotationItemsByQuotationId(quotationId);


            document.add(new Paragraph("Quotation ID: " + quotationId));
            document.add(new Paragraph("Customer ID: " + quotation.getCustomerId()));
            document.add(new Paragraph("Date: " + quotation.getDate()));
            document.add(new Paragraph("Total: $" + quotation.getTotal()));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Product ID");
            table.addCell("Name");
            table.addCell("Quantity");
            table.addCell("Total Price");

            for (QuotationItem item : items) {
                Product product = productDAO.getProductById(item.getProductId());
                table.addCell(String.valueOf(product.getId()));
                table.addCell(product.getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.valueOf(item.getPrice()));
            }

            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(this, "PDF bill generated successfully.");
        } catch (FileNotFoundException | DocumentException e) {
            JOptionPane.showMessageDialog(this, "Error generating PDF bill: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerQuotationForm().setVisible(true));
    }
}
