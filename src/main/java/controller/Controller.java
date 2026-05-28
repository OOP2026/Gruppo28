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


	private Controller() {
		fintoDatabaseUtenti = new ArrayList<>();
		fintoDatabaseArgomenti = new ArrayList<>();


		fintoDatabaseUtenti.add(new Studente(1, "Mario", "Rossi", "m@mail.it", "mrossi", "123", "MAT01"));
		fintoDatabaseArgomenti.add(new ArgomentoTirocinio(1, "Sviluppo App", TipoTirocinio.INTERNO, null));
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
			System.out.println("Login effettuato da studente!");
		}

	}

	public List<ArgomentoTirocinio> getTuttiGliArgomenti() {
		return fintoDatabaseArgomenti;
	}

	public void richiediTirocinioPerStudente(ArgomentoTirocinio argomento) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			s.richiediTirocinio(argomento);
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
}