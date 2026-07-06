package implementazionedao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtentePostgresDao implements UtenteDAO {

    private static final Logger LOGGER = Logger.getLogger(UtentePostgresDao.class.getName());

    private static final String COL_ID = "id";
    private static final String COL_NOME = "nome";
    private static final String COL_COGNOME = "cognome";
    private static final String COL_EMAIL = "email";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    private Connection connection;

    public UtentePostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
    }

    @Override
    public Utente autenticaUtente(String username, String password) {
        String query = "SELECT id, nome, cognome, email, username, password FROM utenti WHERE username = ? AND password = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return mapResultSetToUtente(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'autenticazione dell'utente", e);
        }

        return null;
    }

    @Override
    public Utente getUtenteById(int id) {
        String query = "SELECT id, nome, cognome, email, username, password FROM utenti WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return mapResultSetToUtente(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero dell'utente per ID", e);
        }

        return null;
    }

    private Utente mapResultSetToUtente(ResultSet rs) throws SQLException {
        return new Utente(
                rs.getInt(COL_ID),
                rs.getString(COL_NOME),
                rs.getString(COL_COGNOME),
                rs.getString(COL_EMAIL),
                rs.getString(COL_USERNAME),
                rs.getString(COL_PASSWORD)
        );
    }
}