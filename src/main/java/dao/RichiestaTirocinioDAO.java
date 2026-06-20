package dao;

import model.RichiestaTirocinio;
import model.Stato;
import java.util.List;

public interface RichiestaTirocinioDAO {
    void salvaRichiesta(RichiestaTirocinio richiesta);
    void aggiornaStatoRichiesta(int richiestaId, Stato nuovoStato, String motivazioneRifiuto);
    RichiestaTirocinio getRichiestaById(int id);
    RichiestaTirocinio getRichiestaAttualeByStudente(int studenteId);
    List<RichiestaTirocinio> getRichiesteByDocente(int docenteId);
}