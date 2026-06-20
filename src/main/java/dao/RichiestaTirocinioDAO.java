package dao;

import model.RichiestaTirocinio;
import model.Stato;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per le richieste di tirocinio.
 */
public interface RichiestaTirocinioDAO {

    /**
     * Salva una nuova richiesta di tirocinio inoltrata da uno studente.
     *
     * @param richiesta L'oggetto RichiestaTirocinio da salvare
     */
    void salvaRichiesta(RichiestaTirocinio richiesta);

    /**
     * Aggiorna lo stato di una richiesta esistente (es. da IN_ATTESA a APPROVATA o RIFIUTATA).
     *
     * @param richiestaId L'ID della richiesta da aggiornare
     * @param nuovoStato Il nuovo stato da assegnare
     * @param motivazioneRifiuto Eventuale motivazione in caso di rifiuto (può essere null)
     */
    void aggiornaStatoRichiesta(int richiestaId, Stato nuovoStato, String motivazioneRifiuto);

    /**
     * Recupera una richiesta specifica tramite il suo ID.
     *
     * @param id L'identificativo della richiesta
     * @return La RichiestaTirocinio corrispondente, null se non trovata
     */
    RichiestaTirocinio getRichiestaById(int id);

    /**
     * Recupera l'ultima richiesta inoltrata in ordine di tempo da uno specifico studente.
     *
     * @param studenteId L'ID dello studente
     * @return La RichiestaTirocinio attuale dello studente, null se non ha mai fatto richieste
     */
    RichiestaTirocinio getRichiestaAttualeByStudente(int studenteId);

    /**
     * Recupera tutte le richieste di tirocinio destinate a un determinato docente.
     *
     * @param docenteId L'ID del docente
     * @return Lista delle richieste associate agli argomenti di quel docente
     */
    List<RichiestaTirocinio> getRichiesteByDocente(int docenteId);
}