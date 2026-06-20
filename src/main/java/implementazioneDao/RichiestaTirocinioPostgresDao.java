package implementazioneDao;

import dao.RichiestaTirocinioDAO;
import dao.ArgomentoTirocinioDAO;
import dao.StudenteDAO;
import database_connection.ConnessioneDatabase;
import model.RichiestaTirocinio;
import model.Stato;
import model.ArgomentoTirocinio;
import model.Studente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RichiestaTirocinioPostgresDao implements RichiestaTirocinioDAO {

    private Connection connection;
    private ArgomentoTirocinioDAO argomentoDao;
    private StudenteDAO studenteDao;

    public RichiestaTirocinioPostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
        this.argomentoDao = new ArgomentoTirocinioPostgresDao();
        this.studenteDao = new StudentePostgresDao();
    }

    @Override
    public void salvaRichiesta(RichiestaTirocinio richiesta) {
        String query = "INSERT INTO richieste_tirocinio (stato, argomento_id, studente_id, motivazione_rifiuto) VALUES (CAST(? AS stato_enum), ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, richiesta.getStato().name());
            pst.setInt(2, richiesta.getArgomento().getId());
            pst.setInt(3, richiesta.getStudente().getId());
            pst.setString(4, richiesta.getMotivazioneRifiuto());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void aggiornaStatoRichiesta(int richiestaId, Stato nuovoStato, String motivazioneRifiuto) {
        String query = "UPDATE richieste_tirocinio SET stato = CAST(? AS stato_enum), motivazione_rifiuto = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, nuovoStato.name());
            pst.setString(2, motivazioneRifiuto);
            pst.setInt(3, richiestaId);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public RichiestaTirocinio getRichiestaById(int id) {
        String query = "SELECT * FROM richieste_tirocinio WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ArgomentoTirocinio arg = argomentoDao.getArgomentoById(rs.getInt("argomento_id"));
                Studente stud = studenteDao.getStudenteById(rs.getInt("studente_id"));
                RichiestaTirocinio r = new RichiestaTirocinio(rs.getInt("id"), arg, stud);
                r.setStato(Stato.valueOf(rs.getString("stato")));
                r.setMotivazioneRifiuto(rs.getString("motivazione_rifiuto"));
                return r;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public RichiestaTirocinio getRichiestaAttualeByStudente(int studenteId) {
        String query = "SELECT * FROM richieste_tirocinio WHERE studente_id = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, studenteId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ArgomentoTirocinio arg = argomentoDao.getArgomentoById(rs.getInt("argomento_id"));
                Studente stud = studenteDao.getStudenteById(rs.getInt("studente_id"));
                RichiestaTirocinio r = new RichiestaTirocinio(rs.getInt("id"), arg, stud);
                r.setStato(Stato.valueOf(rs.getString("stato")));
                r.setMotivazioneRifiuto(rs.getString("motivazione_rifiuto"));
                return r;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<RichiestaTirocinio> getRichiesteByDocente(int docenteId) {
        List<RichiestaTirocinio> lista = new ArrayList<>();
        String query = "SELECT r.* FROM richieste_tirocinio r JOIN argomenti_tirocinio a ON r.argomento_id = a.id WHERE a.docente_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, docenteId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ArgomentoTirocinio arg = argomentoDao.getArgomentoById(rs.getInt("argomento_id"));
                Studente stud = studenteDao.getStudenteById(rs.getInt("studente_id"));
                RichiestaTirocinio r = new RichiestaTirocinio(rs.getInt("id"), arg, stud);
                r.setStato(Stato.valueOf(rs.getString("stato")));
                r.setMotivazioneRifiuto(rs.getString("motivazione_rifiuto"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }
}