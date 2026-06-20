package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica recuperando i dati dello studente loggato
     * dal Controller. Popola la lista degli argomenti di tirocinio disponibili
     * e gestisce dinamicamente l'abilitazione dei pulsanti in base allo stato attuale
     * della pratica dello studente.
     */
    public HomeStudente() {
        setContentPane(mainPanel);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Studente studenteLoggato = (Studente) Controller.getInstance().getUtenteLoggato();
        setTitle("Area Studente - " + studenteLoggato.getMatricola());
        lblBenvenuto.setText("Benvenuto nella tua area studente, " + studenteLoggato.getNome() + " " + studenteLoggato.getCognome() + "!");

        for (ArgomentoTirocinio arg : Controller.getInstance().getTuttiGliArgomenti()) {
            comboArgomenti.addItem(arg);
        }

        aggiornaInterfaccia();

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        btnRichiediTirocinio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArgomentoTirocinio scelto = (ArgomentoTirocinio) comboArgomenti.getSelectedItem();
                if (scelto != null) {
                    Controller.getInstance().richiediTirocinioPerStudente(scelto);
                    Controller.getInstance().setStudenteAvvisatoRifiuto(false);
                    JOptionPane.showMessageDialog(mainPanel, "Richiesta inviata!");
                    aggiornaInterfaccia();
                }
            }
        });

        btnAccediLaurea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaLaurea paginaLaurea = new PaginaLaurea();
                paginaLaurea.setVisible(true);
                dispose();
            }
        });
    }

    private void aggiornaInterfaccia() {
        Studente studente = (Studente) Controller.getInstance().getUtenteLoggato();
        RichiestaTirocinio richiesta = studente.getRichiestaAttuale();

        lblStatoRichiesta.setForeground(Color.BLACK);

        if (richiesta == null) {
            lblStatoRichiesta.setText("Stato: Nessun tirocinio richiesto.");
            comboArgomenti.setEnabled(true);
            btnRichiediTirocinio.setEnabled(true);
            btnAccediLaurea.setEnabled(false);
        } else {
            switch (richiesta.getStato()) {
                case IN_ATTESA:
                    lblStatoRichiesta.setText("Stato: Richiesta in attesa di valutazione.");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(false);
                    break;
                case RIFIUTATA:
                    lblStatoRichiesta.setForeground(Color.RED);
                    lblStatoRichiesta.setText("Stato: Richiesta RIFIUTATA. Scegli un altro argomento.");
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
                    lblStatoRichiesta.setText("Stato: Tirocinio ACCETTATO e in corso! Puoi procedere alla laurea.");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(true);
                    break;
            }
        }
    }
}