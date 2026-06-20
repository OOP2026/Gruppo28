package dao;

import model.ArgomentoTirocinio;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per gli argomenti di tirocinio.
 */
public interface ArgomentoTirocinioDAO {

    /**
     * Salva un nuovo argomento di tirocinio nel database, associandolo a un docente.
     *
     * @param argomento L'argomento da salvare
     * @param docenteId L'ID del docente che propone l'argomento
     */
    void salvaArgomento(ArgomentoTirocinio argomento, int docenteId);

    /**
     * Recupera un argomento di tirocinio tramite il suo ID.
     *
     * @param id L'identificativo dell'argomento
     * @return L'oggetto ArgomentoTirocinio, null se non trovato
     */
    ArgomentoTirocinio getArgomentoById(int id);

    /**
     * Recupera tutti gli argomenti proposti da un determinato docente.
     *
     * @param docenteId L'ID del docente
     * @return Una lista di ArgomentoTirocinio proposti dal docente
     */
    List<ArgomentoTirocinio> getArgomentiByDocente(int docenteId);

    /**
     * Recupera la lista di tutti gli argomenti di tirocinio disponibili nel sistema.
     *
     * @return Una lista globale di ArgomentoTirocinio
     */
    List<ArgomentoTirocinio> getAllArgomenti();
}