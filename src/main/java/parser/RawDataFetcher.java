package parser;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RawDataFetcher {

    private String url;
    private String targetFilePath;
    private String priceUrl;
    private String priceTargetFilePath;

    public RawDataFetcher() {
        // Link zu allen Kartendrucken inkl. Fremdsprachen (foreignData)
        this.url = "https://mtgjson.com/api/v5/AllPrintings.json";
        this.priceUrl = "https://mtgjson.com/api/v5/AllPricesToday.json";
        this.priceTargetFilePath = "src/test/resources/AllPricesToday.json";


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

    public String fetchOnlineMetaDate() {
        try {
            URL url = new URL("https://mtgjson.com/api/v5/Meta.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (conn.getResponseCode() == 200) {
                String content = new String(conn.getInputStream().readAllBytes());
                JSONObject json = new JSONObject(content);
                if (json.has("data")) {
                    return json.getJSONObject("data").optString("date", null);
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Meta.json: " + e.getMessage());
        }
        return null;
    }

    public void fetchpriceData() {
        try {
            System.out.println("Lade AllPricesToday.json herunter...");
            URL url = new URL("https://mtgjson.com/api/v5/AllPricesToday.json");
            File targetFile = new File("src/test/resources/AllPricesToday.json");

            // Stellt sicher, dass alte Preis-Dateien überschrieben werden
            try (InputStream in = url.openStream()) {
                Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("AllPricesToday.json erfolgreich aktualisiert.");
        } catch (Exception e) {
            System.err.println("Fehler beim Herunterladen der Preisdaten: " + e.getMessage());
        }
    }
}