package controller;

import model.*;
import gui.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {

	private static Controller instance;

	private Utente utenteLoggato;
	private List<Utente> fintoDatabaseUtenti;
	private List<ArgomentoTirocinio> fintoDatabaseArgomenti;
	private List<RichiestaTirocinio> tutteLeRichieste = new ArrayList<>();
	private List<Tesi> tutteLeTesi = new ArrayList<>();
	private List<SedutaLaurea> tutteLeSedute = new ArrayList<>();
	private boolean studenteAvvisatoRifiuto = false;

	private Controller() {
		fintoDatabaseUtenti = new ArrayList<>();
		fintoDatabaseArgomenti = new ArrayList<>();

		Studente studente1 = new Studente(1, "Mario", "Rossi", "m.rossi@mail.it", "mrossi", "123", "MAT01");
		Studente studente2 = new Studente(4, "Giulia", "Bianchi", "g.bianchi@mail.it", "gbianchi", "123", "MAT02");
		Docente docente = new Docente(2, "Luigi", "Verdi", "l.verdi@mail.it", "lverdi", "456");
		Coordinatore coord = new Coordinatore(3, "Anna", "Neri", "anna@mail.it", "coord", "admin");

		fintoDatabaseUtenti.add(studente1);
		fintoDatabaseUtenti.add(studente2);
		fintoDatabaseUtenti.add(docente);
		fintoDatabaseUtenti.add(coord);

		ArgomentoTirocinio argomento = new ArgomentoTirocinio(1, "Sviluppo App", TipoTirocinio.INTERNO, docente.getNome() + " " + docente.getCognome());
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
		if (utenteLoggato instanceof Coordinatore) {
			new HomeCoordinatore().setVisible(true);
		} else if (utenteLoggato instanceof Studente) {
			new HomeStudente().setVisible(true);
		} else if (utenteLoggato instanceof Docente) {
			new HomeDocente().setVisible(true);
		}
	}

	public List<ArgomentoTirocinio> getTuttiGliArgomenti() {
		return fintoDatabaseArgomenti;
	}

	public void richiediTirocinioPerStudente(ArgomentoTirocinio argomento) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			s.richiediTirocinio(argomento);
			tutteLeRichieste.add(s.getRichiestaAttuale());
		}
	}

	public List<SedutaLaurea> getTutteLeSedute() {
		return tutteLeSedute;
	}

	public void aggiungiSeduta(LocalDate data, LocalTime ora, String luogo) {
		int id = tutteLeSedute.size() + 1;
		tutteLeSedute.add(new SedutaLaurea(id, data, ora, luogo));
	}

	public void caricaTesiPerStudente(String path, SedutaLaurea seduta) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			int nuovoId = tutteLeTesi.size() + 1;
			Tesi nuovaTesi = new Tesi(nuovoId, path, seduta, s.getNome() + " " + s.getCognome());
			s.caricaTesi(nuovaTesi);
			tutteLeTesi.add(nuovaTesi);
		}
	}

	public void aggiungiNuovoArgomento(String titolo, TipoTirocinio tipo, String referente) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;

			String referenteEffettivo;
			if (tipo == TipoTirocinio.INTERNO) {
				referenteEffettivo = docente.getNome() + " " + docente.getCognome();
			} else {
				referenteEffettivo = referente;
			}

			int nuovoId = getTuttiGliArgomenti().size() + 1;
			ArgomentoTirocinio nuovoArgomento = new ArgomentoTirocinio(nuovoId, titolo, tipo, referenteEffettivo);
			docente.aggiungiArgomento(nuovoArgomento);
			getTuttiGliArgomenti().add(nuovoArgomento);
		}
	}

	public List<RichiestaTirocinio> getRichiesteInAttesa() {
		List<RichiestaTirocinio> inAttesa = new ArrayList<>();
		for (RichiestaTirocinio r : tutteLeRichieste) {
			if (r.getStato() == Stato.IN_ATTESA) {
				inAttesa.add(r);
			}
		}
		return inAttesa;
	}

	public void valutaRichiestaComeDocente(RichiestaTirocinio richiesta, boolean accetta) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			docente.valutaRichiesta(richiesta, accetta);
		}
	}

	public List<Tesi> getTesiInAttesa() {
		List<Tesi> inAttesa = new ArrayList<>();
		for (Tesi t : tutteLeTesi) {
			if (t.getStato() == Stato.IN_ATTESA) {
				inAttesa.add(t);
			}
		}
		return inAttesa;
	}

	public void valutaTesiComeDocente(Tesi tesi, boolean approvata) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			docente.valutaTesi(tesi, approvata);
		}
	}

	public List<String> getStudentiApprovatiPerSeduta(SedutaLaurea seduta) {
		List<String> lista = new ArrayList<>();
		for (Utente u : fintoDatabaseUtenti) {
			if (u instanceof Studente) {
				Studente s = (Studente) u;
				Tesi t = s.getTesi();
				if (t != null && t.getSeduta().getId() == seduta.getId() && t.getStato() == Stato.APPROVATA) {
					lista.add(s.getNome() + " " + s.getCognome() + " (Relatore: Prof. " + s.getRichiestaAttuale().getArgomento().getReferente() + ")");
				}
			}
		}
		return lista;
	}

	public List<String> getCommissionePerSeduta(SedutaLaurea seduta) {
		List<String> commissione = new ArrayList<>();
		for (Utente u : fintoDatabaseUtenti) {
			if (u instanceof Studente) {
				Studente s = (Studente) u;
				Tesi t = s.getTesi();
				if (t != null && t.getSeduta().getId() == seduta.getId() && t.getStato() == Stato.APPROVATA) {
					String relatore = s.getRichiestaAttuale().getArgomento().getReferente();
					if (!commissione.contains(relatore)) {
						commissione.add(relatore);
					}
				}
			}
		}
		return commissione;
	}
	public boolean isStudenteAvvisatoRifiuto() {
		return studenteAvvisatoRifiuto;
	}

	public void setStudenteAvvisatoRifiuto(boolean avvisato) {
		this.studenteAvvisatoRifiuto = avvisato;
	}
}