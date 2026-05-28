package model;

public class ArgomentoTirocinio {
    private int id;
    private String titolo; // Corretto da int a String
    private TipoTirocinio tipo;
    private String referenteAziendale; // Corretto da int a String

    public ArgomentoTirocinio(int id, String titolo, TipoTirocinio tipo, String referenteAziendale) {
        this.id = id;
        this.titolo = titolo;
        this.tipo = tipo;
        this.referenteAziendale = referenteAziendale;
    }

    public int getId() { return id; }
    public String getTitolo() { return titolo; }
    public TipoTirocinio getTipo() { return tipo; }
    public String getReferenteAziendale() { return referenteAziendale; }
    @Override
    public String toString() {
        return this.titolo + " (" + this.tipo + ")"; // Es: Sviluppo App (INTERNO)
    }
}