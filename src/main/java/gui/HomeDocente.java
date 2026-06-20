package gui;

import controller.Controller;
import model.Docente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Finestra principale (Dashboard) per l'utente con ruolo di Docente.
 * Fornisce l'accesso alle funzionalità di gestione degli argomenti proposti,
 * valutazione delle richieste di tirocinio, monitoraggio dei tirocini in corso
 * e approvazione delle tesi finali.
 */
public class HomeDocente extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnAggiungiArgomento;
    private JButton btnGestisciRichieste;
    private JButton btnTirociniInCorso;
    private JButton btnValutaTesi;
    private JButton btnLogout;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, recupera i dati del docente loggato
     * dal Controller per impostare un messaggio di benvenuto personalizzato
     * e configura i collegamenti alle altre schermate di gestione.
     */
    public HomeDocente() {
        setContentPane(mainPanel);
        setSize(500, 400);
        setTitle("Pannello di Controllo - Docente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Docente docenteLoggato = (Docente) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("Benvenuto Prof. " + docenteLoggato.getCognome());

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        btnAggiungiArgomento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreaArgomento finestraCrea = new CreaArgomento();
                finestraCrea.setVisible(true);
            }
        });

        btnGestisciRichieste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestisciRichieste finestraRichieste = new GestisciRichieste();
                finestraRichieste.setVisible(true);
                dispose();
            }
        });

        btnTirociniInCorso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TirociniInCorso finestraElenco = new TirociniInCorso();
                finestraElenco.setVisible(true);
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
    }
}