package gui;

import controller.Controller;
import model.Docente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeCoordinatore extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto; // Aggiunta l'etichetta mancante
    private JButton btnCreaSeduta;
    private JButton btnGestisciCommissione;
    private JButton btnAggiungiArgomento;
    private JButton btnGestisciRichieste;
    private JButton btnTirociniInCorso;
    private JButton btnValutaTesi;
    private JButton btnLogout;

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