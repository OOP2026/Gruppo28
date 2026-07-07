package controller;

import model.*;
import dao.*;
import implementazionedao.*;
import database_connection.ConnessioneDatabase;
import gui.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La classe Controller gestisce la logica di business dell'applicazione.
 * Fa da intermediario tra l'interfaccia grafica (GUI), il modello dei dati (Model)
 * e l'accesso al database (DAO). Utilizza il pattern Singleton.
 */
public class Controller {

	private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
	private static Controller instance;

	private Utente utenteLoggato;
	private boolean studenteAvvisatoRifiuto = false;

	private final UtenteDAO utenteDao;
	private final StudenteDAO studenteDao;
	private final DocenteDAO docenteDao;
	private final ArgomentoTirocinioDAO argomentoDao;
	private final RichiestaTirocinioDAO richiestaDao;
	private final TesiDAO tesiDao;
	private final SedutaLaureaDAO sedutaDao;

	private Controller() {
		this.utenteDao = new UtentePostgresDao();
		this.studenteDao = new StudentePostgresDao();
		this.docenteDao = new DocentePostgresDao();
		this.argomentoDao = new ArgomentoTirocinioPostgresDao();
		this.richiestaDao = new RichiestaTirocinioPostgresDao();
		this.tesiDao = new TesiPostgresDao();
		this.sedutaDao = new SedutaLaureaPostgresDao();
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	public boolean login(String username, String password) {
		Utente u = utenteDao.autenticaUtente(username, password);

		if (u != null) {
			Studente s = studenteDao.getStudenteById(u.getId());
			if (s != null) {
				this.utenteLoggato = s;
				return true;
			}

			if (docenteDao.isCoordinatore(u.getId())) {
				this.utenteLoggato = new Coordinatore(u.getId(), u.getNome(), u.getCognome(), u.getEmail(), u.getUsername(), u.getPassword());
				return true;
			}

			Docente d = docenteDao.getDocenteById(u.getId());
			if (d != null) {
				this.utenteLoggato = d;
				return true;
			}

			this.utenteLoggato = u;
			return true;
		}
		return false;
	}

	/**
	 * Gestisce la logica di registrazione dell'utente delegando l'inserimento
	 * nel database alle implementazioni Postgres dei DAO.
	 *
	 * @param nome      Nome dell'utente.
	 * @param cognome   Cognome dell'utente.
	 * @param email     Email dell'utente.
	 * @param username  Username dell'utente.
	 * @param password  Password dell'utente.
	 * @param matricola Matricola dello studente (null o vuota per i docenti).
	 * @param ruolo     Ruolo dell'utente (Studente o Docente).
	 */
	public void registraUtente(String nome, String cognome, String email, String username, String password, String matricola, String ruolo) {
		if ("Studente".equals(ruolo)) {
			Studente nuovoStudente = new Studente(0, nome, cognome, email, username, password, matricola);
			StudentePostgresDao studenteDAO = new StudentePostgresDao();
			studenteDAO.salvaStudente(nuovoStudente);
		} else {
			Docente nuovoDocente = new Docente(0, nome, cognome, email, username, password);
			DocentePostgresDao docenteDAO = new DocentePostgresDao();
			docenteDAO.salvaDocente(nuovoDocente);
		}
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
		return argomentoDao.getAllArgomenti();
	}

	public void richiediTirocinioPerStudente(ArgomentoTirocinio argomento) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			s.richiediTirocinio(argomento);
			richiestaDao.salvaRichiesta(s.getRichiestaAttuale());
		}
	}

	public RichiestaTirocinio getRichiestaAggiornataPerStudente(int studenteId) {
		return richiestaDao.getRichiestaAttualeByStudente(studenteId);
	}

	public List<RichiestaTirocinio> getTirociniInCorsoAggiornati(int docenteId) {
		List<RichiestaTirocinio> tutti = richiestaDao.getRichiesteByDocente(docenteId);
		List<RichiestaTirocinio> inCorso = new ArrayList<>();
		for (RichiestaTirocinio r : tutti) {
			if (r.getStato() == Stato.APPROVATA) {
				inCorso.add(r);
			}
		}
		return inCorso;
	}

	public Tesi getTesiAggiornataPerStudente(int studenteId) {
		return tesiDao.getTesiByStudente(studenteId);
	}

	public List<SedutaLaurea> getTutteLeSedute() {
		return sedutaDao.getAllSedute();
	}

	public void aggiungiSeduta(LocalDate data, LocalTime ora, String luogo) {
		SedutaLaurea nuovaSeduta = new SedutaLaurea(0, data, ora, luogo);
		sedutaDao.salvaSeduta(nuovaSeduta);
	}

	/**
	 * Carica o aggiorna la tesi dello studente nel database.
	 * Se una tesi esiste già (magari rifiutata), effettua un aggiornamento;
	 * altrimenti, salva una nuova istanza.
	 *
	 * @param path   Percorso del file tesi.
	 * @param seduta Seduta di laurea selezionata.
	 */
	public void caricaTesiPerStudente(String path, SedutaLaurea seduta) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			RichiestaTirocinio r = richiestaDao.getRichiestaAttualeByStudente(s.getId());
			Tesi tesiEsistente = tesiDao.getTesiByStudente(s.getId());

			if (r != null && r.getStato() == Stato.APPROVATA) {
				if (tesiEsistente != null) {
					tesiDao.aggiornaTesi(tesiEsistente.getId(), path, seduta);
					LOGGER.info("Tesi aggiornata nel database con successo.");
				} else {
					Tesi nuovaTesi = new Tesi(0, path, seduta, s.getNome() + " " + s.getCognome());
					tesiDao.salvaTesi(nuovaTesi, s.getId());
					LOGGER.info("Tesi salvata nel database con successo.");
				}
			} else {
				LOGGER.warning("Errore: Impossibile caricare la tesi. Tirocinio non ancora approvato nel database.");
			}
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

			ArgomentoTirocinio nuovoArgomento = new ArgomentoTirocinio(0, titolo, tipo, referenteEffettivo, docente);
			docente.aggiungiArgomento(nuovoArgomento);
			argomentoDao.salvaArgomento(nuovoArgomento, docente.getId());
		}
	}

	public List<RichiestaTirocinio> getRichiesteInAttesa() {
		List<RichiestaTirocinio> inAttesa = new ArrayList<>();
		if (utenteLoggato instanceof Docente) {
			List<RichiestaTirocinio> tutte = richiestaDao.getRichiesteByDocente(utenteLoggato.getId());
			for (RichiestaTirocinio r : tutte) {
				if (r.getStato() == Stato.IN_ATTESA) {
					inAttesa.add(r);
				}
			}
		}
		return inAttesa;
	}

	public void valutaRichiestaComeDocente(RichiestaTirocinio richiesta, boolean accetta) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			docente.valutaRichiesta(richiesta, accetta);
			Stato nuovoStato = accetta ? Stato.APPROVATA : Stato.RIFIUTATA;
			richiestaDao.aggiornaStatoRichiesta(richiesta.getId(), nuovoStato, richiesta.getMotivazioneRifiuto());
		}
	}

	public List<Tesi> getTesiInAttesa() {
		List<Tesi> inAttesa = new ArrayList<>();
		String query = "SELECT id FROM tesi WHERE stato = 'IN_ATTESA'";

		try (PreparedStatement pst = ConnessioneDatabase.getInstance().prepareStatement(query);
		     ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				inAttesa.add(tesiDao.getTesiById(rs.getInt("id")));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return inAttesa;
	}

	public void valutaTesiComeDocente(Tesi tesi, boolean approvata) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			docente.valutaTesi(tesi, approvata);
			Stato nuovoStato = approvata ? Stato.APPROVATA : Stato.RIFIUTATA;
			tesiDao.aggiornaStatoTesi(tesi.getId(), nuovoStato);
		}
	}

	public List<String> getStudentiApprovatiPerSeduta(SedutaLaurea seduta) {
		List<String> lista = new ArrayList<>();
		String query = "SELECT studente_id FROM tesi WHERE seduta_id = ? AND stato = 'APPROVATA'";

		try (PreparedStatement pst = ConnessioneDatabase.getInstance().prepareStatement(query)) {
			pst.setInt(1, seduta.getId());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					int studenteId = rs.getInt("studente_id");
					Studente s = studenteDao.getStudenteById(studenteId);
					RichiestaTirocinio r = richiestaDao.getRichiestaAttualeByStudente(studenteId);

					if (s != null && r != null && r.getArgomento() != null) {
						lista.add(s.getNome() + " " + s.getCognome() + " (Relatore: Prof. " + r.getArgomento().getReferente() + ")");
					} else if (s != null) {
						lista.add(s.getNome() + " " + s.getCognome());
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return lista;
	}

	public List<String> getCommissionePerSeduta(SedutaLaurea seduta) {
		List<String> commissione = new ArrayList<>();
		String query = "SELECT studente_id FROM tesi WHERE seduta_id = ? AND stato = 'APPROVATA'";

		try (PreparedStatement pst = ConnessioneDatabase.getInstance().prepareStatement(query)) {
			pst.setInt(1, seduta.getId());
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					int studenteId = rs.getInt("studente_id");
					RichiestaTirocinio r = richiestaDao.getRichiestaAttualeByStudente(studenteId);

					if (r != null && r.getArgomento() != null) {
						String relatore = r.getArgomento().getReferente();
						if (!commissione.contains(relatore)) {
							commissione.add(relatore);
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
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