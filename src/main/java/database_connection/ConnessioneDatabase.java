package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnessioneDatabase {

    private static final Logger LOGGER = Logger.getLogger(ConnessioneDatabase.class.getName());

    private static final String URL = "jdbc:postgresql://localhost:5433/gruppo28_db";
    private static final String USER = "postgres";
    private static final String DB_SECRET = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "1234";

    private static Connection connection = null;

    private ConnessioneDatabase() {}

    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, DB_SECRET);
                LOGGER.info("✅ Connessione al database PostgreSQL stabilita con successo!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Errore di connessione al database!", e);
        }
        return connection;
    }

    public static void main(String[] args) {
        ConnessioneDatabase.getInstance();
    }
}