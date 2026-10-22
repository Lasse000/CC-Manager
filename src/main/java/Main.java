import dao.ImporterCoordinator;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starte MTG Datenbank-Import ===");

        ImporterCoordinator coordinator = new ImporterCoordinator();

        try {
            coordinator.startFullImport();
            System.out.println("=== Import erfolgreich abgeschlossen! ===");
        } catch (SQLException e) {
            System.err.println("SQL-Fehler beim Importieren:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unerwarteter Fehler:");
            e.printStackTrace();
        }
    }
}