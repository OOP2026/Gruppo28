package model;

/**
 * Rappresenta un argomento di tirocinio disponibile nel sistema.
 * Contiene le informazioni relative al titolo, alla tipologia (INTERNO o ESTERNO)
 * e al referente incaricato di seguire lo studente.
 */
public class ArgomentoTirocinio {
    private int id;
    private String titolo;
    private TipoTirocinio tipo;
    private String referenteAziendale;
    private Docente docente;

    public ArgomentoTirocinio(int id, String titolo, TipoTirocinio tipo, String referenteAziendale, Docente docente) {
        this.id = id;
        this.titolo = titolo;
        this.tipo = tipo;
        this.referenteAziendale = referenteAziendale;
        this.docente = docente;
    }

    public int getId() { return id; }
    public String getTitolo() { return titolo; }
    public TipoTirocinio getTipo() { return tipo; }
    public String getReferenteAziendale() { return referenteAziendale; }
    public Docente getDocente() { return docente; }

    /**
     * Restituisce il nominativo del referente assegnato a questo tirocinio.
     * Nel caso di tirocinio interno coinciderà con il nome del docente,
     * nel caso di tirocinio esterno sarà il tutor aziendale.
     *
     * @return Il nome del referente
     */
    public String getReferente() {
        return this.referenteAziendale;
    }

    /**
     * Fornisce una rappresentazione testuale formattata dell'argomento,
     * utilizzata per la visualizzazione corretta all'interno delle liste nella GUI.
     *
     * @return Una stringa contenente il titolo e il tipo di tirocinio tra parentesi
     */
    @Override
    public String toString() {
        return this.titolo + " (" + this.tipo + ")";
    }
}