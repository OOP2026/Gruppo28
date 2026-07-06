package implementazionedao;

import dao.SedutaLaureaDAO;
import database_connection.ConnessioneDatabase;
import model.SedutaLaurea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SedutaLaureaPostgresDao implements SedutaLaureaDAO {

    private static final Logger LOGGER = Logger.getLogger(SedutaLaureaPostgresDao.class.getName());

    private static final String COL_ID = "id";
    private static final String COL_DATA = "data";
    private static final String COL_ORA = "ora";
    private static final String COL_LUOGO = "luogo";

    private Connection connection;

    public SedutaLaureaPostgresDao() {
        this.connection = ConnessioneDatabase.getInstance();
    }

    @Override
    public void salvaSeduta(SedutaLaurea seduta) {
        String query = "INSERT INTO sedute_laurea (data, ora, luogo) VALUES (?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setDate(1, Date.valueOf(seduta.getData()));
            pst.setTime(2, Time.valueOf(seduta.getOra()));
            pst.setString(3, seduta.getLuogo());
            pst.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel salvataggio seduta", e);
        }
    }

    @Override
    public SedutaLaurea getSedutaById(int id) {
        String query = "SELECT id, data, ora, luogo FROM sedute_laurea WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new SedutaLaurea(
                        rs.getInt(COL_ID),
                        rs.getDate(COL_DATA).toLocalDate(),
                        rs.getTime(COL_ORA).toLocalTime(),
                        rs.getString(COL_LUOGO)
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero seduta per ID", e);
        }
        return null;
    }

    @Override
    public List<SedutaLaurea> getAllSedute() {
        List<SedutaLaurea> lista = new ArrayList<>();
        String query = "SELECT id, data, ora, luogo FROM sedute_laurea";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(new SedutaLaurea(
                        rs.getInt(COL_ID),
                        rs.getDate(COL_DATA).toLocalDate(),
                        rs.getTime(COL_ORA).toLocalTime(),
                        rs.getString(COL_LUOGO)
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nel recupero di tutte le sedute", e);
        }
        return lista;
    }
}