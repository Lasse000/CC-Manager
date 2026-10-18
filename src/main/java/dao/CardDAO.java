package dao;

import model.JSONCardObject;
import parser.JSONParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDAO {

    DBConnection dbConnection = new DBConnection();
    private ArrayList<JSONCardObject> cardlist;

    public CardDAO(ArrayList<JSONCardObject> cardlist){
        this.cardlist = cardlist;
    }

    public ArrayList<JSONCardObject> getKartenListe(){
        JSONParser jsonParser = new JSONParser();
        this.cardlist = jsonParser.datenAuslesenJSONCardObject();
        return this.cardlist;
    }

    public Map<String, Integer> insertCards(List<JSONCardObject> cardList) {
        // Speichert z.B. "10E" -> 42 (generierte ID aus der DB)
        Map<String, Integer> cardidToMap = new HashMap<>();

        // UPSERT-Statement: Fügt das Set ein oder ignoriert es, falls der Code schon existiert
        String sql = "INSERT INTO cards (name, cardmarket_id, set_number, sets_nr, rarities_nr) " +
                "VALUES (?, ?, ?, ?, ?) ";


        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (JSONCardObject card : cardList) {
                stmt.setString(1, card.getName());
                stmt.setString(2, card.getCardMarket_id());
                stmt.setString(3, card.getSetNumber());
                stmt.setString(4, ""); // noch korrigieren
                stmt.setInt(5, -1); // noch korrigieren

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt("cards_nr");
                        String code = rs.getString("uuid");
                        cardidToMap.put(code, generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardidToMap; // Gibt die Zuordnung Map zur Hauptklasse zurück!
    }



}
