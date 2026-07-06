package temp;

import dao.ArgomentoTirocinioDAO;
import database_connection.ConnessioneDatabase;
import model.ArgomentoTirocinio;
import model.TipoTirocinio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArgomentoTirocinioPostgresDao implements ArgomentoTirocinioDAO {

    private static final Logger LOGGER = Logger.getLogger(ArgomentoTirocinioPostgresDao.class.getName());

    private static final String COL_ID = "id";
    private static final String COL_TITOLO = "titolo";
    private static final String COL_TIPO = "tipo";
    private static final String COL_REFERENTE = "referente_aziendale";

    private Connection connection;

    public ArgomentoTirocinioPostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
    }

    @Override
    public void salvaArgomento(ArgomentoTirocinio argomento, int docenteId) {
        String query = "INSERT INTO argomenti_tirocinio (titolo, tipo, referente_aziendale, docente_id) VALUES (?, CAST(? AS tipo_tirocinio_enum), ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, argomento.getTitolo());
            pst.setString(2, argomento.getTipo().name());
            pst.setString(3, argomento.getReferenteAziendale());
            pst.setInt(4, docenteId);

            pst.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel salvataggio dell'argomento", e);
        }
    }

    @Override
    public ArgomentoTirocinio getArgomentoById(int id) {
        String query = "SELECT id, titolo, tipo, referente_aziendale FROM argomenti_tirocinio WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new ArgomentoTirocinio(
                        rs.getInt(COL_ID),
                        rs.getString(COL_TITOLO),
                        TipoTirocinio.valueOf(rs.getString(COL_TIPO)),
                        rs.getString(COL_REFERENTE)
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero dell'argomento", e);
        }
        return null;
    }

    @Override
    public List<ArgomentoTirocinio> getArgomentiByDocente(int docenteId) {
        List<ArgomentoTirocinio> lista = new ArrayList<>();
        String query = "SELECT id, titolo, tipo, referente_aziendale FROM argomenti_tirocinio WHERE docente_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, docenteId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                lista.add(new ArgomentoTirocinio(
                        rs.getInt(COL_ID),
                        rs.getString(COL_TITOLO),
                        TipoTirocinio.valueOf(rs.getString(COL_TIPO)),
                        rs.getString(COL_REFERENTE)
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero degli argomenti per docente", e);
        }
        return lista;
    }

    @Override
    public List<ArgomentoTirocinio> getAllArgomenti() {
        List<ArgomentoTirocinio> lista = new ArrayList<>();
        String query = "SELECT id, titolo, tipo, referente_aziendale FROM argomenti_tirocinio";

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new ArgomentoTirocinio(
                        rs.getInt(COL_ID),
                        rs.getString(COL_TITOLO),
                        TipoTirocinio.valueOf(rs.getString(COL_TIPO)),
                        rs.getString(COL_REFERENTE)
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero di tutti gli argomenti", e);
        }
        return lista;
    }
}