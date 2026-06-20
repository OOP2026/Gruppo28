package dao;

import model.Tesi;
import model.Stato;
import java.util.List;

public interface TesiDAO {
    void salvaTesi(Tesi tesi, int studenteId);
    void aggiornaStatoTesi(int tesiId, Stato nuovoStato);
    Tesi getTesiById(int id);
    Tesi getTesiByStudente(int studenteId);
    List<Tesi> getTesiBySeduta(int sedutaId);
}