package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User_CardsDAO {

    private final DBConnection dbConnection = new DBConnection();

    public User_CardsDAO() {}

    public void addCard(int userId, int cardsNr, String cardStatus, String language, String finish, String condition, int buyingPrice) {
        String sql = "INSERT INTO user_cards (cards_nr, user_nr, card_status, language, finish, condition, buying_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, cardsNr);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, cardStatus);
            preparedStatement.setString(4, language);
            preparedStatement.setString(5, finish);
            preparedStatement.setString(6, condition);
            preparedStatement.setDouble(7, buyingPrice);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Hinzufügen der Benutzerkarte", e);
        }
    }

    public int getCardsNrWithSetNameSetNumber(String setName, String setNumber) throws SQLException {

        String sql = "SELECT cards_nr FROM cards WHERE set_name = ? AND set_number = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, setName);
            preparedStatement.setString(2, setNumber);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cards_nr");
                } else {
                    throw new SQLException("Keine Karte gefunden für Set: " + setName + " und Nummer: " + setNumber);
                }
            }
        }
    }

    public void updateUserCard(int userCardsNR) throws SQLException {
        String sql = "UPDATE user_cards Set card_status = 'Sold' WHERE cards_nr = ?";

        try(Connection conn = dbConnection.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            preparedStatement.setInt(1, userCardsNR);
            preparedStatement.executeUpdate();
        }
    }

    public void updateUserCard(int userCardsNR, String cardStatus) throws SQLException {}
}
