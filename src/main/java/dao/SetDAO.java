package dao;

import model.JSONCardObject;
import model.SetObject;
import parser.JSONParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetDAO {

    DBConnection dbConnection = new DBConnection();
    JSONParser jsonParser = new JSONParser();
    private ArrayList<SetObject> setobjects = new ArrayList<>();

    public SetDAO() {

        this.setobjects = jsonParser.getSetObjects();
    }

    public Map<String, Integer> insertAllSets(List<SetObject> setList) {
        // Speichert z.B. "10E" -> 42 (generierte ID aus der DB)
        Map<String, Integer> setCodeToIdMap = new HashMap<>();

        // UPSERT-Statement: Fügt das Set ein oder ignoriert es, falls der Code schon existiert
        String sql = "INSERT INTO public.sets (set_name, released_at, abbreviation, scryfall_set_id, games_nr) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (abbreviation) DO UPDATE SET set_name = EXCLUDED.set_name " +
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

        return setCodeToIdMap; // Gibt die Zuordnung Map zur Hauptklasse zurück!
    }
}
