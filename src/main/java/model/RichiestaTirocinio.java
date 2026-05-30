package model;

public class RichiestaTirocinio {
    private int id;
    private Stato stato;
    private ArgomentoTirocinio argomento;
    private Studente studente;
    private String motivazioneRifiuto = "";

    public RichiestaTirocinio(int id, ArgomentoTirocinio argomento, Studente studente) {
        this.id = id;
        this.argomento = argomento;
        this.studente = studente;
        this.stato = Stato.IN_ATTESA;
    }

    public int getId() { return id; }

    public Stato getStato() { return stato; }

    public void setStato(Stato stato) { this.stato = stato; }

    public ArgomentoTirocinio getArgomento() { return argomento; }

    public Studente getStudente() { return studente; }

    @Override
    public String toString() {
        return "Richiesta ID: " + this.id + " | Studente: " + studente.getNome() + " " + studente.getCognome() + " | Stato: " + this.stato;
    }
    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    public void setMotivazioneRifiuto(String motivazioneRifiuto) {
        this.motivazioneRifiuto = motivazioneRifiuto;
    }
}