package model;

public class SetObject {

    private int baseSetSize;     // z.B. 383
    private String block;        // z.B. "Core Set"
    private String code;         // z.B. "10E"
    private String name;         // z.B. "Tenth Edition"
    private String releaseDate;  // z.B. "2007-07-13"
    private String scryfall_set_id;
    private int games_nr = 1;

    public SetObject() {}

    // Getter & Setter
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBaseSetSize() { return baseSetSize; }
    public void setBaseSetSize(int baseSetSize) { this.baseSetSize = baseSetSize; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getScryfall_set_id(){
        return this.scryfall_set_id;
    }

    public void setScryfall_set_id(String scryfallSetId){
        this.scryfall_set_id = scryfallSetId;
    }
    public int getGames_nr(){
        return this.games_nr;
    }

    public void setGames_nr(int gamesNr){
        this.games_nr = gamesNr;
    }


}