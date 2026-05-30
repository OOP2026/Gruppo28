package gui;

import controller.Controller;
import model.SedutaLaurea;
import model.Studente;
import model.Tesi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PaginaLaurea extends JFrame {
    private JPanel mainPanel;
    private JComboBox<SedutaLaurea> comboSedute;
    private JButton btnSfoglia;
    private JButton btnCaricaTesi;
    private JButton btnIndietro;
    private JLabel lblStatoTesi;

    private String percorsoFileSelezionato = "";

    public PaginaLaurea() {
        setContentPane(mainPanel);
        setTitle("Caricamento Tesi e Laurea");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        List<SedutaLaurea> sedute = Controller.getInstance().getTutteLeSedute();
        if (sedute.isEmpty()) {
            comboSedute.setEnabled(false);
            btnCaricaTesi.setEnabled(false);
            btnSfoglia.setEnabled(false);
            JOptionPane.showMessageDialog(mainPanel, "Attualmente non ci sono sedute di laurea disponibili. Riprova più tardi.", "Nessuna Seduta", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (SedutaLaurea seduta : sedute) {
                comboSedute.addItem(seduta);
            }
        }

        aggiornaStatoSchermata();

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().apriHomeUtente();
                dispose();
            }
        });

        btnSfoglia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(mainPanel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    percorsoFileSelezionato = fileChooser.getSelectedFile().getAbsolutePath();
                    JOptionPane.showMessageDialog(mainPanel, "File pronto per il caricamento:\n" + fileChooser.getSelectedFile().getName());
                }
            }
        });

        btnCaricaTesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SedutaLaurea seduta = (SedutaLaurea) comboSedute.getSelectedItem();

                if (percorsoFileSelezionato.isEmpty() || seduta == null) {
                    JOptionPane.showMessageDialog(mainPanel, "Seleziona un file e una seduta!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Controller.getInstance().caricaTesiPerStudente(percorsoFileSelezionato, seduta);
                JOptionPane.showMessageDialog(mainPanel, "Tesi caricata con successo! In attesa di valutazione.");
                aggiornaStatoSchermata();
            }
        });
    }

    private void aggiornaStatoSchermata() {
        Studente s = (Studente) Controller.getInstance().getUtenteLoggato();
        Tesi tesi = s.getTesi();

        if (tesi == null) {
            lblStatoTesi.setText("Stato Tesi: Non consegnata");
            if (!Controller.getInstance().getTutteLeSedute().isEmpty()) {
                impostaAbilitazioneComponenti(true);
            }
        } else {
            switch (tesi.getStato()) {
                case IN_ATTESA:
                    lblStatoTesi.setText("Stato Tesi: In attesa di approvazione dal docente.");
                    impostaAbilitazioneComponenti(false);
                    break;
                case APPROVATA:
                    lblStatoTesi.setText("<html>Stato Tesi: <font color='green'>APPROVATA!</font> Pratica completata.</html>");
                    impostaAbilitazioneComponenti(false);
                    break;
                case RIFIUTATA:
                    lblStatoTesi.setText("<html>Stato Tesi: <font color='red'>RIFIUTATA.</font> Puoi caricare un nuovo file.</html>");
                    if (!Controller.getInstance().getTutteLeSedute().isEmpty()) {
                        impostaAbilitazioneComponenti(true);
                    }
                    percorsoFileSelezionato = "";
                    break;
            }
        }
    }

    private void impostaAbilitazioneComponenti(boolean abilita) {
        btnSfoglia.setEnabled(abilita);
        btnCaricaTesi.setEnabled(abilita);
        comboSedute.setEnabled(abilita);
    }
}