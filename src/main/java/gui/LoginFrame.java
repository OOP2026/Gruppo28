package gui;

import controller.Controller;
import model.Studente;
import model.Utente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finestra iniziale di login dell'applicazione.
 * Permette a tutti gli attori del sistema (Studenti, Docenti o Coordinatori)
 * di inserire le proprie credenziali per autenticarsi e accedere alle rispettive aree riservate.
 */
public class LoginFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(LoginFrame.class.getName());

    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblLogo;
    private JButton btnRegistrati;

    public LoginFrame() {
        setContentPane(mainPanel);
        setTitle("Sistema Gestione Lauree - Accesso");
        setSize(750, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        Color bluIstituzionale = new Color(0, 51, 102);

        for (Component c : mainPanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(bluIstituzionale);
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }
        }

        btnLogin.setBackground(bluIstituzionale);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(true);

        btnRegistrati.setBackground(bluIstituzionale);
        btnRegistrati.setForeground(Color.WHITE);
        btnRegistrati.setOpaque(true);
        btnRegistrati.setBorderPainted(false);
        btnRegistrati.setContentAreaFilled(true);

        try {
            ImageIcon icon = new ImageIcon("logo.png");
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Immagine non trovata", e);
        }

        Font fontTesto = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBottone = new Font("Segoe UI", Font.BOLD, 14);

        txtUsername.setFont(fontTesto);
        txtPassword.setFont(fontTesto);
        btnLogin.setFont(fontBottone);
        btnRegistrati.setFont(fontBottone);

        txtUsername.addActionListener(e -> txtPassword.requestFocus());
        txtPassword.addActionListener(e -> btnLogin.doClick());

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            boolean successo = Controller.getInstance().login(username, password);

            if (successo) {
                Utente utenteLoggato = Controller.getInstance().getUtenteLoggato();
                String tipoAccesso = (utenteLoggato instanceof Studente) ? "Studente" : "Docente";

                JOptionPane.showMessageDialog(mainPanel, "Accesso eseguito come " + tipoAccesso + "!");
                dispose();
                Controller.getInstance().apriHomeUtente();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegistrati.addActionListener(e -> {
            RegistrazioneFrame regFrame = new RegistrazioneFrame();
            regFrame.setVisible(true);
        });
    }
}