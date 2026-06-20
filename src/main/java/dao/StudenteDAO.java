package dao;

import model.Studente;

public interface StudenteDAO {
    void salvaStudente(Studente studente);
    Studente getStudenteById(int id);
    Studente getStudenteByMatricola(String matricola);
}