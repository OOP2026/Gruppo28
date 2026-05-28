package model;

public class Tesi {
    private int id;
    private String filePath; // Corretto da int a String
    private Stato stato;
    private SedutaLaurea seduta; // Aggiunto riferimento alla seduta scelta

    public Tesi(int id, String filePath, SedutaLaurea seduta) {
        this.id = id;
        this.filePath = filePath;
        this.seduta = seduta;
        this.stato = Stato.IN_ATTESA; // Default quando viene caricata
    }

    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }
    public String getFilePath() { return filePath; }
}