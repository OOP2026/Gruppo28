package model;

/**
 * Rappresenta un utente generico del sistema.
 * È la classe base (superclasse) da cui ereditano tutti gli attori specifici
 * dell'applicazione (Studente, Docente, Coordinatore).
 * Centralizza i dati anagrafici e le credenziali di autenticazione comuni a tutti.
 */
public class Utente {
    protected int id;
    protected String nome;
    protected String cognome;
    protected String email;
    protected String username;
    protected String password;


    public Utente(int id, String nome, String cognome, String email, String username, String password) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() { return email; }
}