package implementazioneDao;

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

public class ArgomentoTirocinioPostgresDao implements ArgomentoTirocinioDAO {

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
            System.err.println("Errore nel salvataggio dell'argomento: " + e.getMessage());
        }
    }

    @Override
    public ArgomentoTirocinio getArgomentoById(int id) {
        String query = "SELECT * FROM argomenti_tirocinio WHERE id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new ArgomentoTirocinio(
                        rs.getInt("id"),
                        rs.getString("titolo"),
                        TipoTirocinio.valueOf(rs.getString("tipo")),
                        rs.getString("referente_aziendale")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dell'argomento: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ArgomentoTirocinio> getArgomentiByDocente(int docenteId) {
        List<ArgomentoTirocinio> lista = new ArrayList<>();
        String query = "SELECT * FROM argomenti_tirocinio WHERE docente_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, docenteId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                lista.add(new ArgomentoTirocinio(
                        rs.getInt("id"),
                        rs.getString("titolo"),
                        TipoTirocinio.valueOf(rs.getString("tipo")),
                        rs.getString("referente_aziendale")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli argomenti per docente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<ArgomentoTirocinio> getAllArgomenti() {
        List<ArgomentoTirocinio> lista = new ArrayList<>();
        String query = "SELECT * FROM argomenti_tirocinio";

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new ArgomentoTirocinio(
                        rs.getInt("id"),
                        rs.getString("titolo"),
                        TipoTirocinio.valueOf(rs.getString("tipo")),
                        rs.getString("referente_aziendale")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero di tutti gli argomenti: " + e.getMessage());
        }
        return lista;
    }
}