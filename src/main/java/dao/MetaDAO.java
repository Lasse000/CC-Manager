package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetaDAO {

    private final DBConnection dbConnection = new DBConnection();

    public MetaDAO() {
    }

    public String getMetaValue(String key) {
        String sql = "SELECT value FROM db_metadata WHERE key = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMetaValue(String key, String value) {
        String sql = "INSERT INTO db_metadata (key, value, updated_at) VALUES (?, ?, NOW()) " +
                "ON CONFLICT (key) DO UPDATE SET value = EXCLUDED.value, updated_at = NOW()";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}