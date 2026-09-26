package parser;

import model.ForeignCardData;
import model.JSONCardObject;
import model.SetObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class JSONParser {

    private final File rohDaten;
    private ArrayList<SetObject> setObjects = new ArrayList<>();

    public JSONParser(File testDatei) {
        this.rohDaten = new File("src/test/resources/testDatenSatz.json");
    }

    public ArrayList<JSONCardObject> datenAuslesenJSONCardObject() {
        ArrayList<JSONCardObject> jsonCardObjects = new ArrayList<>();

        if (!this.rohDaten.exists()) { // überprüfen ob die Datei existiert
            System.out.println("Die Datei existiert nicht: " + this.rohDaten.getAbsolutePath());
            return jsonCardObjects;
        }

        try { // überprüfen ob die Datei leer ist
            String inhalt = Files.readString(this.rohDaten.toPath(), StandardCharsets.UTF_8);
            if (inhalt.isBlank()) {
                return jsonCardObjects;
            }

            // Wurzel-Objekt einlesen
            JSONObject root = new JSONObject(inhalt);

            // "data" Objekt holen (enthält alle Sets)
            if (root.has("data")) {
                JSONObject dataObject = root.getJSONObject("data");

                // Durch alle Set-Codes iterieren (z.B. "10E")
                for (String setCode : dataObject.keySet()) {
                    JSONObject setJsonObject = dataObject.getJSONObject(setCode);
                    SetObject set = new SetObject();
                    set.setCode(setCode);
                    set.setName(setJsonObject.optString("name", null));
                    set.setBaseSetSize(setJsonObject.optInt("baseSetSize", 0));
                    set.setBlock(setJsonObject.optString("block", null));
                    set.setReleaseDate(setJsonObject.optString("releaseDate", null)); // falls im JSON vorhanden

                    setObjects.add(set);

                    if (setJsonObject.has("cards")) {
                        JSONArray cardsArray = setJsonObject.getJSONArray("cards");

                        // Durch alle Karten des Sets iterieren
                        for (int i = 0; i < cardsArray.length(); i++) {
                            JSONObject cardJson = cardsArray.getJSONObject(i);

                            // setCode als zweiten Parameter mitgeben!
                            JSONCardObject card = jsonArrayToJSONCardObject(cardJson, setCode);
                            if (card != null) {
                                jsonCardObjects.add(card);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonCardObjects;
    }



    // Die Methode nimmt jetzt zusätzlich den setCode an
    public JSONCardObject jsonArrayToJSONCardObject(JSONObject jo, String setCode) {
        JSONCardObject card = new JSONCardObject();

        // Pflichtfelder & sichere Abfragen (optString verhindert Abstürze bei fehlenden Feldern)
        card.setUuid(jo.optString("uuid", null));
        card.setName(jo.optString("name", null));
        card.setManaCost(jo.optString("manaCost", null));
        card.setType(jo.optString("type", null));
        card.setText(jo.optString("text", null));
        card.setFlavorText(jo.optString("flavorText", null)); // Achtung: MTGJSON nutzt meist "flavorText" (mit 'u'-loser US-Schreibweise)
        card.setArtist(jo.optString("artist", null));
        card.setRarity(jo.optString("rarity", null));
        card.setConvertedManaCost(jo.optDouble("convertedManaCost", 0.0));

        // Den übergebenen Set-Code direkt setzen!
        card.setSetCode(setCode);

        // --- Foreign Cards (Sprachen) auslesen ---
        if (jo.has("foreignData") && !jo.isNull("foreignData")) {
            JSONArray foreignArray = jo.getJSONArray("foreignData");
            ArrayList<ForeignCardData> foreignList = new ArrayList<>();

            for (int j = 0; j < foreignArray.length(); j++) {
                JSONObject fJo = foreignArray.getJSONObject(j);

                ForeignCardData foreignCard = new ForeignCardData();
                foreignCard.setLanguage(fJo.optString("language", null));
                foreignCard.setName(fJo.optString("name", null));
                foreignCard.setText(fJo.optString("text", null));
                foreignCard.setType(fJo.optString("type", null));
                foreignCard.setFlavorText(fJo.optString("flavorText", null));

                if (fJo.has("multiverseId") && !fJo.isNull("multiverseId")) {
                    foreignCard.setMultiverseId(fJo.getInt("multiverseId"));
                }

                foreignList.add(foreignCard);
            }

            // Die Fremdsprachen-Liste dem Kartenobjekt übergeben
            card.setForeignData(foreignList);
        }

        return card;
    }
}