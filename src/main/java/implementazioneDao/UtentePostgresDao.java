package implementazioneDao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentePostgresDao implements UtenteDAO {

    private Connection connection;

    public UtentePostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
    }

    @Override
    public Utente autenticaUtente(String username, String password) {
        String query = "SELECT * FROM utenti WHERE username = ? AND password = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Utente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'autenticazione dell'utente: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Utente getUtenteById(int id) {
        String query = "SELECT * FROM utenti WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Utente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dell'utente per ID: " + e.getMessage());
        }

        return null;
    }
}