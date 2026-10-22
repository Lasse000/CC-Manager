package model;

import java.util.List;

public class JSONCardObject {

    // Eindeutige IDs
    private String uuid;             // Eindeutige MTGJSON-UUID für diesen Druck
    // uuid muss noch in db hinzugefügt werden
    private String name;             // Englischer Kartenname (z.B. "Ancestor's Chosen")

    // Spielwerte & Eigenschaften
    private String manaCost;         // z.B. "{5}{W}{W}"
    private Double convertedManaCost;// z.B. 7.0
    private String type;             // z.B. "Creature — Human Cleric"
    private String oracleText;             // Englischer Regeltext
    private String flavorText;       // Flavour-Text
    private String rarity;           // z.B. "uncommon", "rare"
    private String artist;           // Name des Künstlers (z.B. "Pete Venters")
    private String setCode;          // Set-Code (z.B. "10E")
    private String cardMarket_id;
    private String setNumber;
    private String sryfallID;
    private String typeLine;
    private String power;
    private String toughness;
    private List<String> keywords;
    private String layout;
    private Integer defense;
    private Integer loyalty;
    private List<String> producedMana;
    private String border_color;
    private Integer edhRank;
    private String flavorName;
    private boolean fullArt;
    private List<String> games; //(MTG_Arena, MTG Online, Paper)
    private boolean promo;
    private boolean reprint;
    private String edhRecSaltiness;


    // Listen für Typen & Farben
    private List<String> colors;        // z.B. ["W"]
    private List<String> colorIdentity; // z.B. ["W"]
    private List<String> finishes;     // z.B. ["nonfoil", "foil"]

    // Unterobjekt für Sprachen
    private List<ForeignCardData> foreignData; // Enthält Deutsch, Japanisch etc.

    // Standard-Konstruktor
    public JSONCardObject() {}

    // Getter und Setter
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManaCost() { return manaCost; }
    public void setManaCost(String manaCost) { this.manaCost = manaCost; }

    public Double getConvertedManaCost() { return convertedManaCost; }
    public void setConvertedManaCost(Double convertedManaCost) { this.convertedManaCost = convertedManaCost; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getOracleText() { return oracleText; }
    public void setOracleText(String oracleText) { this.oracleText = oracleText; }

    public String getFlavorText() { return flavorText; }
    public void setFlavorText(String flavorText) { this.flavorText = flavorText; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getSetCode() { return setCode; }
    public void setSetCode(String setCode) { this.setCode = setCode; }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }

    public List<String> getColorIdentity(){
        return this.colorIdentity;
    }

    public void setColorIdentity(List<String> colorIdentity){
        this.colorIdentity = colorIdentity;
    }

    public List<String> getFinishes(){
        return this.finishes;
    }

    public void setFinishes(List<String> finishes){
        this.finishes = finishes;
    }

    public List<ForeignCardData> getForeignData() { return foreignData; }
    public void setForeignData(List<ForeignCardData> foreignData) { this.foreignData = foreignData; }

    public String getRarity() { return rarity; }
    public void setRarity(String rarity) { this.rarity = rarity; }

    public String getCardMarket_id() {
        return cardMarket_id;
    }

    public void setCardMarket_id(String cardMarket_id) {
        this.cardMarket_id = cardMarket_id;
    }

    public String getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(String setNumber) {
        this.setNumber = setNumber;
    }

    public String getSryfallID() {
        return sryfallID;
    }

    public void setSryfallID(String sryfallID) {
        this.sryfallID = sryfallID;
    }

    public String getTypeLine() {
        return typeLine;
    }

    public void setTypeLine(String typeLine) {
        this.typeLine = typeLine;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Integer loyalty) {
        this.loyalty = loyalty;
    }

    public List<String> getProducedMana() {
        return producedMana;
    }

    public void setProducedMana(List<String> producedMana) {
        this.producedMana = producedMana;
    }

    public String getBorder_color() {
        return border_color;
    }

    public void setBorder_color(String border_color) {
        this.border_color = border_color;
    }

    public Integer getEdhRank() {
        return edhRank;
    }

    public void setEdhRank(Integer edhRank) {
        this.edhRank = edhRank;
    }

    public String getFlavorName() {
        return flavorName;
    }

    public void setFlavorName(String flavorName) {
        this.flavorName = flavorName;
    }

    public boolean isFullArt() {
        return fullArt;
    }

    public void setFullArt(boolean fullArt) {
        this.fullArt = fullArt;
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public boolean isPromo() {
        return promo;
    }

    public void setPromo(boolean promo) {
        this.promo = promo;
    }

    public boolean isReprint() {
        return reprint;
    }

    public void setReprint(boolean reprint) {
        this.reprint = reprint;
    }

    public String getEdhRecSaltiness() {
        return edhRecSaltiness;
    }

    public void setEdhRecSaltiness(String edhRecSaltiness) {
        this.edhRecSaltiness = edhRecSaltiness;
    }

    @Override
    public String toString() {
        return "CardObject{" +
                "name='" + name + '\'' +
                ", setCode='" + setCode + '\'' +
                ", foreignLanguages=" + (foreignData != null ? foreignData.size() : 0) +
                '}';
    }

    public boolean checkForeignCards(){
        return true;
    }
}