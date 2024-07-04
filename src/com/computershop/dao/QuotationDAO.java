package com.computershop.dao;

import com.computershop.model.Quotation;
import com.computershop.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuotationDAO {
    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }
    ///////////////////////////////////////////////////////////////////////////////
    public int getLastInsertedId() {
        String sql = "SELECT LAST_INSERT_ID()";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    ////////////////////////////////////////////////////////////////////////////////////

    public List<Quotation> getAllQuotations() {
        List<Quotation> quotations = new ArrayList<>();
        String sql = "SELECT * FROM quotations";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Quotation quotation = new Quotation();
                quotation.setId(resultSet.getInt("id"));
                quotation.setCustomerId(resultSet.getInt("customer_id"));
                quotation.setTotal(resultSet.getDouble("total"));
                quotation.setDate(resultSet.getTimestamp("date"));
                quotations.add(quotation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quotations;
    }

    public void addQuotation(Quotation quotation) throws SQLException {
        String sql = "INSERT INTO quotations (customer_id, total) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quotation.getCustomerId());
            statement.setDouble(2, quotation.getTotal());
            statement.executeUpdate();
        }
    }

    public void updateQuotation(Quotation quotation) throws SQLException {
        String sql = "UPDATE quotations SET customer_id = ?, total = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quotation.getCustomerId());
            statement.setDouble(2, quotation.getTotal());
            statement.setInt(3, quotation.getId());
            statement.executeUpdate();
        }
    }

    public void deleteQuotation(int id) throws SQLException {
        String sql = "DELETE FROM quotations WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Quotation getQuotationById(int quotationId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
