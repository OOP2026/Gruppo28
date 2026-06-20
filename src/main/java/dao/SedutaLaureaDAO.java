package dao;

import model.SedutaLaurea;
import java.util.List;

public interface SedutaLaureaDAO {
    void salvaSeduta(SedutaLaurea seduta);
    SedutaLaurea getSedutaById(int id);
    List<SedutaLaurea> getAllSedute();
}