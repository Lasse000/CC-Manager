package dao;

import model.JSONCardObject;
import parser.JSONParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsMDAO {

    DBConnection dbConnection = new DBConnection();
    private ArrayList<JSONCardObject> cardlist;

    public DetailsMDAO(ArrayList<JSONCardObject> cardlist) {
        this.cardlist = cardlist;
    }

    public ArrayList<JSONCardObject> getKartenListe() {
        JSONParser jsonParser = new JSONParser();
        this.cardlist = jsonParser.datenAuslesenJSONCardObject();
        return this.cardlist;
    }

    public Map<String, Integer> insertDetailsM(List<JSONCardObject> cardList) {
        Map<String, Integer> detailsMIdToMap = new HashMap<>();

        String sql = "INSERT INTO details_m (scryfall_id, type_line, oracle_text, cmc, power, toughness, color, color_identity, keywords, layout, defense, loyalty, produced_mana, artist, border_color, finishes, edhrec_rank, flavor_name, flavor_text, full_art, games, promo, promo_type, reprint, edhrecsaltiness) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (JSONCardObject card : cardList) {
                // 1. scryfall_id
                stmt.setString(1, card.getSryfallID());
                // 2. type_line
                stmt.setString(2, card.getTypeLine());
                // 3. oracle_text
                stmt.setString(3, card.getoracleText());

                // 4. cmc
                if (card.getConvertedManaCost() != null) {
                    stmt.setDouble(4, card.getConvertedManaCost());
                } else {
                    stmt.setNull(4, Types.DOUBLE);
                }

                // 5. power
                stmt.setString(5, card.getPower());
                // 6. toughness
                stmt.setString(6, card.getToughness());

                // Arrays (mit Hilfsmethode)
                setSqlArray(conn, stmt, 7, card.getColors());          // 7. color
                setSqlArray(conn, stmt, 8, card.getColorIdentity());  // 8. color_identity
                setSqlArray(conn, stmt, 9, card.getKeywords());       // 9. keywords

                // 10. layout
                stmt.setString(10, card.getLayout());

                // 11. defense
                if (card.getDefense() != null) {
                    stmt.setInt(11, card.getDefense());
                } else {
                    stmt.setNull(11, Types.INTEGER);
                }

                // 12. loyalty
                if (card.getLoyalty() != null) {
                    stmt.setInt(12, card.getLoyalty());
                } else {
                    stmt.setNull(12, Types.INTEGER);
                }

                // 13. produced_mana
                setSqlArray(conn, stmt, 13, card.getProducedMana());

                // 14. artist
                stmt.setString(14, card.getArtist());
                // 15. border_color
                stmt.setString(15, card.getBorder_color());

                // 16. finishes
                setSqlArray(conn, stmt, 16, card.getFinishes());

                // 17. edhrec_rank
                if (card.getEdhRank() != null) {
                    stmt.setInt(17, card.getEdhRank());
                } else {
                    stmt.setNull(17, Types.INTEGER);
                }

                // 18. flavor_name
                stmt.setString(18, card.getFlavorName());
                // 19. flavor_text
                stmt.setString(19, card.getFlavorText());
                // 20. full_art
                stmt.setBoolean(20, card.isFullArt());

                // 21. games
                setSqlArray(conn, stmt, 21, card.getGames());

                // 22. promo
                stmt.setBoolean(22, card.isPromo());

                // 23. promo_type (nicht in JSONCardObject enthalten -> null)
                stmt.setNull(23, Types.VARCHAR);

                // 24. reprint
                stmt.setBoolean(24, card.isReprint());

                // 25. edhrecsaltiness
                stmt.setString(25, card.getEdhRecSaltiness());

                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detailsMIdToMap;
    }

    /**
     * Hilfsmethode zum Konvertieren einer List<String> in ein PostgreSQL SQL-Array.
     */
    private void setSqlArray(Connection conn, PreparedStatement stmt, int parameterIndex, List<String> list) throws SQLException {
        if (list != null && !list.isEmpty()) {
            String[] array = list.toArray(new String[0]);
            java.sql.Array sqlArray = conn.createArrayOf("VARCHAR", array);
            stmt.setArray(parameterIndex, sqlArray);
        } else {
            stmt.setNull(parameterIndex, Types.ARRAY);
        }
    }
}