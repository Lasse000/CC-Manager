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

public class RaritiesDAO {

    DBConnection dbConnection = new DBConnection();
    private ArrayList<String> rarities = new ArrayList<>();
    JSONParser jsonParser = new JSONParser();

    public RaritiesDAO(ArrayList<JSONCardObject> cardlist) {
        this.rarities = jsonParser.getRarities();
    }


    public Map<String, Integer> insertAllSets(List<SetObject> setList) {
        // Speichert z.B. "10E" -> 42 (generierte ID aus der DB)
        Map<String, Integer> setCodeToIdMap = new HashMap<>();

        // UPSERT-Statement: Fügt das Set ein oder ignoriert es, falls der Code schon existiert
        String sql = "INSERT INTO public.sets (rarity_name, games_nr) " +
                "VALUES (?, ?) ";


        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String rarity : this.rarities) {
                stmt.setString(1, rarity);
                stmt.setInt(2, 1);


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
