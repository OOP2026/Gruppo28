package model;

import java.util.ArrayList;
import java.util.List;

public class Coordinatore extends Docente {
    private List<SedutaLaurea> sedutePianificate = new ArrayList<>();

    public Coordinatore(int id, String nome, String cognome, String email, String username, String password) {
        super(id, nome, cognome, email, username, password);
    }

    /**
     * Riceve l'oggetto SedutaLaurea e lo salva nella lista.
     */
    public void inserisciSeduta(SedutaLaurea seduta) {
        this.sedutePianificate.add(seduta);
        System.out.println("Seduta in data pianificata nel sistema.");
    }

    /**
     * Riceve l'oggetto SedutaLaurea di riferimento per formare la commissione.
     */
    public List<Docente> formaCommissione(SedutaLaurea seduta) {
        List<Docente> commissione = new ArrayList<>();

        // Il coordinatore si autoinserisce
        commissione.add(this);

        System.out.println("Commissione formata per la seduta nell'aula: " + seduta.getLuogo());
        return commissione;
    }
}