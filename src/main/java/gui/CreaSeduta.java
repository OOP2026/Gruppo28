package gui;

import controller.Controller;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Finestra grafica che permette al Coordinatore di inserire
 * una nuova seduta di laurea programmata nel sistema.
 */
public class CreaSeduta extends JFrame {
    private JPanel mainPanel;
    private JTextField txtData;
    private JTextField txtOra;
    private JTextField txtLuogo;
    private JButton btnSalva;
    private JButton btnIndietro;

    /**
     * Costruttore della finestra.
     * Inizializza i componenti grafici, imposta le azioni dei bottoni
     * e gestisce la navigazione rapida tra i campi di testo tramite tastiera.
     */
    public CreaSeduta() {
        setContentPane(mainPanel);
        setTitle("Nuova Seduta di Laurea");
        setSize(400, 350);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        txtData.addActionListener(e -> txtOra.requestFocus());

        txtOra.addActionListener(e -> txtLuogo.requestFocus());

        txtLuogo.addActionListener(e -> eseguiSalvataggio());

        btnSalva.addActionListener(e -> eseguiSalvataggio());

        btnIndietro.addActionListener(e -> {
            Controller.getInstance().apriHomeUtente();
            dispose();
        });
    }

    private void eseguiSalvataggio() {
        try {
            DateTimeFormatter formattatoreData = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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