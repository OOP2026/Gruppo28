package gui;

import controller.Controller;
import model.TipoTirocinio;

import javax.swing.*;

/**
 * Finestra grafica che permette a un Docente di creare e proporre
 * un nuovo argomento di tirocinio agli studenti.
 */
public class CreaArgomento extends JFrame {
    private JPanel mainPanel;
    private JTextField txtTitolo;
    private JComboBox<String> comboTipo;
    private JTextField txtReferente;
    private JButton btnSalva;

    /**
     * Costruttore della finestra.
     * Inizializza i componenti grafici, configura l'abilitazione del campo referente
     * in base al tipo di tirocinio selezionato e imposta l'azione di salvataggio.
     */
    public CreaArgomento() {
        setContentPane(mainPanel);
        setTitle("Nuovo Argomento Tirocinio");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
                JOptionPane.showMessageDialog(mainPanel, "Argomento creato con successo! Ora è visibile agli studenti.");
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