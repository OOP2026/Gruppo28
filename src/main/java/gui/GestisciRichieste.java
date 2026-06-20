package gui;

import controller.Controller;
import model.RichiestaTirocinio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Finestra grafica dedicata al Docente per la gestione delle richieste di tirocinio.
 * Permette di visualizzare le richieste in attesa, approvarle oppure rifiutarle
 * inserendo una motivazione opzionale.
 */
public class GestisciRichieste extends JFrame {
    private JPanel mainPanel;
    private JComboBox<RichiestaTirocinio> comboRichieste;
    private JButton btnAccetta;
    private JButton btnRifiuta;
    private JButton btnIndietro;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia, imposta un renderer personalizzato per visualizzare
     * in modo chiaro il nome dello studente e l'argomento scelto nel menu a tendina,
     * e configura i listener per i pulsanti di accettazione e rifiuto.
     */
    public GestisciRichieste() {
        setContentPane(mainPanel);
        setTitle("Gestione Richieste Studenti");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        comboRichieste.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof RichiestaTirocinio) {
                    RichiestaTirocinio r = (RichiestaTirocinio) value;
                    setText(r.getStudente().getNome() + " " + r.getStudente().getCognome() + " - " + r.getArgomento().getTitolo());
                }
                return this;
            }
        });

        aggiornaListaRichieste();

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().apriHomeUtente();
                dispose();
            }
        });

        btnAccetta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valuta(true);
            }
        });

        btnRifiuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RichiestaTirocinio selezionata = (RichiestaTirocinio) comboRichieste.getSelectedItem();

                if (selezionata != null) {
                    String motivazione = "";

                    int scelta = JOptionPane.showConfirmDialog(mainPanel,
                            "Vuoi inserire una motivazione per il rifiuto?",
                            "Motivazione Rifiuto",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    if (scelta == JOptionPane.YES_OPTION) {
                        motivazione = JOptionPane.showInputDialog(mainPanel,
                                "Scrivi la motivazione del rifiuto:",
                                "Inserimento Motivazione",
                                JOptionPane.PLAIN_MESSAGE);

                        if (motivazione == null) {
                            motivazione = "";
                        }
                    }

                    selezionata.setMotivazioneRifiuto(motivazione);
                    valuta(false);
                }
            }
        });
    }

    private void valuta(boolean accetta) {
        RichiestaTirocinio selezionata = (RichiestaTirocinio) comboRichieste.getSelectedItem();

        if (selezionata == null) {
            JOptionPane.showMessageDialog(mainPanel, "Nessuna richiesta da valutare!", "Avviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Controller.getInstance().valutaRichiestaComeDocente(selezionata, accetta);

        String messaggio = accetta ? "Richiesta APPROVATA!" : "Richiesta RIFIUTATA!";
        JOptionPane.showMessageDialog(mainPanel, messaggio);

        aggiornaListaRichieste();
    }

    private void aggiornaListaRichieste() {
        comboRichieste.removeAllItems();
        List<RichiestaTirocinio> inAttesa = Controller.getInstance().getRichiesteInAttesa();

        for (RichiestaTirocinio r : inAttesa) {
            comboRichieste.addItem(r);
        }

        boolean ciSonoRichieste = !inAttesa.isEmpty();
        btnAccetta.setEnabled(ciSonoRichieste);
        btnRifiuta.setEnabled(ciSonoRichieste);
        comboRichieste.setEnabled(ciSonoRichieste);
    }
}