package controller;

import model.*;
import gui.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

	private static Controller instance;

	private Utente utenteLoggato;
	private List<Utente> fintoDatabaseUtenti;
	private List<ArgomentoTirocinio> fintoDatabaseArgomenti;
	private List<model.RichiestaTirocinio> tutteLeRichieste = new ArrayList<>();

	private Controller() {
		fintoDatabaseUtenti = new ArrayList<>();
		fintoDatabaseArgomenti = new ArrayList<>();

		Studente studente = new Studente(1, "Mario", "Rossi", "m@mail.it", "mrossi", "123", "MAT01");
		Docente docente = new Docente(2, "Luigi", "Verdi", "l.verdi@mail.it", "lverdi", "456");

		fintoDatabaseUtenti.add(studente);
		fintoDatabaseUtenti.add(docente);

		ArgomentoTirocinio argomento = new ArgomentoTirocinio(1, "Sviluppo App", TipoTirocinio.INTERNO, docente.getCognome());

		docente.aggiungiArgomento(argomento);

		fintoDatabaseArgomenti.add(argomento);
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public boolean login(String username, String password) {
		for (Utente u : fintoDatabaseUtenti) {
			if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
				this.utenteLoggato = u;
				return true;
			}
		}
		return false;
	}

	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

	public void apriHomeUtente() {
		if (utenteLoggato instanceof Studente) {
			new HomeStudente().setVisible(true);
			System.out.println("Login effettuato come studente!");
		}
		else if (utenteLoggato instanceof model.Docente) {
			new gui.HomeDocente().setVisible(true);
			System.out.println("Login effettuato come docente!");
		}
	}

	public List<ArgomentoTirocinio> getTuttiGliArgomenti() {
		return fintoDatabaseArgomenti;
	}

	public void richiediTirocinioPerStudente(model.ArgomentoTirocinio argomento) {
		if (utenteLoggato instanceof model.Studente) {
			model.Studente s = (model.Studente) utenteLoggato;
			s.richiediTirocinio(argomento);

			model.RichiestaTirocinio nuovaRichiesta = s.getRichiestaAttuale();
			tutteLeRichieste.add(nuovaRichiesta);

			for (Utente u : fintoDatabaseUtenti) {
				if (u instanceof model.Docente) {
					model.Docente d = (model.Docente) u;
					if (d.getCognome().equals(argomento.getReferente())) {

					}
				}
			}
		}
	}

	public List<SedutaLaurea> getTutteLeSedute() {
		List<SedutaLaurea> sedute = new ArrayList<>();
		sedute.add(new SedutaLaurea(1, java.time.LocalDate.now(), java.time.LocalTime.now(), "Aula Magna (Luglio)"));
		return sedute;
	}

	public void caricaTesiPerStudente(String path, SedutaLaurea seduta) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			s.caricaTesi(path, seduta);
		}
	}

	public void aggiungiNuovoArgomento(String titolo, model.TipoTirocinio tipo, String referente) {
		if (utenteLoggato instanceof model.Docente) {
			model.Docente docente = (model.Docente) utenteLoggato;

			int nuovoId = getTuttiGliArgomenti().size() + 1;
			model.ArgomentoTirocinio nuovoArgomento = new model.ArgomentoTirocinio(nuovoId, titolo, tipo, referente);

			docente.aggiungiArgomento(nuovoArgomento);
			getTuttiGliArgomenti().add(nuovoArgomento);
		}
	}

	public List<model.RichiestaTirocinio> getRichiesteInAttesa() {
		List<model.RichiestaTirocinio> inAttesa = new ArrayList<>();
		for (model.RichiestaTirocinio r : tutteLeRichieste) {
			if (r.getStato() == model.Stato.IN_ATTESA) {
				inAttesa.add(r);
			}
		}
		return inAttesa;
	}

	public void valutaRichiestaComeDocente(model.RichiestaTirocinio richiesta, boolean accetta) {
		if (utenteLoggato instanceof model.Docente) {
			model.Docente docente = (model.Docente) utenteLoggato;
			docente.valutaRichiesta(richiesta, accetta);
		}
	}
}