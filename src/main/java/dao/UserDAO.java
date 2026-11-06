package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    /**
     * Sucht anhand der google_id nach einem bestehenden Nutzer.
     * Erstellt einen neuen Eintrag, falls die google_id noch nicht existiert.
     *
     * @return Die interne Datenbank-ID (user_id) des Nutzers
     */
    DBConnection dbConnection = new DBConnection();
    public int findOrCreateUser(String googleId, String email, String firstName, String lastName, String pictureUrl) throws SQLException {

        // 1. Prüfen, ob der User bereits existiert
        String selectSql = "SELECT user_id FROM users WHERE google_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setString(1, googleId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }

        // 2. Falls neu: Mit allen Feldern deiner Datenbank-Struktur einfügen
        String insertSql = "INSERT INTO users (google_id, email, first_name, last_name, profile_picture_url) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, googleId);
            insertStmt.setString(2, email);
            insertStmt.setString(3, firstName);
            insertStmt.setString(4, lastName);
            insertStmt.setString(5, pictureUrl);

            int affectedRows = insertStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Erstellen des Benutzers fehlgeschlagen, keine Zeilen betroffen.");
            }

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Erstellen des Benutzers fehlgeschlagen, keine ID erhalten.");
                }
            }
        }
    }
}