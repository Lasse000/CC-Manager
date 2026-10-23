package dao;

import model.JSONCardObject;
import model.SetObject;
import parser.JSONParser;
import parser.RawDataFetcher;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ImporterCoordinator {

    private final JSONParser jsonParser = new JSONParser();
    private final RawDataFetcher rawDataFetcher = new RawDataFetcher();

    public void startFullImport() throws SQLException {
        if (!jsonParser.fileExists()) {
            System.out.println("Rohdaten nicht gefunden. Starte Download via RawDataFetcher...");
            rawDataFetcher.fetchData(); // hier schauhen, ob die meta Daten aktuell sind
            rawDataFetcher.fetchpriceData();
        }
        importJSONToDB();
    }

    public void importJSONToDB() {
        long startTime = System.currentTimeMillis();

        System.out.println("Starte JSON-Parsing...");
        List<JSONCardObject> cardList = jsonParser.datenAuslesenJSONCardObject();
        List<SetObject> setList = jsonParser.getSetObjects();
        List<String> rarityList = jsonParser.getRarities();

        System.out.println("Geparst: " + cardList.size() + " Karten, " + setList.size() + " Sets, " + rarityList.size() + " Rarities.");

        SetDAO setDAO = new SetDAO();
        RaritiesDAO raritiesDAO = new RaritiesDAO();
        CardDAO cardDAO = new CardDAO();
        DetailsMDAO detailsMDAO = new DetailsMDAO();

        try {
            System.out.println("Speichere Sets und Rarities...");
            Map<String, Integer> setMap = setDAO.insertAllSets(setList);
            Map<String, Integer> rarityMap = raritiesDAO.insertAllRarities(rarityList);

            System.out.println("Speichere Karten in 'cards'...");
            Map<String, Integer> cardUuidToNrMap = cardDAO.insertCards(cardList, setMap, rarityMap);

            System.out.println("Speichere Kartendetails in 'details_m'...");
            detailsMDAO.insertDetailsM(cardList, cardUuidToNrMap);

            ForeignCardsDAO foreignCardsDAO = new ForeignCardsDAO();
// ...
            System.out.println("Speichere Übersetzungen in 'foreign_cards'...");
            foreignCardsDAO.insertForeignCards(cardList, cardUuidToNrMap);

            long endTime = System.currentTimeMillis();
            System.out.println("Import erfolgreich abgeschlossen in " + ((endTime - startTime) / 1000.0) + " Sekunden!");

        } catch (Exception e) {
            System.err.println("Fehler während des Datenbank-Imports: " + e.getMessage());
            e.printStackTrace();
        }
    }
}