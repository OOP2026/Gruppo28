package gui;

import controller.Controller;
import model.Docente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeDocente extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnAggiungiArgomento;
    private JButton btnGestisciRichieste;
    private JButton btnTirociniInCorso;
    private JButton btnValutaTesi;
    private JButton btnLogout;

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
                JOptionPane.showMessageDialog(mainPanel, "Funzione Tirocini in Corso in arrivo!");
            }
        });

        btnValutaTesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Funzione Valuta Tesi in arrivo!");
            }
        });
    }
}