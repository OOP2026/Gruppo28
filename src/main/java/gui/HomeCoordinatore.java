package gui;

import controller.Controller;
import model.Docente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra principale (Dashboard) per l'utente con ruolo di Coordinatore.
 * Fornisce l'accesso a tutte le funzionalità standard di un docente (gestione argomenti,
 * richieste e tesi) con l'aggiunta delle funzionalità esclusive per la creazione
 * delle sedute di laurea e la formazione delle commissioni.
 */
public class HomeCoordinatore extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnCreaSeduta;
    private JButton btnGestisciCommissione;
    private JButton btnAggiungiArgomento;
    private JButton btnGestisciRichieste;
    private JButton btnTirociniInCorso;
    private JButton btnValutaTesi;
    private JButton btnLogout;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, imposta un messaggio di benvenuto personalizzato
     * recuperando i dati dal Controller e configura i collegamenti a tutte le altre
     * schermate del sistema tramite i pulsanti del menu.
     */
    public HomeCoordinatore() {
        setContentPane(mainPanel);
        setTitle("Dashboard Coordinatore");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Docente coordinatoreLoggato = (Docente) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("Benvenuto Coordinatore Prof. " + coordinatoreLoggato.getCognome());

        btnCreaSeduta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaSeduta().setVisible(true);
            }
        });

        btnGestisciCommissione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestioneCommissione().setVisible(true);
                dispose();
            }
        });

        btnAggiungiArgomento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaArgomento().setVisible(true);
            }
        });

        btnGestisciRichieste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestisciRichieste().setVisible(true);
                dispose();
            }
        });

        btnTirociniInCorso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TirociniInCorso().setVisible(true);
                dispose();
            }
        });

        btnValutaTesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ValutaTesi().setVisible(true);
                dispose();
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }
}