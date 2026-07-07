package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Finestra grafica dedicata allo Studente per la gestione della domanda di laurea.
 * Permette di selezionare una seduta tra quelle disponibili, sfogliare il file system
 * per scegliere l'elaborato di tesi e inoltrarlo al sistema, tracciandone lo stato
 * di approvazione o eventuale rifiuto.
 */
public class PaginaLaurea extends JFrame {
    private JPanel mainPanel;
    private JComboBox<SedutaLaurea> comboSedute;
    private JButton btnSfoglia;
    private JButton btnCaricaTesi;
    private JLabel lblStatoTesi;
    private JLabel txt1;
    private JLabel txt2;
    private JLabel txt3;
    private JLabel lblLogoHome;

    private String percorsoFileSelezionato = "";

    /**
     * Costruttore della finestra.
     * Inizializza i componenti grafici e gestisce il blocco dinamico dei componenti.
     */
    public PaginaLaurea() {
        setContentPane(mainPanel);
        setTitle("Caricamento Tesi e Laurea");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        Color bluIstituzionale = new Color(0, 51, 102);
        Font fontBottoni = new Font("Segoe UI", Font.BOLD, 14);

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

        JButton[] bottoniBlu = {btnSfoglia, btnCaricaTesi};
        for (JButton btn : bottoniBlu) {
            btn.setBackground(bluIstituzionale);
            btn.setForeground(Color.WHITE);
            btn.setFont(fontBottoni);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);
        }

        List<SedutaLaurea> sedute = Controller.getInstance().getTutteLeSedute();
        if (sedute.isEmpty()) {
            impostaAbilitazioneComponenti(false);
            JOptionPane.showMessageDialog(mainPanel, "Nessuna seduta disponibile.");
        } else {
            for (SedutaLaurea seduta : sedute) {
                comboSedute.addItem(seduta);
            }
        }

        aggiornaStatoSchermata();

        btnSfoglia.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(mainPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                percorsoFileSelezionato = fileChooser.getSelectedFile().getAbsolutePath();
                JOptionPane.showMessageDialog(mainPanel, "File pronto: " + fileChooser.getSelectedFile().getName());
            }
        });

        btnCaricaTesi.addActionListener(e -> {
            SedutaLaurea seduta = (SedutaLaurea) comboSedute.getSelectedItem();
            if (percorsoFileSelezionato.isEmpty() || seduta == null) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona file e seduta!");
                return;
            }
            Controller.getInstance().caricaTesiPerStudente(percorsoFileSelezionato, seduta);
            JOptionPane.showMessageDialog(mainPanel, "Tesi inviata!");
            aggiornaStatoSchermata();
        });
    }

    /**
     * Aggiorna lo stato della schermata recuperando la tesi più recente dal database.
     */
    private void aggiornaStatoSchermata() {
        Studente s = (Studente) Controller.getInstance().getUtenteLoggato();
        Tesi tesi = Controller.getInstance().getTesiAggiornataPerStudente(s.getId());

        if (tesi == null) {
            lblStatoTesi.setText("Stato Tesi: Non consegnata");
            impostaAbilitazioneComponenti(true);
        } else {
            switch (tesi.getStato()) {
                case IN_ATTESA:
                    lblStatoTesi.setText("Stato Tesi: In attesa di approvazione.");
                    impostaAbilitazioneComponenti(false);
                    break;
                case APPROVATA:
                    lblStatoTesi.setText("<html>Stato Tesi: APPROVATA.<br>Presentarsi alla seduta indicata:</html>");
                    impostaAbilitazioneComponenti(false);
                    break;
                case RIFIUTATA:
                    lblStatoTesi.setText("Stato Tesi: RIFIUTATA. Puoi ricaricare.");
                    impostaAbilitazioneComponenti(true);
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