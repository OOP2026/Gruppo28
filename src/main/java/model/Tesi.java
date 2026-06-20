package model;

/**
 * Rappresenta l'elaborato di tesi finale caricato da uno studente.
 * Contiene il percorso del file fisico caricato a sistema, lo stato di valutazione
 * da parte del relatore e il collegamento alla seduta di laurea scelta per la discussione.
 */
public class Tesi {
    private int id;
    private String filePath;
    private Stato stato;
    private SedutaLaurea seduta;
    private String nomeStudente;

    public Tesi(int id, String filePath, SedutaLaurea seduta, String nomeStudente) {
        this.id = id;
        this.filePath = filePath;
        this.seduta = seduta;
        this.nomeStudente = nomeStudente;
        this.stato = Stato.IN_ATTESA;
    }

    public int getId() { return id; }
    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }
    public String getFilePath() { return filePath; }
    public SedutaLaurea getSeduta() { return seduta; }
    public String getNomeStudente() { return nomeStudente; }

    /**
     * Fornisce una rappresentazione testuale formattata della tesi.
     * Utilizza tag HTML per permettere un'impaginazione multilinea (con andata a capo)
     * all'interno dei componenti visivi (come JList o JLabel) nell'interfaccia grafica.
     *
     * @return Una stringa formattata in HTML contenente l'ID, il nome dello studente e il nome del file
     */
    @Override
    public String toString() {
        String nomeFile = new java.io.File(this.filePath).getName();
        return "<html>Tesi ID: " + this.id + " | Studente: " + this.nomeStudente + "<br>File: " + nomeFile + "</html>";
    }
}