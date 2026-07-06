package model;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta uno Studente all'interno del sistema.
 */
public class Studente extends Utente {
    private static final Logger LOGGER = Logger.getLogger(Studente.class.getName());
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String matricola;
    private RichiestaTirocinio richiestaAttuale;
    private Tesi tesi;

    public Studente(int id, String nome, String cognome, String email, String username, String password, String matricola) {
        super(id, nome, cognome, email, username, password);
        this.matricola = matricola;
    }

    public void richiediTirocinio(ArgomentoTirocinio argomento) {
        int nuovoId = SECURE_RANDOM.nextInt(1000);
        this.richiestaAttuale = new RichiestaTirocinio(nuovoId, argomento, this);

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Studente {0} ha richiesto il tirocinio: {1}",
                    new Object[]{this.getNome(), argomento.getTitolo()});
        }
    }

    public void caricaTesi(Tesi tesi) {
        if (this.richiestaAttuale != null && this.richiestaAttuale.getStato() == Stato.APPROVATA) {
            this.tesi = tesi;
            LOGGER.info("Tesi caricata con successo.");
        } else {
            LOGGER.warning("Errore: Impossibile caricare la tesi. Tirocinio non ancora approvato.");
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