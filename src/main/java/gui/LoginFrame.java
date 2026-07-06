package gui;

import controller.Controller;
import model.Docente;
import model.Studente;
import model.Utente;

import javax.swing.*;

/**
 * Finestra iniziale di login dell'applicazione.
 * Permette a tutti gli attori del sistema (Studenti, Docenti o Coordinatori)
 * di inserire le proprie credenziali per autenticarsi e accedere alle rispettive aree riservate.
 */
public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, configura la navigazione rapida
     * tramite il tasto Invio tra i campi di testo e definisce l'azione
     * del pulsante di accesso, delegando il controllo delle credenziali al Controller.
     */
    public LoginFrame() {
        setContentPane(mainPanel);
        setTitle("Sistema Gestione Lauree - Accesso");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        txtPassword.addActionListener(e -> btnLogin.doClick());

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            boolean successo = Controller.getInstance().login(username, password);

            if (successo) {
                Utente utenteLoggato = Controller.getInstance().getUtenteLoggato();
                String tipoAccesso = "Utente";

                if (utenteLoggato instanceof Studente) {
                    tipoAccesso = "Studente";
                } else if (utenteLoggato instanceof Docente) {
                    tipoAccesso = "Docente";
                }

                JOptionPane.showMessageDialog(mainPanel, "Accesso eseguito come " + tipoAccesso + "!");

                dispose();

                Controller.getInstance().apriHomeUtente();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}