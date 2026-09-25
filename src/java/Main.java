import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class Main{
    public static void main (String[] args) throws SQLException {
        DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        src.RawDataFetcher rawDataFetcher = new src.RawDataFetcher();
        //rawDataFetcher.fetchData();


    }
}

