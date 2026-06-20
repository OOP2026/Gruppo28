package dao;

import model.Utente;

public interface UtenteDAO {
    Utente autenticaUtente(String username, String password);
    Utente getUtenteById(int id);
}