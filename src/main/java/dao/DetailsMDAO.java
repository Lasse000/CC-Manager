package dao;

import model.JSONCardObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

public class DetailsMDAO {

    private final DBConnection dbConnection = new DBConnection();

    public DetailsMDAO() {
    }

    public void insertDetailsM(List<JSONCardObject> cardList, Map<String, Integer> cardUuidToNrMap) {

        String sql = "INSERT INTO details_m (cards_nr, scryfall_id, type_line, oracle_text, cmc, power, toughness, " +
                "color, color_identity, keywords, layout, defense, loyalty, produced_mana, artist, " +
                "border_color, finishes, edhrec_rank, flavor_name, flavor_text, full_art, games, promo, " +
                "promo_type, reprint, edhrecsaltiness) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (cards_nr) DO UPDATE SET " +
                "oracle_text = EXCLUDED.oracle_text, " +
                "type_line = EXCLUDED.type_line, " +
                "cmc = EXCLUDED.cmc, " +
                "artist = EXCLUDED.artist, " +
                "edhrec_rank = EXCLUDED.edhrec_rank";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (JSONCardObject card : cardList) {

                Integer cardsNr = cardUuidToNrMap.get(card.getUuid());
                if (cardsNr == null) {
                    continue;
                }

                stmt.setInt(1, cardsNr);
                stmt.setString(2, card.getSryfallID());
                stmt.setString(3, card.getTypeLine());
                stmt.setString(4, card.getOracleText());

                if (card.getConvertedManaCost() != null) {
                    stmt.setDouble(5, card.getConvertedManaCost());
                } else {
                    stmt.setNull(5, Types.DOUBLE);
                }

                stmt.setString(6, card.getPower());
                stmt.setString(7, card.getToughness());

                setSqlArray(conn, stmt, 8, card.getColors(), "VARCHAR");
                setSqlArray(conn, stmt, 9, card.getColorIdentity(), "VARCHAR");
                setSqlArray(conn, stmt, 10, card.getKeywords(), "VARCHAR");

                if (card.getLayout() != null) {
                    stmt.setString(11, card.getLayout());
                } else {
                    stmt.setNull(11, Types.OTHER);
                }

                if (card.getDefense() != null) {
                    stmt.setInt(12, card.getDefense());
                } else {
                    stmt.setNull(12, Types.INTEGER);
                }

                if (card.getLoyalty() != null) {
                    stmt.setInt(13, card.getLoyalty());
                } else {
                    stmt.setNull(13, Types.INTEGER);
                }

                setSqlArray(conn, stmt, 14, card.getProducedMana(), "VARCHAR");
                stmt.setString(15, card.getArtist());
                if (card.getBorder_color() != null) {
                    stmt.setString(16, card.getBorder_color());
                } else {
                    stmt.setNull(16, Types.OTHER);
                }
                setSqlArray(conn, stmt, 17, card.getFinishes(), "VARCHAR");
                if (card.getEdhRank() != null) {
                    stmt.setInt(18, card.getEdhRank());
                } else {
                    stmt.setNull(18, Types.INTEGER);
                }

                stmt.setString(19, card.getFlavorName());
                stmt.setString(20, card.getFlavorText());
                stmt.setBoolean(21, card.isFullArt());

// 22. games (Typ in DB jetzt VARCHAR[])
                setSqlArray(conn, stmt, 22, card.getGames(), "VARCHAR");
                stmt.setBoolean(23, card.isPromo());
                stmt.setNull(24, Types.VARCHAR);
                stmt.setBoolean(25, card.isReprint());
                // 26. edhrecsaltiness (Konvertierung von String zu Float/Double mit NULL-Check)
                if (card.getEdhRecSaltiness() != null && !card.getEdhRecSaltiness().isBlank()) {
                    try {
                        stmt.setFloat(26, Float.parseFloat(card.getEdhRecSaltiness()));
                    } catch (NumberFormatException e) {
                        stmt.setNull(26, Types.FLOAT);
                    }
                } else {
                    stmt.setNull(26, Types.FLOAT);
                }

                stmt.addBatch();
            }

            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSqlArray(Connection conn, PreparedStatement stmt, int parameterIndex, List<String> list) throws SQLException {
        if (list != null && !list.isEmpty()) {
            String[] array = list.toArray(new String[0]);
            java.sql.Array sqlArray = conn.createArrayOf("VARCHAR", array);
            stmt.setArray(parameterIndex, sqlArray);
        } else {
            stmt.setNull(parameterIndex, Types.ARRAY);
        }
    }

    private void setSqlArray(Connection conn, PreparedStatement stmt, int parameterIndex, List<String> list, String typeName) throws SQLException {
        if (list != null && !list.isEmpty()) {
            String[] array = list.toArray(new String[0]);
            // Übergibt den exakten DB-Typnamen (z.B. "color" oder "VARCHAR")
            java.sql.Array sqlArray = conn.createArrayOf(typeName, array);
            stmt.setArray(parameterIndex, sqlArray);
        } else {
            stmt.setNull(parameterIndex, Types.ARRAY);
        }
    }
}