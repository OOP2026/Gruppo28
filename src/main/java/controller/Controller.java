package controller;

import model.*;
import dao.*;
import implementazioneDao.*;
import database_connection.ConnessioneDatabase;
import gui.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Controller gestisce la logica di business dell'applicazione.
 * Fa da intermediario tra l'interfaccia grafica (GUI), il modello dei dati (Model)
 * e l'accesso al database (DAO). Utilizza il pattern Singleton.
 */
public class Controller {

	private static Controller instance;

	private Utente utenteLoggato;
	private boolean studenteAvvisatoRifiuto = false;

	private UtenteDAO utenteDao;
	private StudenteDAO studenteDao;
	private DocenteDAO docenteDao;
	private ArgomentoTirocinioDAO argomentoDao;
	private RichiestaTirocinioDAO richiestaDao;
	private TesiDAO tesiDao;
	private SedutaLaureaDAO sedutaDao;

	/**
	 * Costruttore privato del Controller.
	 * Inizializza tutte le istanze dei DAO per la comunicazione con PostgreSQL.
	 */
	private Controller() {
		this.utenteDao = new UtentePostgresDao();
		this.studenteDao = new StudentePostgresDao();
		this.docenteDao = new DocentePostgresDao();
		this.argomentoDao = new ArgomentoTirocinioPostgresDao();
		this.richiestaDao = new RichiestaTirocinioPostgresDao();
		this.tesiDao = new TesiPostgresDao();
		this.sedutaDao = new SedutaLaureaPostgresDao();
	}

	/**
	 * Restituisce l'unica istanza del Controller (Pattern Singleton).
	 *
	 * @return l'istanza del Controller
	 */
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	/**
	 * Gestisce l'autenticazione dell'utente nel sistema.
	 * Verifica le credenziali sul database e determina il ruolo specifico dell'utente (Studente, Docente, Coordinatore).
	 *
	 * @param username Il nome utente inserito
	 * @param password La password inserita
	 * @return true se l'autenticazione ha successo, false altrimenti
	 */
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
	 * Restituisce l'utente attualmente loggato nel sistema.
	 *
	 * @return l'oggetto Utente loggato
	 */
	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

	/**
	 * Apre la finestra Home corrispondente al ruolo dell'utente loggato.
	 */
	public void apriHomeUtente() {
		if (utenteLoggato instanceof Coordinatore) {
			new HomeCoordinatore().setVisible(true);
		} else if (utenteLoggato instanceof Studente) {
			new HomeStudente().setVisible(true);
		} else if (utenteLoggato instanceof Docente) {
			new HomeDocente().setVisible(true);
		}
	}

	/**
	 * Recupera la lista di tutti gli argomenti di tirocinio disponibili.
	 *
	 * @return Lista di ArgomentoTirocinio
	 */
	public List<ArgomentoTirocinio> getTuttiGliArgomenti() {
		return argomentoDao.getAllArgomenti();
	}

