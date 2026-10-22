package dao;

import model.SetObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetDAO {

    private final DBConnection dbConnection = new DBConnection();

    public SetDAO() {
    }

    public Map<String, Integer> insertAllSets(List<SetObject> setList) {
        Map<String, Integer> setCodeToIdMap = new HashMap<>();

        String sql = "INSERT INTO sets (set_name, released_at, abbreviation, scryfall_set_id, games_nr) " +
                "VALUES (?, ?::date, ?, ?, ?) " +
                "ON CONFLICT (abbreviation) DO UPDATE SET " +
                "set_name = EXCLUDED.set_name, " +
                "released_at = EXCLUDED.released_at, " +
                "scryfall_set_id = EXCLUDED.scryfall_set_id " +
                "RETURNING set_nr, abbreviation";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (SetObject set : setList) {
                stmt.setString(1, set.getName());
                stmt.setString(2, set.getReleaseDate());
                stmt.setString(3, set.getCode());
                stmt.setString(4, set.getScryfall_set_id());
                stmt.setInt(5, 1);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt("set_nr");
                        String code = rs.getString("abbreviation");
                        setCodeToIdMap.put(code, generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return setCodeToIdMap;
    }
}