package parser;

import model.ForeignCardData;
import model.JSONCardObject;
import model.PriceObjectModel;
import model.SetObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONParser {

    private final File rohDaten = new File("src/test/resources/AllPrintings.json");
    private List<SetObject> setObjects = new ArrayList<>();
    private List<String> rarities = new ArrayList<>();

    public JSONParser() {
    }


    public List<JSONCardObject> datenAuslesenJSONCardObject() {
        List<JSONCardObject> jsonCardObjects = new ArrayList<>();

        if (!this.rohDaten.exists()) {
            System.out.println("Die Datei existiert nicht: " + this.rohDaten.getAbsolutePath());
            return jsonCardObjects;
        }

        // Streaming statt Files.readString()
        try (BufferedReader reader = new BufferedReader(new FileReader(this.rohDaten, StandardCharsets.UTF_8))) {

            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);

            if (root.has("data")) {
                JSONObject dataObject = root.getJSONObject("data");

                for (String setCode : dataObject.keySet()) {
                    JSONObject setJsonObject = dataObject.getJSONObject(setCode);

                    SetObject set = new SetObject();
                    set.setCode(setCode);
                    set.setName(setJsonObject.optString("name", null));
                    set.setBaseSetSize(setJsonObject.optInt("baseSetSize", 0));
                    set.setBlock(setJsonObject.optString("block", null));
                    set.setReleaseDate(setJsonObject.optString("releaseDate", null));
                    setObjects.add(set);

                    if (setJsonObject.has("cards")) {
                        JSONArray cardsArray = setJsonObject.getJSONArray("cards");

                        for (int i = 0; i < cardsArray.length(); i++) {
                            JSONObject cardJson = cardsArray.getJSONObject(i);
                            JSONCardObject card = jsonArrayToJSONCardObject(cardJson, setCode);
                            if (card != null) {
                                jsonCardObjects.add(card);

                                // Sammle Rarity für die RaritiesDAO
                                if (card.getRarity() != null && !rarities.contains(card.getRarity())) {
                                    rarities.add(card.getRarity());
                                }
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

    public JSONCardObject jsonArrayToJSONCardObject(JSONObject jo, String setCode) {
        JSONCardObject card = new JSONCardObject();

        card.setUuid(jo.optString("uuid", null));
        card.setName(jo.optString("name", null));
        card.setManaCost(jo.optString("manaCost", null));
        card.setTypeLine(jo.optString("type", null));
        card.setOracleText(jo.optString("text", null));
        card.setFlavorText(jo.optString("flavorText", null));
        card.setArtist(jo.optString("artist", null));
        card.setRarity(jo.optString("rarity", null));
        card.setSetNumber(jo.optString("number", null));
        card.setLayout(jo.optString("layout", null));
        card.setBorder_color(jo.optString("borderColor", null));
        card.setFlavorName(jo.optString("flavorName", null));

        if (jo.has("convertedManaCost") && !jo.isNull("convertedManaCost")) {
            card.setConvertedManaCost(jo.getDouble("convertedManaCost"));
        }

        if (jo.has("edhrecRank") && !jo.isNull("edhrecRank")) {
            card.setEdhRank(jo.getInt("edhrecRank"));
        }

        if (jo.has("edhrecSaltiness") && !jo.isNull("edhrecSaltiness")) {
            card.setEdhRecSaltiness(String.valueOf(jo.getDouble("edhrecSaltiness")));
        }

        card.setFullArt(jo.optBoolean("isFullArt", false));
        card.setPromo(jo.optBoolean("isPromo", false));
        card.setReprint(jo.optBoolean("isReprint", false));
        card.setSetCode(setCode);

        // Verschachtelte Identifiers
        if (jo.has("identifiers") && !jo.isNull("identifiers")) {
            JSONObject identifiers = jo.getJSONObject("identifiers");
            card.setCardMarket_id(identifiers.optString("mcmId", null));
            card.setSryfallID(identifiers.optString("scryfallId", null));
        }
        if (jo.has("foreignData") && !jo.isNull("foreignData")) {
            JSONArray fdArray = jo.getJSONArray("foreignData");
            List<ForeignCardData> foreignList = new ArrayList<>();

            for (int i = 0; i < fdArray.length(); i++) {
                JSONObject fdJson = fdArray.getJSONObject(i);
                ForeignCardData fd = new ForeignCardData();
                fd.setLanguage(fdJson.optString("language", null));
                fd.setName(fdJson.optString("name", null));
                fd.setText(fdJson.optString("text", null));
                fd.setType(fdJson.optString("type", null));
                fd.setFlavorText(fdJson.optString("flavorText", null));

                if (fdJson.has("multiverseId") && !fdJson.isNull("multiverseId")) {
                    fd.setMultiverseId(fdJson.getInt("multiverseId"));
                }
                fd.setUuid(fdJson.optString("uuid", null));

                foreignList.add(fd);
            }
            card.setForeignData(foreignList);
        }

        // String-Listen parsen
        card.setColors(parseStringList(jo, "colors"));
        card.setColorIdentity(parseStringList(jo, "colorIdentity"));
        card.setKeywords(parseStringList(jo, "keywords"));
        card.setProducedMana(parseStringList(jo, "producedMana"));
        card.setFinishes(parseStringList(jo, "finishes"));
        card.setGames(parseStringList(jo, "availability")); // In MTGJSON heisst das Array oft "availability"

        return card;
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

    public List<PriceObjectModel> parseDailyPrices(JSONObject rootJson, Map<String, Integer> uuidToCardsNrMap) {
        List<PriceObjectModel> priceList = new ArrayList<>();

        if (!rootJson.has("data")) {
            return priceList;
        }

        JSONObject dataObj = rootJson.getJSONObject("data");

        for (String uuid : dataObj.keySet()) {
            Integer cardsNr = uuidToCardsNrMap.get(uuid);
            if (cardsNr == null) {
                continue; // Karte existiert nicht in unserer DB
            }

            JSONObject cardPrices = dataObj.getJSONObject(uuid);

            if (!cardPrices.has("paper")) continue;
            JSONObject paperObj = cardPrices.getJSONObject("paper");

            for (String provider : paperObj.keySet()) {
                JSONObject providerObj = paperObj.getJSONObject(provider);
                String currency = providerObj.optString("currency", "EUR");

                if (!providerObj.has("retail")) continue;
                JSONObject retailObj = providerObj.getJSONObject("retail");

                for (String finish : retailObj.keySet()) {
                    JSONObject finishObj = retailObj.getJSONObject(finish);

                    for (String dateStr : finishObj.keySet()) {
                        double priceInUnits = finishObj.getDouble(dateStr);

                        int amountInCents = (int) Math.round(priceInUnits * 100);

                        PriceObjectModel priceObj = new PriceObjectModel();
                        priceObj.setCards_nr(cardsNr);
                        priceObj.setSource(provider);
                        priceObj.setCurrency(currency);
                        priceObj.setFinish(finish);
                        priceObj.setPrice_type("retail");
                        priceObj.setAmount(amountInCents);

                        try {
                            priceObj.setPriceDate(java.sql.Date.valueOf(dateStr));
                        } catch (IllegalArgumentException e) {
                            priceObj.setPriceDate(new java.sql.Date(System.currentTimeMillis()));
                        }

                        priceList.add(priceObj);
                    }
                }
            }
        }

        return priceList;
    }

    public List<SetObject> getSetObjects() {
        return setObjects;
    }

    public List<String> getRarities() {
        return rarities;
    }

    public boolean fileExists() {
        return this.rohDaten != null && this.rohDaten.exists();
    }
}