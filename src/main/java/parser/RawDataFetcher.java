package parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RawDataFetcher {

    private String url;
    private String targetFilePath;

    public RawDataFetcher() {
        // Link zu allen Kartendrucken inkl. Fremdsprachen (foreignData)
        this.url = "https://mtgjson.com/api/v5/AllPrintings.json";

        // Relativer Pfad im Projekt (erzeugt data/AllPrintings.json automatisch)
        this.targetFilePath = "src/test/resources/AllPrintings.json";
    }

    public void fetchData() {
        try {
            Path destination = Paths.get(targetFilePath);

            // Stellt sicher, dass der Ordner "data" existiert
            if (destination.getParent() != null) {
                Files.createDirectories(destination.getParent());
            }

            System.out.println("Lade AllPrintings.json herunter (kann kurz dauern)...");

            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Daten erfolgreich heruntergeladen und gespeichert unter: " + destination.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Herunterladen der Daten: " + e.getMessage());
        }
    }
}