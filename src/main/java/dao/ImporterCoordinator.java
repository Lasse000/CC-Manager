package dao;

import model.JSONCardObject;
import parser.JSONParser;
import parser.RawDataFetcher;

import java.sql.SQLException;
import java.util.ArrayList;

public class ImporterCoordinator {
JSONParser jsonParser = new JSONParser();
RawDataFetcher rawDataFetcher = new RawDataFetcher();
private ArrayList<JSONCardObject> cardlist;
CardDAO cardDAO = new CardDAO(this.cardlist);
    private int magic_Game_Nr = 1;

    public ImporterCoordinator(){
        this.cardlist = jsonParser.datenAuslesenJSONCardObject();
    }

    private void startFullImport() throws SQLException { // kompletter Ablauf koordiniert
        if (!jsonParser.FileExists()){ // überprüfen, ob die RohDaten existieren
            rawDataFetcher.fetchData();
        }
        else{
            importJSONToDB();
        }
    }

    public void importJSONToDB(){
        long startTime = System.currentTimeMillis();
        int cardCount = 0;
        try{
            cardDAO.prepareBatch();
        }
        catch (Exception e){
            System.out.println(e);
        }

        for(JSONCardObject jsonCardObject : this.cardlist){
            if(jsonCardObject.getRarity() != null){ // falls es eine vollständige Karte ist, also sie mindestens eine Rarity hat

                /*
                eventuell eine Methode, um für jedes

                String uuid = jsonCardObject.getUuid();
                String cardName = jsonCardObject.getName();
                String manaCost = jsonCardObject.getManaCost();
                String type = jsonCardObject.getType();
                String text = jsonCardObject.getText();
                String flavorText = jsonCardObject.getFlavorText();
                String artist = jsonCardObject.getArtist();
                String rarity = jsonCardObject.getRarity();
                double cmc = jsonCardObject.getConvertedManaCost();

                if (jsonCardObject.checkForeignCards()) {

                }

                 */


            }
        }

    }


}
