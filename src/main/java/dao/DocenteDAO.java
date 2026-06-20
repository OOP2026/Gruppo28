package dao;

import model.Docente;
import java.util.List;

public interface DocenteDAO {
    void salvaDocente(Docente docente);
    Docente getDocenteById(int id);
    List<Docente> getAllDocenti();
    boolean isCoordinatore(int docenteId);
}