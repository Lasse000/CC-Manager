import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class Main{
    public static void main (String[] args) throws SQLException {
        try(DBConnection db = new DBConnection();
        Connection conn = db.getConnection();
        PreparedStatement stmt = conn.prepareStatement("Select * From games");
        ResultSet rs = stmt.executeQuery()) {
            while(rs.next()) {
                System.out.println(rs.getString("name"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

//Als nächstes Git Repo aufmachen + Github repo