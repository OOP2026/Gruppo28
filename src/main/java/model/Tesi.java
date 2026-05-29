package model;

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

    @Override
    public String toString() {
        String nomeFile = new java.io.File(this.filePath).getName();
        return "<html>Tesi ID: " + this.id + " | Studente: " + this.nomeStudente + "<br>File: " + nomeFile + "</html>";
    }
}