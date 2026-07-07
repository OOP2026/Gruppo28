package gui;

import controller.Controller;
import model.TipoTirocinio;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreaArgomento extends JFrame {
    private JPanel mainPanel;
    private JTextField txtTitolo;
    private JComboBox<String> comboTipo;
    private JTextField txtReferente;
    private JButton btnSalva;
    private JLabel txt1;
    private JLabel txt2;
    private JLabel txt3;
    private JLabel txt4;

    public CreaArgomento() {
        setContentPane(mainPanel);
        setTitle("Nuovo Argomento Tirocinio");
        setSize(500, 350);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        Color bluIstituzionale = new Color(0, 51, 102);

        JLabel[] etichette = {txt1, txt2, txt3, txt4};
        for (JLabel lbl : etichette) {
            if (lbl != null) {
                lbl.setForeground(bluIstituzionale);
            }
        }

        btnSalva.setBackground(bluIstituzionale);
        btnSalva.setForeground(Color.WHITE);
        btnSalva.setOpaque(true);
        btnSalva.setBorderPainted(false);
        btnSalva.setFocusPainted(false);
        btnSalva.setContentAreaFilled(true);

        aggiornaStatoReferente();

        comboTipo.addActionListener(e -> aggiornaStatoReferente());

        btnSalva.addActionListener(e -> {
            String titolo = txtTitolo.getText();
            Object elementoSelezionato = comboTipo.getSelectedItem();
            TipoTirocinio tipo = TipoTirocinio.valueOf(elementoSelezionato.toString());
            String referente = txtReferente.getText();

            if (titolo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Inserisci il titolo dell'argomento!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                Controller.getInstance().aggiungiNuovoArgomento(titolo, tipo, referente);
                JOptionPane.showMessageDialog(mainPanel, "Argomento creato con successo!");
                dispose();
            }
        });
    }

    private void aggiornaStatoReferente() {
        Object selezionato = comboTipo.getSelectedItem();
        if (selezionato != null && selezionato.toString().equals("ESTERNO")) {
            txtReferente.setEnabled(true);
        } else {
            txtReferente.setEnabled(false);
            txtReferente.setText("");
        }
    }
}