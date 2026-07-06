package gui;

import controller.Controller;
import model.Tesi;

import javax.swing.*;
import java.io.File;
import java.awt.Desktop;
import java.util.List;

/**
 * Finestra grafica dedicata al Docente per la valutazione degli elaborati finali di tesi.
 * Permette di visualizzare le tesi in attesa, aprire il file fisico caricato dallo studente
 * per la consultazione e procedere con l'approvazione o il rifiuto dell'elaborato.
 */
public class ValutaTesi extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Tesi> comboTesi;
    private JButton btnApprova;
    private JButton btnRifiuta;
    private JButton btnIndietro;
    private JButton btnVisualizzaFile;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, popola il menu a tendina con le tesi attualmente
     * in attesa di valutazione e configura i listener per aprire il file nel sistema operativo
     * o per esprimere il giudizio finale (approvazione/rifiuto).
     */
    public ValutaTesi() {
        setContentPane(mainPanel);
        setTitle("Valutazione Tesi Finali");
        setSize(450, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        aggiornaListaTesi();

        btnIndietro.addActionListener(e -> {
            Controller.getInstance().apriHomeUtente();
            dispose();
        });

        btnVisualizzaFile.addActionListener(e -> {
            Tesi selezionata = (Tesi) comboTesi.getSelectedItem();
            if (selezionata == null) return;

            try {
                File fileTesi = new File(selezionata.getFilePath());
                if (fileTesi.exists()) {
                    Desktop.getDesktop().open(fileTesi);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Impossibile trovare il file nel computer.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Errore durante l'apertura del file.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnApprova.addActionListener(e -> valuta(true));

        btnRifiuta.addActionListener(e -> valuta(false));
    }

    private void valuta(boolean approva) {
        Tesi selezionata = (Tesi) comboTesi.getSelectedItem();
        if (selezionata == null) return;

        Controller.getInstance().valutaTesiComeDocente(selezionata, approva);

        String msg = approva ? "Tesi Approvata!" : "Tesi Rifiutata.";
        JOptionPane.showMessageDialog(mainPanel, msg);

        aggiornaListaTesi();
    }

    private void aggiornaListaTesi() {
        comboTesi.removeAllItems();
        List<Tesi> inAttesa = Controller.getInstance().getTesiInAttesa();

        for (Tesi t : inAttesa) {
            comboTesi.addItem(t);
        }

        boolean ciSonoTesi = !inAttesa.isEmpty();
        btnApprova.setEnabled(ciSonoTesi);
        btnRifiuta.setEnabled(ciSonoTesi);
        comboTesi.setEnabled(ciSonoTesi);
        btnVisualizzaFile.setEnabled(ciSonoTesi);
    }
}