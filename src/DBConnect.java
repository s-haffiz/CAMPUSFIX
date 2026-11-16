import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Database connection code
public class DBConnect {

    // Reads db.properties and returns its settings
    private static Properties loadProperties() throws SQLException {
        // The working folder changes depending on how the app is started,
        // so we check a few likely places
        String[] locations = {
            "db.properties",
            "../db.properties",
            "CAMPUSFIX/db.properties"
        };

        for (String path : locations) {
            File file = new File(path);
            if (file.exists()) {
                try (FileInputStream in = new FileInputStream(file)) {
                    Properties props = new Properties();
                    props.load(in);
                    return props;
                } catch (IOException e) {
                    throw new SQLException("Could not read " + path + ": " + e.getMessage());
                }
            }
        }
        throw new SQLException("db.properties not found. Copy db.properties.example "
            + "to db.properties and enter your MySQL password.");
    }

    // Returns a new connection. Callers should close it (try-with-resources).
    public static Connection getConnection() throws SQLException {
        Properties props = loadProperties();
        return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password"));
    }
}