package gui;

import controller.Controller;
import model.TipoTirocinio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreaArgomento extends JFrame {
    private JPanel mainPanel;
    private JTextField txtTitolo;
    private JComboBox<TipoTirocinio> comboTipo;
    private JTextField txtReferente;
    private JButton btnSalva;

    public CreaArgomento() {
        setContentPane(mainPanel);
        setTitle("Nuovo Argomento Tirocinio");
        setSize(400, 300);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        comboTipo.addItem(TipoTirocinio.INTERNO);
        comboTipo.addItem(TipoTirocinio.ESTERNO);


        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titolo = txtTitolo.getText();
                TipoTirocinio tipo = (TipoTirocinio) comboTipo.getSelectedItem();
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
}