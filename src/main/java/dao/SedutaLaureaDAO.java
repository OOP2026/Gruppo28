package dao;

import model.SedutaLaurea;
import java.util.List;

/**
 * Interfaccia che definisce le operazioni di accesso ai dati per le sedute di laurea.
 */
public interface SedutaLaureaDAO {

    /**
     * Registra una nuova seduta di laurea nel sistema.
     *
     * @param seduta L'oggetto SedutaLaurea da salvare
     */
    void salvaSeduta(SedutaLaurea seduta);

    /**
     * Recupera una seduta di laurea tramite il suo ID.
     *
     * @param id L'identificativo univoco della seduta
     * @return L'oggetto SedutaLaurea corrispondente, null se non trovato
     */
    SedutaLaurea getSedutaById(int id);

    /**
     * Recupera la lista di tutte le sedute di laurea programmate nel sistema.
     *
     * @return Lista globale delle sedute di laurea
     */
    List<SedutaLaurea> getAllSedute();
}