# **Documentazione del Sistema di Gestione Sedute di Laurea**

## **Introduzione e Scelte Architetturali**

Il diagramma delle classi modella l'architettura logica (Model) del sistema informativo per la gestione dei tirocini e delle sedute di laurea.

L'obiettivo principale è stato separare chiaramente gli "Attori" del sistema (le persone fisiche) dalle "Entità di Dominio" (i documenti e gli eventi gestiti), garantendo la massima tracciabilità dei dati.

---

## **La Gerarchia degli Utenti (Ereditarietà)**

La scelta progettuale più importante riguarda la gestione degli accessi. Per evitare la duplicazione dei dati anagrafici e delle credenziali, è stata creata una struttura gerarchica.

### **`Utente` (Classe Astratta)**

* **Significato:** È la "superclasse" radice del sistema. Rappresenta una persona generica registrata sulla piattaforma.  
* **Scelta della classe Astratta (`<<abstract>>`):** È stata resa astratta perché nel dominio applicativo non esiste un utente generico: chiunque acceda è necessariamente o uno Studente o un Docente. Questo impedisce l'istanziazione di utenti senza un ruolo specifico.  
* **Scelta degli attributi Protetti (`#`):** Gli attributi anagrafici (`id`, `nome`, `cognome`, `email`, `username`, `password`) sono impostati come *protected*. Questa è una scelta mirata a favorire le classi figlie: `Studente` e `Docente` erediteranno questi campi e potranno accedervi direttamente, mantenendoli però nascosti al resto del sistema esterno.

### **`Studente`**

* **Significato:** Eredita da `Utente` e rappresenta il candidato.  
* **Attributi:** Aggiunge `- matricola` (privato), un dato esclusivo di questo ruolo.  
* **Metodi:** Le operazioni `richiediTirocinio()` e `caricaTesi()` riflettono le azioni attive che lo studente compie sul sistema, prendendo in input gli oggetti necessari (rispettivamente l'argomento scelto e il file da allegare).

### **`Docente`**

* **Significato:** Eredita da `Utente`. Agisce come proponente di tirocini e relatore delle tesi.  
* **Metodi:** Contiene la logica di approvazione (`valutaRichiesta`, `valutaTesi`). Restituisce inoltre liste di dati, come `getTirociniInCorso()`.

### **`Coordinatore`**

* **Significato:** Eredita direttamente da `Docente` (che a sua volta eredita da `Utente`).  
* **Scelta progettuale:** Questa ereditarietà a cascata modella perfettamente la specifica "*Il coordinatore del corso di laurea è anch’esso un docente*". Il Coordinatore possiede tutte le capacità di un Docente (può avere tesisti), ma ha in più i privilegi amministrativi per inserire sedute (`inserisciSeduta()`) e generare la lista dei relatori (`formaCommissione()`).

---

## **Le Entità di Dominio**

Queste classi rappresentano gli oggetti gestiti dal sistema. I loro attributi sono mantenuti strettamente privati (`-`) per garantire il completo incapsulamento (i valori verranno letti tramite metodi "get" in fase di implementazione Java).

### **`ArgomentoTirocinio`**

* **Significato:** Rappresenta la proposta formativa creata da un docente.  
* **Attributi:** `titolo`, `tipo` (Interno/Esterno) e il `referenteAziendale`. Quest'ultimo sarà valorizzato solo se il tirocinio è di tipo esterno.

### **`RichiestaTirocinio`**

* **Significato:** È la classe di transizione ("classe associativa" logica) che unisce la volontà dello studente con l'argomento offerto.  
* **Scelta progettuale:** È fondamentale che esista come classe autonoma perché deve memorizzare il suo ciclo di vita temporale tramite l'attributo `stato` (In Attesa, Approvata, Rifiutata).

### **`Tesi`**

* **Significato:** Il prodotto finale del lavoro dello studente.  
* **Attributi:** Contiene il riferimento al documento fisico caricato (`filePath`) e uno `stato` indipendente per l'approvazione finale del relatore.

### **`SedutaLaurea`**

* **Significato:** L'evento conclusivo.  
* **Attributi:** Raggruppa le informazioni logistiche: `data`, `ora` e `luogo`.

---

## **Le Enumerazioni (`<<enumeration>>`)**

L'uso dei tipi enumerati è una "best practice" ingegneristica per garantire l'**integrità dei dati** e prevenire anomalie nel database o nel software.

* **`Stato` (IN\_ATTESA, APPROVATA, RIFIUTATA):** Condivisa da `RichiestaTirocinio` e `Tesi`. Impedisce l'inserimento di stati anomali causati da errori di battitura manuali.  
* **`TipoTirocinio` (INTERNO, ESTERNO):** Limita rigorosamente le categorie di tirocini ammesse dalla segreteria.

---

## **Le Relazioni e le Molteplicità (I Collegamenti)**

I collegamenti tra le classi descrivono le "regole di business" (vincoli) del sistema gestionale.

1. **Docente (1) \--- (\*) ArgomentoTirocinio**  
   * *Significato:* Un singolo docente può proporre infiniti argomenti nel tempo, ma un argomento specifico è inserito da esattamente un docente (che ne diventerà il relatore).  
2. **Studente (1) \--- (\*) RichiestaTirocinio**  
   * *Significato:* Uno studente può effettuare più richieste. Questa molteplicità (`*`) è necessaria perché, in caso di rifiuto di una richiesta, lo studente dovrà poterne compilare una nuova, generando uno storico.  
3. **RichiestaTirocinio (\*) \--- (1) ArgomentoTirocinio**  
   * *Significato:* Molti studenti (tramite le loro richieste) possono competere o candidarsi per lo stesso identico argomento proposto dal docente.  
4. **RichiestaTirocinio (1) \--- (0..1) Tesi**  
   * *Scelta progettuale critica:* Questo è il legame logico più raffinato. Una richiesta (se approvata) darà vita ad **al massimo una** tesi (`0..1`). Zero (`0`) rappresenta la fase in cui il tirocinio è in corso; Uno (`1`) indica che il tirocinio è concluso e la tesi è stata caricata. Questa associazione permette di risalire sempre al docente relatore partendo dalla Tesi.  
5. **Tesi (\*) \--- (1) SedutaLaurea**  
   * *Significato:* Durante una specifica giornata di seduta di laurea verranno discusse contemporaneamente numerose tesi (`*`), ma una determinata tesi viene presentata in una sola data esatta.  
6. **Coordinatore (1) \--- (\*) SedutaLaurea**  
   * *Significato:* Il coordinatore è la singola figura preposta alla creazione e organizzazione del calendario delle sedute.

