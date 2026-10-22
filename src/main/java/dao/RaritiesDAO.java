package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaritiesDAO {

    private final DBConnection dbConnection = new DBConnection();

    public RaritiesDAO() {
    }

    public Map<String, Integer> insertAllRarities(List<String> rarityList) {
        Map<String, Integer> rarityNameToIdMap = new HashMap<>();

        String sql = "INSERT INTO rarities (rarity_name, games_nr) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (rarity_name, games_nr) DO UPDATE SET rarity_name = EXCLUDED.rarity_name " +
                "RETURNING rarities_nr, rarity_name";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String rarity : rarityList) {
                if (rarity == null || rarity.isBlank()) {
                    continue;
                }

                stmt.setString(1, rarity.toLowerCase());
                stmt.setInt(2, 1); // Default Magic: The Gathering

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt("rarities_nr");
                        String name = rs.getString("rarity_name");
                        rarityNameToIdMap.put(name, generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rarityNameToIdMap;
    }
}