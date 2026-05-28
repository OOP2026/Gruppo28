package main;

import model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		System.out.println("--- 1. INIZIALIZZAZIONE DEGLI UTENTI ---");
		Studente studente = new Studente(1, "Mario", "Rossi", "mario.rossi@studenti.it", "mrossi", "pass123", "MAT123456");
		Docente docente = new Docente(2, "Luigi", "Bianchi", "luigi.bianchi@uni.it", "lbianchi", "prof123");
		Coordinatore coord = new Coordinatore(3, "Anna", "Verdi", "anna.verdi@uni.it", "averdi", "coord123");


		System.out.println("\n--- 2. CREAZIONE DEI DATI DI BASE ---");
		// Il coordinatore inserisce una seduta di laurea
		SedutaLaurea sedutaLuglio = new SedutaLaurea(10, LocalDate.of(2025, 7, 20), LocalTime.of(9, 0), "Aula Magna");
		coord.inserisciSeduta(sedutaLuglio);

		// Il docente propone un argomento di tirocinio
		ArgomentoTirocinio argWeb = new ArgomentoTirocinio(100, "Sviluppo Portale Web in Java", TipoTirocinio.INTERNO, null);
		docente.aggiungiArgomento(argWeb);


		System.out.println("\n--- 3. FLUSSO TIROCINIO: RICHIESTA E APPROVAZIONE ---");
		// Lo studente richiede l'argomento proposto
		studente.richiediTirocinio(argWeb);

		// Simuliamo che il docente veda la richiesta nel sistema e la approvi (true)
		RichiestaTirocinio richiestaDiMario = studente.getRichiestaAttuale();
		docente.valutaRichiesta(richiestaDiMario, true);


		System.out.println("\n--- 4. FLUSSO TESI: CARICAMENTO E VALUTAZIONE ---");
		// Lo studente carica la tesi e si prenota per la seduta di Luglio
		// (Se il docente avesse rifiutato il tirocinio, questo metodo stamperebbe un errore!)
		studente.caricaTesi("C:/documenti/Tesi_Mario_Rossi.pdf", sedutaLuglio);

		// Simuliamo che il relatore riceva il file e lo approvi
		Tesi tesiDiMario = studente.getTesi();
		docente.valutaTesi(tesiDiMario, true);


		System.out.println("\n--- 5. ORGANIZZAZIONE DELLA SEDUTA ---");
		// Il coordinatore genera la commissione per la seduta di Luglio
		List<Docente> commissione = coord.formaCommissione(sedutaLuglio);

		System.out.println("Numero di docenti nella commissione: " + commissione.size());
		System.out.println("Presidente di commissione: " + commissione.get(0).getNome() + " " + commissione.get(0).getCognome());

		System.out.println("\n--- TEST COMPLETATO CON SUCCESSO ---");
	}
}