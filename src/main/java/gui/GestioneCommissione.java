package gui;

import controller.Controller;
import model.SedutaLaurea;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JLabel txt1;
    private JLabel txt2;
    private JLabel txt3;
    private JLabel lblLogoHome;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, carica le sedute di laurea disponibili
     * nel menu a tendina e imposta i listener per aggiornare dinamicamente
     * i dati degli studenti e della commissione ad ogni cambio di selezione.
     */
    public GestioneCommissione() {
        setContentPane(mainPanel);
        setTitle("Formazione Commissione");
        setSize(600, 500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);

        JLabel[] etichette = {txt1, txt2, txt3};
        for (JLabel lbl : etichette) {
            if (lbl != null) lbl.setForeground(bluIstituzionale);
        }

        try {
            ImageIcon icona = new ImageIcon("logo.png");
            Image imgScalata = icona.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogoHome.setIcon(new ImageIcon(imgScalata));
            lblLogoHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblLogoHome.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Controller.getInstance().apriHomeUtente();
                    dispose();
                }
            });
        } catch (Exception e) {
            lblLogoHome.setText("Home");
        }

        txtStudenti.setEditable(false);
        txtCommissione.setEditable(false);

        List<SedutaLaurea> sedute = Controller.getInstance().getTutteLeSedute();
        if (sedute.isEmpty()) {
            comboSedute.setEnabled(false);
            txtStudenti.setText("Nessuna seduta disponibile.");
            txtCommissione.setText("Nessuna seduta disponibile.");
        } else {
            for (SedutaLaurea s : sedute) {
                comboSedute.addItem(s);
            }
            aggiornaDati();
        }

        comboSedute.addActionListener(e -> aggiornaDati());
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
            txtCommissione.append("Nessun docente necessario.\n");
        } else {
            txtCommissione.append("Il Coordinatore (Presidente)\n");
            for (String doc : commissione) {
                txtCommissione.append("- Prof. " + doc + "\n");
            }
        }
    }
}