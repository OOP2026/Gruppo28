package gui;

import controller.Controller;
import model.Docente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finestra principale (Dashboard) per l'utente con ruolo di Docente.
 * Fornisce l'accesso alle funzionalità di gestione degli argomenti proposti,
 * valutazione delle richieste di tirocinio, monitoraggio dei tirocini in corso
 * e approvazione delle tesi finali.
 */
public class HomeDocente extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(HomeDocente.class.getName());
    private static final String FONT_FAMILY = "Segoe UI";

    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnAggiungiArgomento;
    private JButton btnGestisciRichieste;
    private JButton btnTirociniInCorso;
    private JButton btnValutaTesi;
    private JButton btnLogout;
    private JLabel lblLogo;
    private JLabel lblTitolo;

    public HomeDocente() {
        setContentPane(mainPanel);
        setSize(650, 600);
        setTitle("Pannello di Controllo - Docente");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        Color coloreRosso = new Color(200, 0, 0);
        Font fontBottoni = new Font(FONT_FAMILY, Font.BOLD, 16);

        try {
            ImageIcon originalIcon = new ImageIcon("logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Immagine logo non trovata.", e);
        }

        lblTitolo.setText("HOME");
        lblTitolo.setForeground(bluIstituzionale);
        lblTitolo.setFont(new Font(FONT_FAMILY, Font.BOLD, 40));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);

        Docente docenteLoggato = (Docente) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("<html>Benvenuto Prof. " + docenteLoggato.getCognome() + "<br><br>Selezioni la funzione di cui vuole usufruire:</html>");
        lblBenvenuto.setForeground(bluIstituzionale);
        lblBenvenuto.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));

        JButton[] bottoniBlu = {btnAggiungiArgomento, btnGestisciRichieste, btnTirociniInCorso, btnValutaTesi};
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

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        btnAggiungiArgomento.addActionListener(e -> {
            CreaArgomento finestraCrea = new CreaArgomento();
            finestraCrea.setVisible(true);
        });

        btnGestisciRichieste.addActionListener(e -> {
            GestisciRichieste finestraRichieste = new GestisciRichieste();
            finestraRichieste.setVisible(true);
            dispose();
        });

        btnTirociniInCorso.addActionListener(e -> {
            TirociniInCorso finestraElenco = new TirociniInCorso();
            finestraElenco.setVisible(true);
            dispose();
        });

        btnValutaTesi.addActionListener(e -> {
            new ValutaTesi().setVisible(true);
            dispose();
        });
    }
}