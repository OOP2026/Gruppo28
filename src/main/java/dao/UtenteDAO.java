package dao;

import model.Utente;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per l'entità Utente.
 */
public interface UtenteDAO {

    /**
     * Verifica le credenziali di un utente nel database.
     *
     * @param username L'username inserito
     * @param password La password inserita
     * @return L'oggetto Utente se le credenziali sono corrette, null altrimenti
     */
    Utente autenticaUtente(String username, String password);

    /**
     * Recupera un utente dal database tramite il suo ID.
     *
     * @param id L'identificativo univoco dell'utente
     * @return L'oggetto Utente corrispondente, null se non trovato
     */
    Utente getUtenteById(int id);
}