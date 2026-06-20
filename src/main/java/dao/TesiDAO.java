package dao;

import model.Tesi;
import model.Stato;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per la gestione delle tesi.
 */
public interface TesiDAO {

    /**
     * Salva una nuova tesi caricata da uno studente, associandola al suo ID.
     *
     * @param tesi L'oggetto Tesi da salvare
     * @param studenteId L'ID dello studente che ha caricato la tesi
     */
    void salvaTesi(Tesi tesi, int studenteId);

    /**
     * Aggiorna lo stato di valutazione di una tesi (es. da IN_ATTESA a APPROVATA).
     *
     * @param tesiId L'ID della tesi da aggiornare
     * @param nuovoStato Il nuovo stato assegnato dal docente
     */
    void aggiornaStatoTesi(int tesiId, Stato nuovoStato);

    /**
     * Recupera una tesi specifica tramite il suo ID.
     *
     * @param id L'identificativo della tesi
     * @return L'oggetto Tesi corrispondente, null se non trovato
     */
    Tesi getTesiById(int id);

    /**
     * Recupera la tesi caricata da uno specifico studente.
     *
     * @param studenteId L'ID dello studente
     * @return La Tesi dello studente, null se non ha ancora caricato nulla
     */
    Tesi getTesiByStudente(int studenteId);

    /**
     * Recupera tutte le tesi associate a una determinata seduta di laurea.
     *
     * @param sedutaId L'ID della seduta di laurea
     * @return Lista delle tesi prenotate per quella seduta
     */
    List<Tesi> getTesiBySeduta(int sedutaId);
}