package model;

/**
 * Rappresenta uno Studente all'interno del sistema.
 * Estende la classe Utente ed eredita le credenziali di accesso.
 * Gestisce l'invio della propria richiesta di tirocinio, il tracciamento dello stato
 * del tirocinio stesso e il caricamento del documento di tesi finale per la laurea.
 */
public class Studente extends Utente {
    private String matricola;
    private RichiestaTirocinio richiestaAttuale;
    private Tesi tesi;

    public Studente(int id, String nome, String cognome, String email, String username, String password, String matricola) {
        super(id, nome, cognome, email, username, password);
        this.matricola = matricola;
    }

    /**
     * Consente allo studente di inoltrare una nuova richiesta formale di tirocinio.
     * Crea una nuova istanza di RichiestaTirocinio associata a questa istanza di studente.
     *
     * @param argomento L'argomento di tirocinio selezionato dallo studente
     */
    public void richiediTirocinio(ArgomentoTirocinio argomento) {
        int nuovoId = (int) (Math.random() * 1000);
        this.richiestaAttuale = new RichiestaTirocinio(nuovoId, argomento, this);
        System.out.println("Studente " + this.nome + " ha richiesto il tirocinio: " + argomento.getTitolo());
    }

    /**
     * Consente allo studente di caricare l'elaborato di tesi.
     * L'operazione viene completata con successo solo ed esclusivamente se lo studente
     * ha già una richiesta di tirocinio precedentemente registrata e già approvata da un docente.
     *
     * @param tesi L'oggetto Tesi che rappresenta il file e la seduta scelti
     */
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