package model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Rappresenta un Docente all'interno del sistema.
 * Estende la classe Utente ed eredita le credenziali di accesso.
 * Gestisce gli argomenti di tirocinio proposti, la valutazione delle richieste
 * da parte degli studenti e l'approvazione delle tesi finali.
 */
public class Docente extends Utente {

    private static final Logger LOGGER = Logger.getLogger(Docente.class.getName());

    private List<ArgomentoTirocinio> argomentiProposti = new ArrayList<>();
    private List<RichiestaTirocinio> tirociniInCorso = new ArrayList<>();

    public Docente(int id, String nome, String cognome, String email, String username, String password) {
        super(id, nome, cognome, email, username, password);
    }

    /**
     * Permette al docente di proporre un nuovo argomento di tirocinio
     * aggiungendolo alla propria lista interna.
     *
     * @param argomento L'oggetto ArgomentoTirocinio da aggiungere
     */
    public void aggiungiArgomento(ArgomentoTirocinio argomento) {
        this.argomentiProposti.add(argomento);
        LOGGER.info("Argomento '" + argomento.getTitolo() + "' aggiunto con successo.");
    }

    /**
     * Modifica lo stato di una richiesta di tirocinio in base alla valutazione del docente.
     * Se approvata, il tirocinio viene inserito tra quelli attivi in corso.
     *
     * @param richiesta L'oggetto RichiestaTirocinio da valutare
     * @param approvata true per approvare la richiesta, false per rifiutarla
     */
    public void valutaRichiesta(RichiestaTirocinio richiesta, boolean approvata) {
        if (approvata) {
            richiesta.setStato(Stato.APPROVATA);
            this.tirociniInCorso.add(richiesta);
        } else {
            richiesta.setStato(Stato.RIFIUTATA);
        }
    }

    /**
     * Consente al docente di esprimere un giudizio definitivo sulla tesi caricata da uno studente,
     * cambiandone lo stato in APPROVATA o RIFIUTATA.
     *
     * @param tesi L'oggetto Tesi da valutare
     * @param tesiApprovata true se la tesi soddisfa i requisiti, false altrimenti
     */
    public void valutaTesi(Tesi tesi, boolean tesiApprovata) {
        tesi.setStato(tesiApprovata ? Stato.APPROVATA : Stato.RIFIUTATA);
    }

    public List<RichiestaTirocinio> getTirociniInCorso() {
        return this.tirociniInCorso;
    }
}