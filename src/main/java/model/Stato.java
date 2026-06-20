package model;

/**
 * Enumerazione che definisce i possibili stati di avanzamento e valutazione
 * per le pratiche gestite nel sistema (richieste di tirocinio e caricamento tesi).
 */
public enum Stato {

    /** * La pratica è stata appena inoltrata dallo studente ed è in attesa
     * di essere presa in carico e valutata dal docente di riferimento.
     */
    IN_ATTESA,

    /** * La pratica è stata valutata positivamente e confermata dal docente.
     */
    APPROVATA,

    /** * La pratica è stata valutata negativamente e respinta.
     */
    RIFIUTATA
}