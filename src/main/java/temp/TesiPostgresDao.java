package temp;

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

public class TesiPostgresDao implements TesiDAO {

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
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Tesi getTesiById(int id) {
        String query = "SELECT * FROM tesi WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                SedutaLaurea seduta = null;
                if (rs.getObject("seduta_id") != null) {
                    seduta = sedutaDao.getSedutaById(rs.getInt("seduta_id"));
                }
                Tesi t = new Tesi(rs.getInt("id"), rs.getString("file_path"), seduta, rs.getString("nome_studente"));
                t.setStato(Stato.valueOf(rs.getString("stato")));
                return t;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Tesi getTesiByStudente(int studenteId) {
        String query = "SELECT * FROM tesi WHERE studente_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, studenteId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                SedutaLaurea seduta = null;
                if (rs.getObject("seduta_id") != null) {
                    seduta = sedutaDao.getSedutaById(rs.getInt("seduta_id"));
                }
                Tesi t = new Tesi(rs.getInt("id"), rs.getString("file_path"), seduta, rs.getString("nome_studente"));
                t.setStato(Stato.valueOf(rs.getString("stato")));
                return t;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Tesi> getTesiBySeduta(int sedutaId) {
        List<Tesi> lista = new ArrayList<>();
        String query = "SELECT * FROM tesi WHERE seduta_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, sedutaId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                SedutaLaurea seduta = sedutaDao.getSedutaById(rs.getInt("seduta_id"));
                Tesi t = new Tesi(rs.getInt("id"), rs.getString("file_path"), seduta, rs.getString("nome_studente"));
                t.setStato(Stato.valueOf(rs.getString("stato")));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }
}