	/**
	 * Permette allo studente loggato di inviare una richiesta per un tirocinio.
	 * Salva la richiesta sul database.
	 *
	 * @param argomento L'argomento di tirocinio selezionato
	 */
	public void richiediTirocinioPerStudente(ArgomentoTirocinio argomento) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			s.richiediTirocinio(argomento);
			richiestaDao.salvaRichiesta(s.getRichiestaAttuale());
		}
	}

	/**
	 * Recupera tutte le sedute di laurea programmate dal database.
	 *
	 * @return Lista di SedutaLaurea
	 */
	public List<SedutaLaurea> getTutteLeSedute() {
		return sedutaDao.getAllSedute();
	}

	/**
	 * Aggiunge una nuova seduta di laurea al database.
	 *
	 * @param data La data della seduta
	 * @param ora L'orario della seduta
	 * @param luogo Il luogo della seduta (es. Aula Magna)
	 */
	public void aggiungiSeduta(LocalDate data, LocalTime ora, String luogo) {
		SedutaLaurea nuovaSeduta = new SedutaLaurea(0, data, ora, luogo);
		sedutaDao.salvaSeduta(nuovaSeduta);
	}

	/**
	 * Permette allo studente loggato di caricare il file della propria tesi.
	 * La tesi viene salvata sul database solo se il tirocinio è stato precedentemente approvato.
	 *
	 * @param path Il percorso del file della tesi
	 * @param seduta La seduta di laurea a cui lo studente vuole prenotarsi
	 */
	public void caricaTesiPerStudente(String path, SedutaLaurea seduta) {
		if (utenteLoggato instanceof Studente) {
			Studente s = (Studente) utenteLoggato;
			RichiestaTirocinio r = richiestaDao.getRichiestaAttualeByStudente(s.getId());
			Tesi nuovaTesi = new Tesi(0, path, seduta, s.getNome() + " " + s.getCognome());

			if (r != null && r.getStato() == Stato.APPROVATA) {
				tesiDao.salvaTesi(nuovaTesi, s.getId());
				System.out.println("Tesi salvata nel database con successo.");
			} else {
				System.out.println("Errore: Impossibile caricare la tesi. Tirocinio non ancora approvato nel database.");
			}
		}
	}

	/**
	 * Permette al docente loggato di inserire un nuovo argomento di tirocinio proponibile agli studenti.
	 *
	 * @param titolo Il titolo del tirocinio
	 * @param tipo INTERNO o ESTERNO
	 * @param referente Il nome dell'eventuale referente aziendale (se esterno)
	 */
	public void aggiungiNuovoArgomento(String titolo, TipoTirocinio tipo, String referente) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			String referenteEffettivo;

			if (tipo == TipoTirocinio.INTERNO) {
				referenteEffettivo = docente.getNome() + " " + docente.getCognome();
			} else {
				referenteEffettivo = referente;
			}

			ArgomentoTirocinio nuovoArgomento = new ArgomentoTirocinio(0, titolo, tipo, referenteEffettivo);
			docente.aggiungiArgomento(nuovoArgomento);
			argomentoDao.salvaArgomento(nuovoArgomento, docente.getId());
		}
	}

	/**
	 * Recupera le richieste di tirocinio in attesa di valutazione destinate al docente loggato.
	 *
	 * @return Lista di RichiestaTirocinio con stato IN_ATTESA
	 */
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

	/**
	 * Permette al docente di accettare o rifiutare una richiesta di tirocinio, aggiornando il database.
	 *
	 * @param richiesta La richiesta da valutare
	 * @param accetta true se approvata, false se rifiutata
	 */
	public void valutaRichiestaComeDocente(RichiestaTirocinio richiesta, boolean accetta) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			docente.valutaRichiesta(richiesta, accetta);
			Stato nuovoStato = accetta ? Stato.APPROVATA : Stato.RIFIUTATA;
			richiestaDao.aggiornaStatoRichiesta(richiesta.getId(), nuovoStato, richiesta.getMotivazioneRifiuto());
		}
	}

	/**
	 * Recupera tutte le tesi attualmente in attesa di valutazione.
	 *
	 * @return Lista di Tesi con stato IN_ATTESA
	 */
	public List<Tesi> getTesiInAttesa() {
		List<Tesi> inAttesa = new ArrayList<>();
		String query = "SELECT id FROM tesi WHERE stato = 'IN_ATTESA'";

		try (PreparedStatement pst = ConnessioneDatabase.getInstance().prepareStatement(query);
		     ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				inAttesa.add(tesiDao.getTesiById(rs.getInt("id")));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return inAttesa;
	}

	/**
	 * Permette al docente di approvare o rifiutare una tesi caricata, aggiornando il database.
	 *
	 * @param tesi La tesi da valutare
	 * @param approvata true se approvata, false se rifiutata
	 */
	public void valutaTesiComeDocente(Tesi tesi, boolean approvata) {
		if (utenteLoggato instanceof Docente) {
			Docente docente = (Docente) utenteLoggato;
			docente.valutaTesi(tesi, approvata);
			Stato nuovoStato = approvata ? Stato.APPROVATA : Stato.RIFIUTATA;
			tesiDao.aggiornaStatoTesi(tesi.getId(), nuovoStato);
		}
	}

	/**
	 * Restituisce un elenco formattato di studenti la cui tesi è stata approvata per una specifica seduta.
	 *
	 * @param seduta La seduta di laurea di riferimento
	 * @return Lista di stringhe contenenti i dati degli studenti
	 */
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
			System.err.println(e.getMessage());
		}
		return lista;
	}

	/**
	 * Recupera i nomi dei relatori degli studenti assegnati a una determinata seduta di laurea,
	 * per comporre la commissione. Evita duplicati.
	 *
	 * @param seduta La seduta di laurea
	 * @return Lista di nomi dei docenti in commissione
	 */
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
			System.err.println(e.getMessage());
		}
		return commissione;
	}

	/**
	 * Verifica se lo studente è già stato avvisato di un eventuale rifiuto.
	 *
	 * @return true se è stato avvisato, false altrimenti
	 */
	public boolean isStudenteAvvisatoRifiuto() {
		return studenteAvvisatoRifiuto;
	}

	/**
	 * Imposta lo stato di notifica per lo studente riguardo a un rifiuto.
	 *
	 * @param avvisato Il nuovo stato della notifica
	 */
	public void setStudenteAvvisatoRifiuto(boolean avvisato) {
		this.studenteAvvisatoRifiuto = avvisato;
	}
}