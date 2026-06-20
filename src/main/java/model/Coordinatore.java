package model;

/**
 * Rappresenta il ruolo del Coordinatore all'interno del sistema.
 * Estende la classe Docente, in quanto ne eredita tutte le caratteristiche di base
 * (nome, cognome, credenziali), ma viene identificato dal sistema per avere
 * permessi e funzionalità aggiuntive (come la gestione delle sedute di laurea).
 */
public class Coordinatore extends Docente {

    public Coordinatore(int id, String nome, String cognome, String email, String username, String password) {
        super(id, nome, cognome, email, username, password);
    }
}