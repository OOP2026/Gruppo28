package model;

public class RichiestaTirocinio {
    private int id;
    private Stato stato; // Sostituito il boolean con l'Enum Stato
    private ArgomentoTirocinio argomento;
    private Studente studente;

    public RichiestaTirocinio(int id, ArgomentoTirocinio argomento, Studente studente) {
        this.id = id;
        this.argomento = argomento;
        this.studente = studente;
        this.stato = Stato.IN_ATTESA; // Default iniziale
    }

    public int getId() { return id; }
    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }
    public ArgomentoTirocinio getArgomento() { return argomento; }
    public Studente getStudente() { return studente; }

    @Override
    public String toString() {
        return "Richiesta ID: " + this.id + " - Stato: " + this.stato;
    }
}