import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String url = "jdbc:mysql://localhost:3306/college";
    private static final String user = "root";
    private static final String password = "root";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}