package dao;

import model.Studente;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per l'entità Studente.
 */
public interface StudenteDAO {

    /**
     * Salva un nuovo studente nel database, inserendo i dati sia nella tabella utenti che in quella studenti.
     *
     * @param studente L'oggetto Studente da salvare
     */
    void salvaStudente(Studente studente);

    /**
     * Recupera uno studente dal database tramite il suo ID utente.
     *
     * @param id L'identificativo univoco dello studente
     * @return L'oggetto Studente corrispondente, null se non trovato
     */
    Studente getStudenteById(int id);

    /**
     * Recupera uno studente dal database tramite la sua matricola.
     *
     * @param matricola La matricola dello studente
     * @return L'oggetto Studente corrispondente, null se non trovato
     */
    Studente getStudenteByMatricola(String matricola);
}