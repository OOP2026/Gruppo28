package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.util.List;

/**
 * Finestra grafica dedicata al Docente per la visualizzazione dell'elenco
 * dei tirocini attualmente attivi. Mostra un riepilogo di tutte le richieste
 * di tirocinio che sono state precedentemente valutate e approvate.
 */
public class TirociniInCorso extends JFrame {
    private JPanel mainPanel;
    private JTextArea txtAreaTirocini;
    private JButton btnIndietro;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, imposta i collegamenti per la navigazione
     * e popola dinamicamente l'area di testo recuperando la lista dei tirocini
     * in corso direttamente dal database.
     */
    public TirociniInCorso() {
        setContentPane(mainPanel);
        setTitle("Elenco Tirocini in Corso");
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        popolaListaTirocini();

        btnIndietro.addActionListener(e -> {
            Controller.getInstance().apriHomeUtente();
            dispose();
        });
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

            testo.append("Richiesta ID: ").append(r.getId())
                    .append(" | Studente: ").append(r.getStudente().getNome()).append(" ").append(r.getStudente().getCognome())
                    .append(" | Stato: ").append(statoVisualizzato).append("\n");
            testo.append("--------------------------------------------------\n");
        }

        txtAreaTirocini.setText(testo.toString());
    }
}