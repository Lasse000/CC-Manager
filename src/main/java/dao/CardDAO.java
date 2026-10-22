package dao;

import model.JSONCardObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDAO {

    private final DBConnection dbConnection = new DBConnection();

    public CardDAO() {
    }

    public Map<String, Integer> insertCards(List<JSONCardObject> cardList,
                                            Map<String, Integer> setMap,
                                            Map<String, Integer> rarityMap) {
        Map<String, Integer> uuidToCardsNrMap = new HashMap<>();

        String sql = "INSERT INTO cards (name, cardmarket_id, set_number, sets_nr, rarities_nr, uuid) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (uuid) DO UPDATE SET " +
                "name = EXCLUDED.name, " +
                "cardmarket_id = EXCLUDED.cardmarket_id, " +
                "set_number = EXCLUDED.set_number, " +
                "sets_nr = EXCLUDED.sets_nr, " +
                "rarities_nr = EXCLUDED.rarities_nr " +
                "RETURNING cards_nr, uuid";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (JSONCardObject card : cardList) {
                stmt.setString(1, card.getName());
                stmt.setString(2, card.getCardMarket_id());
                stmt.setString(3, card.getSetNumber());

                Integer setNr = card.getSetCode() != null ? setMap.get(card.getSetCode()) : null;
                Integer rarityNr = card.getRarity() != null ? rarityMap.get(card.getRarity().toLowerCase()) : null;

                if (setNr != null) {
                    stmt.setInt(4, setNr);
                } else {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                }

                if (rarityNr != null) {
                    stmt.setInt(5, rarityNr);
                } else {
                    stmt.setNull(5, java.sql.Types.INTEGER);
                }

                stmt.setString(6, card.getUuid());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt("cards_nr");
                        String uuid = rs.getString("uuid");
                        uuidToCardsNrMap.put(uuid, generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uuidToCardsNrMap;
    }
}