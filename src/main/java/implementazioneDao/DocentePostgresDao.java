package implementazioneDao;

import dao.DocenteDAO;
import database_connection.ConnessioneDatabase;
import model.Docente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DocentePostgresDao implements DocenteDAO {

    private Connection connection;

    public DocentePostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
    }

    @Override
    public void salvaDocente(Docente docente) {
        String queryUtente = "INSERT INTO utenti (nome, cognome, email, username, password) VALUES (?, ?, ?, ?, ?)";
        String queryDocente = "INSERT INTO docenti (utente_id) VALUES (?)";

        try {
            connection.setAutoCommit(false);
            int utenteIdGenerato = -1;

            try (PreparedStatement pstUtente = connection.prepareStatement(queryUtente, Statement.RETURN_GENERATED_KEYS)) {
                pstUtente.setString(1, docente.getNome());
                pstUtente.setString(2, docente.getCognome());
                pstUtente.setString(3, docente.getEmail());
                pstUtente.setString(4, docente.getUsername());
                pstUtente.setString(5, docente.getPassword());

                pstUtente.executeUpdate();

                ResultSet rsKeys = pstUtente.getGeneratedKeys();
                if (rsKeys.next()) {
                    utenteIdGenerato = rsKeys.getInt(1);
                }
            }

            try (PreparedStatement pstDocente = connection.prepareStatement(queryDocente)) {
                pstDocente.setInt(1, utenteIdGenerato);
                pstDocente.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Errore nel rollback: " + ex.getMessage());
            }
            System.err.println("Errore nel salvataggio del docente: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Errore nel ripristino dell'autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public Docente getDocenteById(int id) {
        String query = "SELECT u.id, u.nome, u.cognome, u.email, u.username, u.password " +
                "FROM utenti u JOIN docenti d ON u.id = d.utente_id WHERE u.id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Docente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero del docente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Docente> getAllDocenti() {
        List<Docente> listaDocenti = new ArrayList<>();
        String query = "SELECT u.id, u.nome, u.cognome, u.email, u.username, u.password " +
                "FROM utenti u JOIN docenti d ON u.id = d.utente_id";

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Docente d = new Docente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                listaDocenti.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero della lista docenti: " + e.getMessage());
        }
        return listaDocenti;
    }

    @Override
    public boolean isCoordinatore(int docenteId) {
        String query = "SELECT utente_id FROM coordinatori WHERE utente_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, docenteId);
            ResultSet rs = pst.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.err.println("Errore nella verifica del coordinatore: " + e.getMessage());
        }
        return false;
    }
}
