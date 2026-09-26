package model;

public class ForeignCardData {

    private String language;     // z.B. "German", "Japanese"
    private String name;         // Name in der jeweiligen Sprache (z.B. "Ausgewählter der Ahnfrau")
    private String text;         // Übersetzter Kartentext
    private String type;         // Übersetzter Typen-Text
    private String flavorText;   // Übersetzter Flavour-Text
    private Integer multiverseId;// Offizielle Wizards-ID (falls vorhanden)
    private String uuid;         // Eindeutige UUID für den fremdsprachigen Druck

    public ForeignCardData() {}

    // Getter und Setter
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFlavorText() { return flavorText; }
    public void setFlavorText(String flavorText) { this.flavorText = flavorText; }

    public Integer getMultiverseId() { return multiverseId; }
    public void setMultiverseId(Integer multiverseId) { this.multiverseId = multiverseId; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
}