package gui;

import controller.Controller;
import model.TipoTirocinio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreaArgomento extends JFrame {
    private JPanel mainPanel;
    private JTextField txtTitolo;
    private JComboBox comboTipo;
    private JTextField txtReferente;
    private JButton btnSalva;

    public CreaArgomento() {
        setContentPane(mainPanel);
        setTitle("Nuovo Argomento Tirocinio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        aggiornaStatoReferente();

        comboTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aggiornaStatoReferente();
            }
        });

        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titolo = txtTitolo.getText();

                Object elementoSelezionato = comboTipo.getSelectedItem();
                TipoTirocinio tipo = TipoTirocinio.valueOf(elementoSelezionato.toString());

                String referente = txtReferente.getText();

                if (titolo.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Inserisci il titolo dell'argomento!", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    Controller.getInstance().aggiungiNuovoArgomento(titolo, tipo, referente);
                    JOptionPane.showMessageDialog(mainPanel, "Argomento creato con successo! Ora è visibile agli studenti.");
                    dispose();
                }
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