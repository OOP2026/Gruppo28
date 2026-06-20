package model;

/**
 * Enumerazione che definisce le possibili tipologie di tirocinio
 * a cui uno studente può candidarsi.
 */
public enum TipoTirocinio {

    /** * Tirocinio svolto all'interno delle strutture universitarie.
     * Il referente assegnato corrisponde al docente stesso che propone l'argomento.
     */
    INTERNO,

    /** * Tirocinio svolto presso un'azienda o un ente esterno all'università.
     * Prevede l'assegnazione di un referente o tutor aziendale specifico.
     */
    ESTERNO
}