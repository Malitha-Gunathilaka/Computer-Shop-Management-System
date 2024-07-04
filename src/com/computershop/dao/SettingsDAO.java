package com.computershop.dao;

import com.computershop.model.Settings;
import com.computershop.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDAO {

    public Settings getSettings() throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM settings LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Settings settings = new Settings();
                settings.setTaxRate(rs.getDouble("tax_rate"));
                settings.setDiscountRate(rs.getDouble("discount_rate"));
                return settings;
            }
            return null;
        }
    }

    public void saveSettings(Settings settings) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO settings (tax_rate, discount_rate) VALUES (?, ?) " +
                           "ON DUPLICATE KEY UPDATE tax_rate = VALUES(tax_rate), discount_rate = VALUES(discount_rate)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, settings.getTaxRate());
            stmt.setDouble(2, settings.getDiscountRate());
            stmt.executeUpdate();
        }
    }
}
