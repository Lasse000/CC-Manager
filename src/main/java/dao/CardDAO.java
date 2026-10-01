package dao;

import model.JSONCardObject;
import parser.JSONParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

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

    public void insertCard(ArrayList<JSONCardObject>){
        String sql;

        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }



}
