import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection implements AutoCloseable{

    private final String URL;
    private final String User;
    private final String Password;
    private Connection connection;

    public DBConnection(){
        this.URL = "jdbc:postgresql://localhost:5432/postgres";
        this.User = "user";
        this.Password = "password";


    }

    public Connection getConnection() throws SQLException{
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL, User, Password);
        }
        return connection;
    }
    @Override
    public void close() throws SQLException{
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Verbindung mit Datenbank erfolgreich geschlossen");
        }
    }
}
