package implementazionedao;

import dao.TesiDAO;
import dao.SedutaLaureaDAO;
import database_connection.ConnessioneDatabase;
import model.Tesi;
import model.Stato;
import model.SedutaLaurea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TesiPostgresDao implements TesiDAO {

    private static final Logger LOGGER = Logger.getLogger(TesiPostgresDao.class.getName());

    private static final String COL_ID = "id";
    private static final String COL_FILE_PATH = "file_path";
    private static final String COL_SEDUTA_ID = "seduta_id";
    private static final String COL_NOME_STUDENTE = "nome_studente";
    private static final String COL_STATO = "stato";

    private Connection connection;
    private SedutaLaureaDAO sedutaDao;

    public TesiPostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
        this.sedutaDao = new SedutaLaureaPostgresDao();
    }

    @Override
    public void salvaTesi(Tesi tesi, int studenteId) {
        String query = "INSERT INTO tesi (file_path, stato, seduta_id, nome_studente, studente_id) VALUES (?, CAST(? AS stato_enum), ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, tesi.getFilePath());
            pst.setString(2, tesi.getStato().name());

            if (tesi.getSeduta() != null) {
                pst.setInt(3, tesi.getSeduta().getId());
            } else {
                pst.setNull(3, java.sql.Types.INTEGER);
            }

            pst.setString(4, tesi.getNomeStudente());
            pst.setInt(5, studenteId);
            pst.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel salvataggio tesi", e);
        }
    }

    @Override
    public void aggiornaStatoTesi(int tesiId, Stato nuovoStato) {
        String query = "UPDATE tesi SET stato = CAST(? AS stato_enum) WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, nuovoStato.name());
            pst.setInt(2, tesiId);
            pst.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'aggiornamento stato tesi", e);
        }
    }

    @Override
    public Tesi getTesiById(int id) {
        String query = "SELECT id, file_path, seduta_id, nome_studente, stato FROM tesi WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapResultSetToTesi(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero tesi per ID", e);
        }
        return null;
    }

    @Override
    public Tesi getTesiByStudente(int studenteId) {
        String query = "SELECT id, file_path, seduta_id, nome_studente, stato FROM tesi WHERE studente_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, studenteId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapResultSetToTesi(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero tesi per studente", e);
        }
        return null;
    }

    @Override
    public List<Tesi> getTesiBySeduta(int sedutaId) {
        List<Tesi> lista = new ArrayList<>();
        String query = "SELECT id, file_path, seduta_id, nome_studente, stato FROM tesi WHERE seduta_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, sedutaId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(mapResultSetToTesi(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero tesi per seduta", e);
        }
        return lista;
    }

    private Tesi mapResultSetToTesi(ResultSet rs) throws SQLException {
        SedutaLaurea seduta = null;
        if (rs.getObject(COL_SEDUTA_ID) != null) {
            seduta = sedutaDao.getSedutaById(rs.getInt(COL_SEDUTA_ID));
        }
        Tesi t = new Tesi(rs.getInt(COL_ID), rs.getString(COL_FILE_PATH), seduta, rs.getString(COL_NOME_STUDENTE));
        t.setStato(Stato.valueOf(rs.getString(COL_STATO)));
        return t;
    }
}