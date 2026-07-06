package temp;

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

public class SedutaLaureaPostgresDao implements SedutaLaureaDAO {

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
            System.err.println(e.getMessage());
        }
    }

    @Override
    public SedutaLaurea getSedutaById(int id) {
        String query = "SELECT * FROM sedute_laurea WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new SedutaLaurea(
                        rs.getInt("id"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("ora").toLocalTime(),
                        rs.getString("luogo")
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<SedutaLaurea> getAllSedute() {
        List<SedutaLaurea> lista = new ArrayList<>();
        String query = "SELECT * FROM sedute_laurea";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(new SedutaLaurea(
                        rs.getInt("id"),
                        rs.getDate("data").toLocalDate(),
                        rs.getTime("ora").toLocalTime(),
                        rs.getString("luogo")
                ));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }
}