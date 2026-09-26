package model;

public class SetObject {

    private String code;         // z.B. "10E"
    private String name;         // z.B. "Tenth Edition"
    private int baseSetSize;     // z.B. 383
    private String block;        // z.B. "Core Set"
    private String releaseDate;  // z.B. "2007-07-13"

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
}