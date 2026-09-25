package model;

import java.util.List;

public class SetObject {

    private String code;         // z.B. "10E"
    private String name;         // z.B. "Tenth Edition"
    private Integer baseSetSize; // z.B. 383
    private String block;        // z.B. "Core Set"
    private List<JSONCardObject> cards; // Alle Karten in diesem Set

    public SetObject() {}

    // Getter und Setter...
}