package gui;

import controller.Controller;
import model.RichiestaTirocinio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JLabel lblLogoHome;
    private JLabel txt1;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia, imposta un renderer personalizzato per visualizzare
     * in modo chiaro il nome dello studente e l'argomento scelto nel menu a tendina,
     * e configura i listener per i pulsanti di accettazione e rifiuto.
     */
    public GestisciRichieste() {
        setContentPane(mainPanel);
        setTitle("Gestione Richieste Studenti");
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        Color coloreRosso = new Color(200, 0, 0);
        Font fontBottoni = new Font("Segoe UI", Font.BOLD, 14);

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

        btnAccetta.setBackground(bluIstituzionale);
        btnAccetta.setForeground(Color.WHITE);
        btnAccetta.setFont(fontBottoni);
        btnAccetta.setOpaque(true);
        btnAccetta.setBorderPainted(false);
        btnAccetta.setContentAreaFilled(true);

        btnRifiuta.setBackground(coloreRosso);
        btnRifiuta.setForeground(Color.WHITE);
        btnRifiuta.setFont(fontBottoni);
        btnRifiuta.setOpaque(true);
        btnRifiuta.setBorderPainted(false);
        btnRifiuta.setContentAreaFilled(true);

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

        btnAccetta.addActionListener(e -> valuta(true));

        btnRifiuta.addActionListener(e -> {
            RichiestaTirocinio selezionata = (RichiestaTirocinio) comboRichieste.getSelectedItem();
            if (selezionata != null) {
                String motivazione = "";
                int scelta = JOptionPane.showConfirmDialog(mainPanel, "Vuoi inserire una motivazione per il rifiuto?", "Motivazione Rifiuto", JOptionPane.YES_NO_OPTION);
                if (scelta == JOptionPane.YES_OPTION) {
                    motivazione = JOptionPane.showInputDialog(mainPanel, "Scrivi la motivazione del rifiuto:");
                    if (motivazione == null) motivazione = "";
                }
                selezionata.setMotivazioneRifiuto(motivazione);
                valuta(false);
            }
        });
    }

    private void valuta(boolean accetta) {
        RichiestaTirocinio selezionata = (RichiestaTirocinio) comboRichieste.getSelectedItem();
        if (selezionata == null) {
            JOptionPane.showMessageDialog(mainPanel, "Nessuna richiesta da valutare!");
            return;
        }
        Controller.getInstance().valutaRichiestaComeDocente(selezionata, accetta);
        JOptionPane.showMessageDialog(mainPanel, accetta ? "Richiesta APPROVATA!" : "Richiesta RIFIUTATA!");
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