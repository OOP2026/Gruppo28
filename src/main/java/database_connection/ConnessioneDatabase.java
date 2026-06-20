package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5433/gruppo28_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    private static Connection connection = null;

    private ConnessioneDatabase() {}

    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connessione al database PostgreSQL stabilita con successo!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("❌ Errore di connessione al database!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        ConnessioneDatabase.getInstance();
    }
}