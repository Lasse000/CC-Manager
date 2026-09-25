import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection implements AutoCloseable {

    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DBConnection() {
        Properties config = new Properties();
        try (FileInputStream fis = new FileInputStream("database.ini")) {
            config.load(fis);
            this.url = "jdbc:postgresql://localhost:5432/postgres";
            this.user = config.getProperty("user");
            this.password = config.getProperty("password");
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der database.ini: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Verbindung mit Datenbank erfolgreich geschlossen");
        }
    }
}