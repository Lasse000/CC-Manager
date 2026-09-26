package parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class JsonImporter {

    private String targetFilePath = "C:/Users/lasse/Documents/CC-Manager Java/CC-Manager/data/AtomicCards.json";
    private ArrayList<Map<String, Object>> jsonData = new ArrayList<>();

    public JsonImporter(){

    }

    public void importJson() {
        try {
            String jsonString = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(targetFilePath)));
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
            org.json.JSONArray cardsArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < cardsArray.length(); i++) {
                org.json.JSONObject cardObject = cardsArray.getJSONObject(i);
                Map<String, Object> cardData = new java.util.Hashtable<>();
                for (Iterator it = cardObject.keys(); it.hasNext(); ) {
                    String key = it.next().toString();
                    cardData.put(key, cardObject.get(key));
                }
                jsonData.add(cardData);
            }
            System.out.println("JSON-Daten erfolgreich importiert. Anzahl der Karten: " + jsonData.size());
        } catch (java.io.IOException e) {
            System.err.println("Fehler beim Lesen der JSON-Datei: " + e.getMessage());
        } catch (org.json.JSONException e) {
            System.err.println("Fehler beim Parsen der JSON-Daten: " + e.getMessage());
        }
    }
}
