package dao;

import model.JSONCardObject;
import model.PriceObjectModel;
import model.SetObject;
import org.json.JSONObject;
import parser.JSONParser;
import parser.RawDataFetcher;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ImporterCoordinator {

    private final JSONParser jsonParser = new JSONParser();
    private final RawDataFetcher rawDataFetcher = new RawDataFetcher();
    private final MetaDAO metaDAO = new MetaDAO();

    public void startFullImport() throws SQLException {
        boolean stammdatenImportNoetig = false;

        // 1. Meta-Check für Stammdaten (AllPrintings.json)
        String localVersion = metaDAO.getMetaValue("mtgjson_version_date");
        String onlineVersion = rawDataFetcher.fetchOnlineMetaDate();

        System.out.println("Lokaler Stammdaten-Stand: " + (localVersion != null ? localVersion : "Keiner"));
        System.out.println("Online Stammdaten-Stand:  " + (onlineVersion != null ? onlineVersion : "Unbekannt"));

        if (!jsonParser.fileExists() || localVersion == null || (onlineVersion != null && !onlineVersion.equals(localVersion))) {
            System.out.println("Neuer Stammdaten-Stand verfügbar oder Datei fehlt. Starte Download der AllPrintings.json...");
            rawDataFetcher.fetchData();
            stammdatenImportNoetig = true;
        } else {
            System.out.println("Stammdaten sind aktuell. Stammdaten-Import wird übersprungen.");
        }

        // 2. Tägliche Preise immer frisch herunterladen
        System.out.println("Lade tagesaktuelle Preise...");
        rawDataFetcher.fetchpriceData();

        // 3. Import durchführen
        importJSONToDB(stammdatenImportNoetig, onlineVersion);
    }

    public void importJSONToDB(boolean importStammdaten, String newVersionDate) {
        long startTime = System.currentTimeMillis();

        CardDAO cardDAO = new CardDAO();
        Map<String, Integer> cardUuidToNrMap;

        // Falls die Stammdaten aktuell sind, müssen wir die 1,5 GB AllPrintings.json nicht neu parsen!
        if (importStammdaten) {
            System.out.println("Starte JSON-Parsing für Stammdaten...");
            List<JSONCardObject> cardList = jsonParser.datenAuslesenJSONCardObject();
            List<SetObject> setList = jsonParser.getSetObjects();
            List<String> rarityList = jsonParser.getRarities();

            System.out.println("Geparst: " + cardList.size() + " Karten, " + setList.size() + " Sets, " + rarityList.size() + " Rarities.");

            SetDAO setDAO = new SetDAO();
            RaritiesDAO raritiesDAO = new RaritiesDAO();
            DetailsMDAO detailsMDAO = new DetailsMDAO();
            ForeignCardsDAO foreignCardsDAO = new ForeignCardsDAO();

            try {
                System.out.println("Speichere Sets und Rarities...");
                Map<String, Integer> setMap = setDAO.insertAllSets(setList);
                Map<String, Integer> rarityMap = raritiesDAO.insertAllRarities(rarityList);

                System.out.println("Speichere Karten in 'cards'...");
                cardUuidToNrMap = cardDAO.insertCards(cardList, setMap, rarityMap);

                System.out.println("Speichere Kartendetails in 'details_m'...");
                detailsMDAO.insertDetailsM(cardList, cardUuidToNrMap);

                System.out.println("Speichere Übersetzungen in 'foreign_cards'...");
                foreignCardsDAO.insertForeignCards(cardList, cardUuidToNrMap);

                // Nach erfolgreichem Stammdaten-Import die neue Version in DB festhalten
                if (newVersionDate != null) {
                    metaDAO.updateMetaValue("mtgjson_version_date", newVersionDate);
                }

            } catch (Exception e) {
                System.err.println("Fehler beim Stammdaten-Import: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } else {
            // Stammdaten-Import wird übersprungen -> UUID-Mapping direkt aus der DB laden!
            System.out.println("Lade bestehende Karten-UUIDs aus der Datenbank...");
            cardUuidToNrMap = cardDAO.getAllCardUuidsToNr();
        }

        // --- PREIS-IMPORT (Läuft JEDES Mal) ---
        try {
            System.out.println("Parse und speichere die täglichen Preisdaten...");
            String priceFilePath = "src/test/resources/AllPricesToday.json";
            File priceFile = new File(priceFilePath);

            if (priceFile.exists()) {
                String priceContent = new String(Files.readAllBytes(Paths.get(priceFilePath)));
                JSONObject priceJsonRoot = new JSONObject(priceContent);

                List<PriceObjectModel> priceObjects = jsonParser.parseDailyPrices(priceJsonRoot, cardUuidToNrMap);
                System.out.println("Geparst: " + priceObjects.size() + " Preiseinträge.");

                PriceDAO priceDAO = new PriceDAO();
                priceDAO.insertPrices(priceObjects);
                System.out.println("Tagespreise erfolgreich in 'price' gespeichert.");
            } else {
                System.err.println("Preis-Datei unter " + priceFilePath + " nicht gefunden.");
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Import-Vorgang erfolgreich abgeschlossen in " + ((endTime - startTime) / 1000.0) + " Sekunden!");

        } catch (Exception e) {
            System.err.println("Fehler beim Preis-Import: " + e.getMessage());
            e.printStackTrace();
        }
    }
}