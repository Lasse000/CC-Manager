package dao;

import model.PriceObjectModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PriceDAO {

    private final DBConnection dbConnection = new DBConnection();

    public PriceDAO() {
    }

    public void insertPrices(List<PriceObjectModel> priceList) {

        String sql = "INSERT INTO price (cards_nr, source, currency, finish, price_type, amount, price_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (cards_nr, source, currency, finish, price_type, price_date) DO UPDATE SET " +
                "amount = EXCLUDED.amount";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (PriceObjectModel price : priceList) {

                stmt.setInt(1, price.getCards_nr());
                stmt.setString(2, price.getSource());
                stmt.setString(3, price.getCurrency());
                stmt.setString(4, price.getFinish());
                stmt.setString(5, price.getPrice_type());
                stmt.setInt(6, price.getAmount());
                stmt.setDate(7, price.getPriceDate());

                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}