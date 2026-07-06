package gui;

import controller.Controller;
import model.SedutaLaurea;

import javax.swing.*;
import java.util.List;

/**
 * Finestra grafica dedicata al Coordinatore per la gestione e la formazione
 * della commissione di laurea. Permette di visualizzare gli studenti approvati
 * per una specifica seduta e di calcolare automaticamente i docenti necessari.
 */
public class GestioneCommissione extends JFrame {
    private JPanel mainPanel;
    private JComboBox<SedutaLaurea> comboSedute;
    private JTextArea txtStudenti;
    private JTextArea txtCommissione;
    private JButton btnIndietro;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, carica le sedute di laurea disponibili
     * nel menu a tendina e imposta i listener per aggiornare dinamicamente
     * i dati degli studenti e della commissione ad ogni cambio di selezione.
     */
    public GestioneCommissione() {
        setContentPane(mainPanel);
        setTitle("Formazione Commissione");
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtStudenti.setEditable(false);
        txtCommissione.setEditable(false);

        List<SedutaLaurea> sedute = Controller.getInstance().getTutteLeSedute();
        if (sedute.isEmpty()) {
            comboSedute.setEnabled(false);
            txtStudenti.setText("Nessuna seduta disponibile. Creane una prima di formare la commissione.");
            txtCommissione.setText("Nessuna seduta disponibile.");
        } else {
            for (SedutaLaurea s : sedute) {
                comboSedute.addItem(s);
            }
            aggiornaDati();
        }

        comboSedute.addActionListener(e -> aggiornaDati());

        btnIndietro.addActionListener(e -> {
            Controller.getInstance().apriHomeUtente();
            dispose();
        });
    }

    private void aggiornaDati() {
        SedutaLaurea selezionata = (SedutaLaurea) comboSedute.getSelectedItem();
        if (selezionata == null) return;

        List<String> studenti = Controller.getInstance().getStudentiApprovatiPerSeduta(selezionata);
        txtStudenti.setText("");
        if (studenti.isEmpty()) {
            txtStudenti.append("Nessuno studente approvato per questa seduta.\n");
        } else {
            for (String s : studenti) {
                txtStudenti.append("- " + s + "\n");
            }
        }

        List<String> commissione = Controller.getInstance().getCommissionePerSeduta(selezionata);
        txtCommissione.setText("");
        if (commissione.isEmpty()) {
            txtCommissione.append("Nessun docente necessario (nessuna tesi approvata).\n");
        } else {
            txtCommissione.append("Il Coordinatore (Presidente)\n");
            for (String doc : commissione) {
                txtCommissione.append("- Prof. " + doc + "\n");
            }
        }
    }
}