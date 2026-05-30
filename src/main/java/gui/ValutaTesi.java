package gui;

import controller.Controller;
import model.Tesi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Desktop;
import java.util.List;

public class ValutaTesi extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Tesi> comboTesi;
    private JButton btnApprova;
    private JButton btnRifiuta;
    private JButton btnIndietro;
    private JButton btnVisualizzaFile;

    public ValutaTesi() {
        setContentPane(mainPanel);
        setTitle("Valutazione Tesi Finali");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        aggiornaListaTesi();

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().apriHomeUtente();
                dispose();
            }
        });

        btnVisualizzaFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        btnApprova.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valuta(true);
            }
        });

        btnRifiuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valuta(false);
            }
        });
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