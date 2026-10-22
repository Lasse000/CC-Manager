import model.ForeignCardData;
import model.JSONCardObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parser.JSONParser;

import java.io.File;
import java.util.ArrayList;

public class JSONParserTest {

    private JSONParser parser;

    @Before
    public void setUp() {
        parser = new JSONParser();
    }
/*
    @Test
    public void testDatenAuslesenUmfangUndStammdaten() {
        // Act: Gesamte Liste einlesen
        ArrayList<JSONCardObject> kartenListe = parser.datenAuslesenJSONCardObject();

        // Assert: Prüfen, ob die Liste genau 1 Karte enthält und die Stammdaten stimmen
        Assert.assertNotNull("Die Ergebnisliste darf nicht null sein", kartenListe);
        Assert.assertEquals("Es sollte genau 1 Karte in der Test-Datei sein", 1, kartenListe.size());

        JSONCardObject card = kartenListe.get(0);
        Assert.assertEquals("Ancestor's Chosen", card.getName());
        Assert.assertEquals("{5}{W}{W}", card.getManaCost());
        Assert.assertEquals("10E", card.getSetCode()); // SetID / setCode korrekt gesetzt?
    }

    @Test
    public void testForeignDataInCardObject() {
        // Act
        ArrayList<JSONCardObject> kartenListe = parser.datenAuslesenJSONCardObject();
        JSONCardObject card = kartenListe.get(0);

        // Assert: Prüfen, ob die Liste der Foreign Cards im JSONCardObject gefüllt ist
        ArrayList<ForeignCardData> foreignList = (ArrayList<ForeignCardData>) card.getForeignData();

        Assert.assertNotNull("Die Foreign-Data-Liste darf nicht null sein", foreignList);
        Assert.assertEquals("Es sollten 3 Übersetzungen enthalten sein", 3, foreignList.size());

        // Gezielt die deutsche Übersetzung herausfiltern und prüfen
        ForeignCardData germanData = foreignList.stream()
                .filter(f -> "German".equalsIgnoreCase(f.getLanguage()))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull("Die deutsche Übersetzung muss vorhanden sein", germanData);
        Assert.assertEquals("Ausgewählter der Ahnfrau", germanData.getName());
    }

 */
}