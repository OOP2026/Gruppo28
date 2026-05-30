package gui;

import controller.Controller;
import model.Docente;
import model.RichiestaTirocinio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TirociniInCorso extends JFrame {
    private JPanel mainPanel;
    private JTextArea txtAreaTirocini;
    private JButton btnIndietro;

    public TirociniInCorso() {
        setContentPane(mainPanel);
        setTitle("Elenco Tirocini in Corso");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        popolaListaTirocini();

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().apriHomeUtente();
                dispose();
            }
        });
    }

    private void popolaListaTirocini() {
        Docente docenteLoggato = (Docente) Controller.getInstance().getUtenteLoggato();

        List<RichiestaTirocinio> inCorso = docenteLoggato.getTirociniInCorso();

        if (inCorso == null || inCorso.isEmpty()) {
            txtAreaTirocini.setText("Nessun tirocinio in corso al momento.");
            return;
        }

        StringBuilder testo = new StringBuilder();
        for (RichiestaTirocinio r : inCorso) {
            testo.append(r.toString()).append("\n");
            testo.append("--------------------------------------------------\n");
        }

        txtAreaTirocini.setText(testo.toString());
    }
}