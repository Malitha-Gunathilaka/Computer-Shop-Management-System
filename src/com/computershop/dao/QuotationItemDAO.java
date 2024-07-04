package com.computershop.dao;

import com.computershop.model.QuotationItem;
import com.computershop.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuotationItemDAO {
    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    public void addQuotationItem(QuotationItem quotationItem) throws SQLException {
        String sql = "INSERT INTO quotation_items (quotation_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quotationItem.getQuotationId());
            statement.setInt(2, quotationItem.getProductId());
            statement.setInt(3, quotationItem.getQuantity());
            statement.setDouble(4, quotationItem.getPrice());
            statement.executeUpdate();
        }
    }

    public List<QuotationItem> getQuotationItems(int quotationId) {
        List<QuotationItem> quotationItems = new ArrayList<>();
        String sql = "SELECT * FROM quotation_items WHERE quotation_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quotationId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                QuotationItem quotationItem = new QuotationItem();
                quotationItem.setId(resultSet.getInt("id"));
                quotationItem.setQuotationId(resultSet.getInt("quotation_id"));
                quotationItem.setProductId(resultSet.getInt("product_id"));
                quotationItem.setQuantity(resultSet.getInt("quantity"));
                quotationItem.setPrice(resultSet.getDouble("price"));
                quotationItems.add(quotationItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quotationItems;
    }

    public List<QuotationItem> getQuotationItemsByQuotationId(int quotationId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
