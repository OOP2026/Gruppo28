package model;

public class Studente extends Utente {
    private String matricola;
    private RichiestaTirocinio richiestaAttuale;
    private Tesi tesi;

    public Studente(int id, String nome, String cognome, String email, String username, String password, String matricola) {
        super(id, nome, cognome, email, username, password);
        this.matricola = matricola;
    }

    public void richiediTirocinio(ArgomentoTirocinio argomento) {
        int nuovoId = (int) (Math.random() * 1000);
        this.richiestaAttuale = new RichiestaTirocinio(nuovoId, argomento, this);
        System.out.println("Studente " + this.nome + " ha richiesto il tirocinio: " + argomento.getTitolo());
    }

    public void caricaTesi(Tesi tesi) {
        if (this.richiestaAttuale != null && this.richiestaAttuale.getStato() == Stato.APPROVATA) {
            this.tesi = tesi;
            System.out.println("Tesi caricata con successo.");
        } else {
            System.out.println("Errore: Impossibile caricare la tesi. Tirocinio non ancora approvato.");
        }
    }

    public RichiestaTirocinio getRichiestaAttuale() {
        return this.richiestaAttuale;
    }

    public Tesi getTesi() {
        return this.tesi;
    }

    public String getMatricola() {
        return this.matricola;
    }
}