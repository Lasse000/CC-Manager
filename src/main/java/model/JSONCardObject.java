package model;

import java.util.List;

public class JSONCardObject {

    // Eindeutige IDs
    private String uuid;             // Eindeutige MTGJSON-UUID für diesen Druck
    private String name;             // Englischer Kartenname (z.B. "Ancestor's Chosen")

    // Spielwerte & Eigenschaften
    private String manaCost;         // z.B. "{5}{W}{W}"
    private Double convertedManaCost;// z.B. 7.0
    private String type;             // z.B. "Creature — Human Cleric"
    private String text;             // Englischer Regeltext
    private String flavorText;       // Flavour-Text
    private String rarity;           // z.B. "uncommon", "rare"
    private String artist;           // Name des Künstlers (z.B. "Pete Venters")
    private String setCode;          // Set-Code (z.B. "10E")
    private String number;           // Sammlernummer im Set

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

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getFlavorText() { return flavorText; }
    public void setFlavorText(String flavorText) { this.flavorText = flavorText; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getSetCode() { return setCode; }
    public void setSetCode(String setCode) { this.setCode = setCode; }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }

    public List<ForeignCardData> getForeignData() { return foreignData; }
    public void setForeignData(List<ForeignCardData> foreignData) { this.foreignData = foreignData; }

    public String getRarity() { return rarity; }
    public void setRarity(String rarity) { this.rarity = rarity; }


    @Override
    public String toString() {
        return "CardObject{" +
                "name='" + name + '\'' +
                ", setCode='" + setCode + '\'' +
                ", foreignLanguages=" + (foreignData != null ? foreignData.size() : 0) +
                '}';
    }
}