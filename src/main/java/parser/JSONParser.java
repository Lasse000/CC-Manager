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
import java.util.List;

public class JSONParser {

    private final File rohDaten = new File("src/test/resources/testDatenSatz.json");
    private ArrayList<SetObject> setObjects = new ArrayList<>();

    public JSONParser() {
    }

    public ArrayList<SetObject> getSetObjects(){
        return this.setObjects;
    }

    public ArrayList<String> getRarities(){
        ArrayList<String> raritiesList = new ArrayList<>();
        for (JSONCardObject card : datenAuslesenJSONCardObject()){
            for(String rarity : raritiesList){
                if(!card.getRarity().equals(rarity)){
                    raritiesList.add(card.getRarity());
                }
            }
        }
        return raritiesList;
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
                    set.setReleaseDate(setJsonObject.optString("releaseDate", null)); // falls im JSON vorhandents.add(set);

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
        card.setSetCode(setCode);
        card.setCardMarket_id(jo.optString("cardmarket_id", null));
        card.setSetNumber(jo.optString("number", null));
        card.setSryfallID(jo.optString("scryfallId", null));
        card.setTypeLine(jo.optString("typeLine", null));
        card.setPower(jo.optString("power", null));
        card.setToughness(jo.optString("toughness", null));
        card.setKeywords(parseStringList(jo, "keywords"));
        card.setLayout(jo.optString("layout", null));
        card.setDefense(jo.optIntegerObject("defense", null));
        card.setDefense(jo.optIntegerObject("loyalty", null));
        card.setProducedMana(parseStringList(jo, "producedMana"));
        card.setBorder_color(jo.optString("borderColor", null));
        card.setEdhRank(jo.optIntegerObject("edhrecRank"));
        card.setFlavorName(jo.optString("flavorName", null));
        card.setFullArt(jo.optBoolean("fullArt"));
        card.setGames(parseStringList(jo, "games"));
        card.setPromo(jo.optBoolean("promo"));
        card.setReprint(jo.optBoolean("reprint"));
        card.setEdhRecSaltiness(jo.optString("edhRecSaltiness"));



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

    public boolean fileExists() {
        return this.rohDaten != null && this.rohDaten.exists();
    }

    private List<String> parseStringList(JSONObject jo, String key) {
        if (jo.has(key) && !jo.isNull(key)) {
            JSONArray array = jo.getJSONArray(key);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
            return list;
        }
        return null;
    }
}