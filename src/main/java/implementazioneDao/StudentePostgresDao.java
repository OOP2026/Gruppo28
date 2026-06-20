package implementazioneDao;

import dao.StudenteDAO;
import database_connection.ConnessioneDatabase;
import model.Studente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentePostgresDao implements StudenteDAO {

    private Connection connection;

    public StudentePostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
    }

    @Override
    public void salvaStudente(Studente studente) {
        String queryUtente = "INSERT INTO utenti (nome, cognome, email, username, password) VALUES (?, ?, ?, ?, ?)";
        String queryStudente = "INSERT INTO studenti (utente_id, matricola) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            int utenteIdGenerato = -1;

            try (PreparedStatement pstUtente = connection.prepareStatement(queryUtente, Statement.RETURN_GENERATED_KEYS)) {
                pstUtente.setString(1, studente.getNome());
                pstUtente.setString(2, studente.getCognome());
                pstUtente.setString(3, studente.getEmail());
                pstUtente.setString(4, studente.getUsername());
                pstUtente.setString(5, studente.getPassword());

                pstUtente.executeUpdate();

                ResultSet rsKeys = pstUtente.getGeneratedKeys();
                if (rsKeys.next()) {
                    utenteIdGenerato = rsKeys.getInt(1);
                }
            }

            try (PreparedStatement pstStudente = connection.prepareStatement(queryStudente)) {
                pstStudente.setInt(1, utenteIdGenerato);
                pstStudente.setString(2, studente.getMatricola());
                pstStudente.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Errore durante il rollback: " + ex.getMessage());
            }
            System.err.println("Errore durante il salvataggio dello studente: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Errore nel ripristino dell'autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public Studente getStudenteById(int id) {
        String query = "SELECT u.id, u.nome, u.cognome, u.email, u.username, u.password, s.matricola " +
                "FROM utenti u JOIN studenti s ON u.id = s.utente_id WHERE u.id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Studente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("matricola")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dello studente per ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Studente getStudenteByMatricola(String matricola) {
        String query = "SELECT u.id, u.nome, u.cognome, u.email, u.username, u.password, s.matricola " +
                "FROM utenti u JOIN studenti s ON u.id = s.utente_id WHERE s.matricola = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, matricola);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Studente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("matricola")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dello studente per Matricola: " + e.getMessage());
        }
        return null;
    }
}
