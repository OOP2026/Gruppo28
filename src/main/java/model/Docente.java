package model;

import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente {

    private List<ArgomentoTirocinio> argomentiProposti = new ArrayList<>();
    private List<RichiestaTirocinio> richiesteApprovate = new ArrayList<>();

    public Docente(int id, String nome, String cognome, String email, String username, String password) {
        super(id, nome, cognome, email, username, password);
    }

    public void aggiungiArgomento(ArgomentoTirocinio argomento) {
        this.argomentiProposti.add(argomento);
        System.out.println("Argomento '" + argomento.getTitolo() + "' aggiunto con successo.");
    }


    public void valutaRichiesta(RichiestaTirocinio richiesta, boolean approvata) {
        if (approvata) {
            richiesta.setStato(Stato.APPROVATA);
            this.richiesteApprovate.add(richiesta);
        } else {
            richiesta.setStato(Stato.RIFIUTATA);
        }

    }


    public void valutaTesi(Tesi tesi, boolean tesiApprovata) {
        tesi.setStato(tesiApprovata ? Stato.APPROVATA : Stato.RIFIUTATA);
    }

    public List<RichiestaTirocinio> getRichiesteApprovate() {
        return this.richiesteApprovate;
    }
}