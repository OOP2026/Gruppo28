package model;

/**
 * Rappresenta una richiesta di tirocinio inoltrata da uno studente.
 * Collega lo studente richiedente all'argomento scelto e traccia l'avanzamento
 * della pratica tramite il suo stato (IN_ATTESA, APPROVATA, RIFIUTATA).
 * Contiene anche l'eventuale motivazione fornita dal docente in caso di rifiuto.
 */
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

    /**
     * Fornisce una stringa riassuntiva della richiesta,
     * utile per la visualizzazione rapida all'interno di liste nella GUI.
     *
     * @return Una stringa formattata con ID, nome dello studente e stato attuale
     */
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