package model;

import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente {
    // Liste di oggetti al posto di liste di ID
    private List<ArgomentoTirocinio> argomentiProposti = new ArrayList<>();
    private List<RichiestaTirocinio> tirociniInCorso = new ArrayList<>();

    public Docente(int id, String nome, String cognome, String email, String username, String password) {
        super(id, nome, cognome, email, username, password);
    }

    public void aggiungiArgomento(ArgomentoTirocinio argomento) {
        this.argomentiProposti.add(argomento);
        System.out.println("Argomento '" + argomento.getTitolo() + "' aggiunto con successo.");
    }

    /**
     * Ora riceve direttamente l'oggetto Richiesta per poterne cambiare lo stato
     */
    public void valutaRichiesta(RichiestaTirocinio richiesta, boolean approvata) {
        richiesta.setStato(approvata ? Stato.APPROVATA : Stato.RIFIUTATA);

        if (approvata) {
            this.tirociniInCorso.add(richiesta);
        }
        System.out.println("Richiesta n." + richiesta.getId() + " valutata: " + richiesta.getStato());
    }

    /**
     * Ora riceve l'oggetto Tesi per cambiarne lo stato
     */
    public void valutaTesi(Tesi tesi, boolean tesiApprovata) {
        tesi.setStato(tesiApprovata ? Stato.APPROVATA : Stato.RIFIUTATA);
        System.out.println("Tesi (File: " + tesi.getFilePath() + ") valutata. Esito: " + tesi.getStato());
    }

    public List<RichiestaTirocinio> getTirociniInCorso() {
        return this.tirociniInCorso;
    }
}