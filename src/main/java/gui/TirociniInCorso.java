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
 * Finestra grafica dedicata al Docente per la visualizzazione dell'elenco
 * dei tirocini attualmente attivi. Mostra un riepilogo di tutte le richieste
 * di tirocinio che sono state precedentemente valutate e approvate.
 */
public class TirociniInCorso extends JFrame {
    private JPanel mainPanel;
    private JTextArea txtAreaTirocini;
    private JLabel lblLogoHome;
    private JLabel txt1;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, imposta i collegamenti per la navigazione
     * e popola dinamicamente l'area di testo recuperando la lista dei tirocini
     * in corso direttamente dal database.
     */
    public TirociniInCorso() {
        setContentPane(mainPanel);
        setTitle("Elenco Tirocini in Corso");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        txt1.setForeground(bluIstituzionale);

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

        txtAreaTirocini.setEditable(false);

        popolaListaTirocini();
    }

    /**
     * Popola l'area di testo con l'elenco dei tirocini, calcolando dinamicamente
     * lo stato (IN CORSO o COMPLETATO) in base alla presenza di una tesi approvata.
     */
    private void popolaListaTirocini() {
        Docente docenteLoggato = (Docente) Controller.getInstance().getUtenteLoggato();
        List<RichiestaTirocinio> inCorso = Controller.getInstance().getTirociniInCorsoAggiornati(docenteLoggato.getId());

        if (inCorso == null || inCorso.isEmpty()) {
            txtAreaTirocini.setText("Nessun tirocinio in corso al momento.");
            return;
        }

        StringBuilder testo = new StringBuilder();
        for (RichiestaTirocinio r : inCorso) {
            Tesi tesi = Controller.getInstance().getTesiAggiornataPerStudente(r.getStudente().getId());
            String statoVisualizzato = (tesi != null && tesi.getStato() == Stato.APPROVATA) ? "COMPLETATO" : "IN CORSO";

            testo.append("Richiesta ID: ").append(r.getId()).append("\n")
                    .append("Studente: ").append(r.getStudente().getNome()).append(" ").append(r.getStudente().getCognome()).append("\n")
                    .append("Tirocinio: ").append(r.getArgomento().getTitolo()).append("\n") // <-- Nuova riga aggiunta qui
                    .append("Stato: ").append(statoVisualizzato).append("\n")
                    .append("--------------------------------------------------\n\n");
        }

        txtAreaTirocini.setText(testo.toString());
    }
}