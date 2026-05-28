package model;

public class Tesi {
    private int id;
    private String filePath;
    private Stato stato;
    private SedutaLaurea seduta;

    public Tesi(int id, String filePath, SedutaLaurea seduta) {
        this.id = id;
        this.filePath = filePath;
        this.seduta = seduta;
        this.stato = Stato.IN_ATTESA;
    }

    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }
    public String getFilePath() { return filePath; }
}