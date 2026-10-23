package dao;

import model.ForeignCardData;
import model.JSONCardObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class ForeignCardsDAO {

    private final DBConnection dbConnection = new DBConnection();

    public ForeignCardsDAO() {
    }

    public void insertForeignCards(List<JSONCardObject> cardList, Map<String, Integer> cardUuidToNrMap) {

        String sql = "INSERT INTO foreign_cards (cards_nr, language, name, text, type_line, flavor_text, multiverse_id, foreign_uuid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (foreign_uuid) DO UPDATE SET " +
                "name = EXCLUDED.name, " +
                "text = EXCLUDED.text, " +
                "type_line = EXCLUDED.type_line, " +
                "flavor_text = EXCLUDED.flavor_text, " +
                "multiverse_id = EXCLUDED.multiverse_id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (JSONCardObject card : cardList) {

                Integer cardsNr = cardUuidToNrMap.get(card.getUuid());
                if (cardsNr == null || card.getForeignData() == null || card.getForeignData().isEmpty()) {
                    continue;
                }

                for (ForeignCardData foreignData : card.getForeignData()) {

                    // Parameter 1: Foreign Key zu cards (cards_nr)
                    stmt.setInt(1, cardsNr);

                    // Parameter 2: Sprache (z.B. "German", "Japanese")
                    stmt.setString(2, foreignData.getLanguage());

                    // Parameter 3: Übersetzter Name
                    stmt.setString(3, foreignData.getName());

                    // Parameter 4: Übersetzter Kartentext (Oracle Text)
                    stmt.setString(4, foreignData.getText());

                    // Parameter 5: Übersetzter Typus
                    stmt.setString(5, foreignData.getType());

                    // Parameter 6: Übersetzter Flavor Text
                    stmt.setString(6, foreignData.getFlavorText());

                    // Parameter 7: Multiverse ID (kann NULL sein)
                    if (foreignData.getMultiverseId() != null) {
                        stmt.setInt(7, foreignData.getMultiverseId());
                    } else {
                        stmt.setNull(7, Types.INTEGER);
                    }

                    // Parameter 8: Eindeutige UUID der Übersetzung (z.B. für ON CONFLICT)
                    stmt.setString(8, foreignData.getUuid());

                    stmt.addBatch();
                }
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}