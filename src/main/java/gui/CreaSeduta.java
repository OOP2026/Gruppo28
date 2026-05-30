package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CreaSeduta extends JFrame {
    private JPanel mainPanel;
    private JTextField txtData;
    private JTextField txtOra;
    private JTextField txtLuogo;
    private JButton btnSalva;
    private JButton btnIndietro;

    public CreaSeduta() {
        setContentPane(mainPanel);
        setTitle("Nuova Seduta di Laurea");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gestione del tasto INVIO per spostarsi tra i campi e salvare
        txtData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtOra.requestFocus(); // Va al campo ora
            }
        });

        txtOra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtLuogo.requestFocus(); // Va al campo luogo
            }
        });

        txtLuogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiSalvataggio(); // Premendo invio sul luogo, salva direttamente!
            }
        });

        // Cliccando sul bottone Salva
        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiSalvataggio();
            }
        });

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().apriHomeUtente();
                dispose();
            }
        });
    }

    private void eseguiSalvataggio() {
        try {
            // Definiamo il formato italiano GG-MM-AAAA
            DateTimeFormatter formattatoreData = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Leggiamo la data usando il nuovo formato italiano
            LocalDate data = LocalDate.parse(txtData.getText().trim(), formattatoreData);
            LocalTime ora = LocalTime.parse(txtOra.getText().trim());
            String luogo = txtLuogo.getText().trim();

            if (luogo.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Inserisci il luogo della seduta!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Controller.getInstance().aggiungiSeduta(data, ora, luogo);
            JOptionPane.showMessageDialog(mainPanel, "Seduta inserita con successo!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Errore! Usa il formato GG-MM-AAAA per la data (es. 30-05-2026) e HH:MM per l'ora.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}