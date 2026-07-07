package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Finestra principale (Dashboard) per l'utente con ruolo di Studente.
 * Fornisce l'accesso alle funzionalità per la richiesta di un nuovo tirocinio,
 * il monitoraggio in tempo reale dello stato della richiesta (in attesa,
 * approvata o rifiutata) e l'accesso alla sezione per la domanda di laurea.
 */
public class HomeStudente extends JFrame {
    private JPanel mainPanel;
    private JComboBox<ArgomentoTirocinio> comboArgomenti;
    private JButton btnRichiediTirocinio;
    private JLabel lblBenvenuto;
    private JLabel lblStatoRichiesta;
    private JButton btnAccediLaurea;
    private JButton btnLogout;
    private JLabel txt1;
    private JLabel txt2;
    private JLabel lblTitolo;
    private JLabel lblLogo;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica recuperando i dati dello studente loggato
     * dal Controller. Popola la lista degli argomenti di tirocinio disponibili
     * e gestisce dinamicamente l'abilitazione dei pulsanti in base allo stato attuale
     * della pratica dello studente.
     */
    public HomeStudente() {
        setContentPane(mainPanel);
        setSize(650, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        Color coloreRosso = new Color(200, 0, 0);
        Font fontBottoni = new Font("Segoe UI", Font.BOLD, 16);

        try {
            ImageIcon originalIcon = new ImageIcon("logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Immagine logo non trovata.");
        }

        lblTitolo.setText("HOME");
        lblTitolo.setForeground(bluIstituzionale);
        lblTitolo.setFont(new Font("Segoe UI", Font.BOLD, 40));

        txt1.setForeground(bluIstituzionale);
        txt2.setForeground(bluIstituzionale);

        Studente studenteLoggato = (Studente) Controller.getInstance().getUtenteLoggato();
        setTitle("Area Studente - " + studenteLoggato.getMatricola());
        lblBenvenuto.setText("Benvenuto nella tua area studente, " + studenteLoggato.getNome() + " " + studenteLoggato.getCognome() + "!");
        lblBenvenuto.setForeground(bluIstituzionale);
        lblBenvenuto.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton[] bottoniBlu = {btnRichiediTirocinio, btnAccediLaurea};
        for (JButton btn : bottoniBlu) {
            btn.setBackground(bluIstituzionale);
            btn.setForeground(Color.WHITE);
            btn.setFont(fontBottoni);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);
        }

        btnLogout.setBackground(coloreRosso);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(fontBottoni);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(true);

        for (ArgomentoTirocinio arg : Controller.getInstance().getTuttiGliArgomenti()) {
            comboArgomenti.addItem(arg);
        }

        aggiornaInterfaccia();

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        btnRichiediTirocinio.addActionListener(e -> {
            ArgomentoTirocinio scelto = (ArgomentoTirocinio) comboArgomenti.getSelectedItem();
            if (scelto != null) {
                Controller.getInstance().richiediTirocinioPerStudente(scelto);
                Controller.getInstance().setStudenteAvvisatoRifiuto(false);
                JOptionPane.showMessageDialog(mainPanel, "Richiesta inviata!");
                aggiornaInterfaccia();
            }
        });

        btnAccediLaurea.addActionListener(e -> {
            PaginaLaurea paginaLaurea = new PaginaLaurea();
            paginaLaurea.setVisible(true);
            dispose();
        });
    }

    /**
     * Aggiorna l'interfaccia grafica recuperando i dati aggiornati dello studente
     * dal database e definendo dinamicamente l'abilitazione dei componenti in base
     * allo stato della richiesta di tirocinio.
     */
    private void aggiornaInterfaccia() {
        Studente studente = (Studente) Controller.getInstance().getUtenteLoggato();
        RichiestaTirocinio richiesta = Controller.getInstance().getRichiestaAggiornataPerStudente(studente.getId());

        lblStatoRichiesta.setForeground(Color.BLACK);

        if (richiesta == null) {
            lblStatoRichiesta.setText("Stato: Nessun tirocinio richiesto.");
            comboArgomenti.setEnabled(true);
            btnRichiediTirocinio.setEnabled(true);
            btnAccediLaurea.setEnabled(false);
        } else {
            switch (richiesta.getStato()) {
                case IN_ATTESA:
                    lblStatoRichiesta.setText("<html>Stato: Richiesta in attesa<br>di valutazione.</html>");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(false);
                    break;
                case RIFIUTATA:
                    lblStatoRichiesta.setForeground(Color.RED);
                    lblStatoRichiesta.setText("<html>Stato: Richiesta RIFIUTATA.<br>Scegli un altro argomento.</html>");
                    comboArgomenti.setEnabled(true);
                    btnRichiediTirocinio.setEnabled(true);
                    btnAccediLaurea.setEnabled(false);
                    if (!Controller.getInstance().isStudenteAvvisatoRifiuto()) {
                        String messaggio = "Attenzione: La tua precedente richiesta di tirocinio è stata rifiutata dal docente.\n\n";
                        if (richiesta.getMotivazioneRifiuto() != null && !richiesta.getMotivazioneRifiuto().trim().isEmpty()) {
                            messaggio += "Motivazione Rifiuto: " + richiesta.getMotivazioneRifiuto();
                        } else {
                            messaggio += "Motivazione Rifiuto: Non Inserita dal Docente";
                        }
                        messaggio += "\n\nPuoi procedere a effettuare una nuova richiesta selezionando un altro argomento.";
                        JOptionPane.showMessageDialog(mainPanel, messaggio, "Richiesta Rifiutata", JOptionPane.WARNING_MESSAGE);
                        Controller.getInstance().setStudenteAvvisatoRifiuto(true);
                    }
                    break;
                case APPROVATA:
                    lblStatoRichiesta.setText("<html>Stato: Tirocinio ACCETTATO e in corso!<br>Puoi procedere alla laurea.</html>");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(true);
                    break;
            }
        }
    }
}