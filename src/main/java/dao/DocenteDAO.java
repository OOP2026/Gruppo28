package dao;

import model.Docente;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per l'entità Docente e Coordinatore.
 */
public interface DocenteDAO {

    /**
     * Salva un nuovo docente nel database.
     *
     * @param docente L'oggetto Docente da salvare
     */
    void salvaDocente(Docente docente);

    /**
     * Recupera un docente dal database tramite il suo ID utente.
     *
     * @param id L'identificativo univoco del docente
     * @return L'oggetto Docente corrispondente, null se non trovato
     */
    Docente getDocenteById(int id);

    /**
     * Recupera la lista di tutti i docenti registrati nel sistema.
     *
     * @return Una lista di oggetti Docente
     */
    List<Docente> getAllDocenti();

    /**
     * Verifica se un determinato docente possiede anche il ruolo di Coordinatore.
     *
     * @param docenteId L'ID del docente da verificare
     * @return true se il docente è un coordinatore, false altrimenti
     */
    boolean isCoordinatore(int docenteId);
}