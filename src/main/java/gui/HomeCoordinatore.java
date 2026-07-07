package gui;

import controller.Controller;
import model.Docente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finestra principale (Dashboard) per l'utente con ruolo di Coordinatore.
 * Fornisce l'accesso a tutte le funzionalità standard di un docente (gestione argomenti,
 * richieste e tesi) con l'aggiunta delle funzionalità esclusive per la creazione
 * delle sedute di laurea e la formazione delle commissioni.
 */
public class HomeCoordinatore extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(HomeCoordinatore.class.getName());
    private static final String FONT_FAMILY = "Segoe UI";

    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnCreaSeduta;
    private JButton btnGestisciCommissione;
    private JButton btnAggiungiArgomento;
    private JButton btnGestisciRichieste;
    private JButton btnTirociniInCorso;
    private JButton btnValutaTesi;
    private JButton btnLogout;
    private JLabel lblLogo;
    private JLabel lblTitolo;
    private JLabel lblFunzioniCoordinatore;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, imposta un messaggio di benvenuto personalizzato
     * recuperando i dati dal Controller e configura i collegamenti a tutte le altre
     * schermate del sistema tramite i pulsanti del menu.
     */
    public HomeCoordinatore() {
        setContentPane(mainPanel);
        setTitle("Dashboard Coordinatore");
        setSize(700, 750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        Color coloreRosso = new Color(200, 0, 0);
        Font fontBottoni = new Font(FONT_FAMILY, Font.BOLD, 16);

        try {
            ImageIcon originalIcon = new ImageIcon("logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledImage));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Immagine logo non trovata.", e);
        }

        lblTitolo.setText("HOME");
        lblTitolo.setForeground(bluIstituzionale);
        lblTitolo.setFont(new Font(FONT_FAMILY, Font.BOLD, 40));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);

        Docente coordinatoreLoggato = (Docente) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("<html>Benvenuto Coordinatore Prof. " + coordinatoreLoggato.getCognome() + "<br><br>Selezioni la funzione di cui vuole usufruire:</html>");
        lblBenvenuto.setForeground(bluIstituzionale);
        lblBenvenuto.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));

        lblFunzioniCoordinatore.setForeground(bluIstituzionale);
        lblFunzioniCoordinatore.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));

        JButton[] bottoniBlu = {btnCreaSeduta, btnGestisciCommissione, btnAggiungiArgomento, btnGestisciRichieste, btnTirociniInCorso, btnValutaTesi};
        for (JButton btn : bottoniBlu) {
            btn.setBackground(bluIstituzionale);
            btn.setForeground(Color.WHITE);
            btn.setFont(fontBottoni);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);
        }

        btnLogout.setBackground(coloreRosso);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(fontBottoni);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(true);

        btnCreaSeduta.addActionListener(e -> new CreaSeduta().setVisible(true));

        btnGestisciCommissione.addActionListener(e -> {
            new GestioneCommissione().setVisible(true);
            dispose();
        });

        btnAggiungiArgomento.addActionListener(e -> new CreaArgomento().setVisible(true));

        btnGestisciRichieste.addActionListener(e -> {
            new GestisciRichieste().setVisible(true);
            dispose();
        });

        btnTirociniInCorso.addActionListener(e -> {
            new TirociniInCorso().setVisible(true);
            dispose();
        });

        btnValutaTesi.addActionListener(e -> {
            new ValutaTesi().setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}