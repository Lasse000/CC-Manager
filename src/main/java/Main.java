import auth.UserSession;
import dao.ImporterCoordinator;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CC-Manager Start ===");

        try {
            // 1. Google OAuth Client ID & Secret aus database.ini laden
            java.util.Properties config = new java.util.Properties();
            try (java.io.InputStream input = new java.io.FileInputStream("database.ini")) {
                config.load(input);
            }

            String clientId = config.getProperty("google.client_id");
            String clientSecret = config.getProperty("google.client_secret");

            if (clientId == null || clientSecret == null) {
                throw new IllegalStateException("google.client_id oder google.client_secret fehlt in der database.ini!");
            }

            // 2. Google Login ausführen (öffnet den Browser)
            System.out.println("\n--- Google Login wird gestartet ---");
            auth.GoogleAuthService authService = new auth.GoogleAuthService();
            com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload =
                    authService.loginWithGoogle(clientId, clientSecret);

            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String pictureUrl = (String) payload.get("picture");

            System.out.println("Login erfolgreich bei Google!");
            System.out.println("Account: " + firstName + " " + lastName + " (" + email + ")");

            // 3. User mit PostgreSQL synchronisieren
            dao.UserDAO userDAO = new dao.UserDAO();
            int userId = userDAO.findOrCreateUser(googleId, email, firstName, lastName, pictureUrl);            System.out.println("Eingeloggt als DB User-ID: " + userId);
            UserSession.init(userId, email, firstName);
            // 4. MTG Datenbank-Import ausführen
            System.out.println("\n=== Starte MTG Datenbank-Import ===");
            ImporterCoordinator coordinator = new ImporterCoordinator();
            coordinator.startFullImport();
            System.out.println("=== Import erfolgreich abgeschlossen! ===");

        } catch (java.sql.SQLException e) {
            System.err.println("SQL-Fehler:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unerwarteter Fehler beim Ablauf:");
            e.printStackTrace();
        }
    }
}
