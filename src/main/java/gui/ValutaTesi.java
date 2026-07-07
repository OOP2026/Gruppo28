package gui;

import controller.Controller;
import model.Tesi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.awt.Desktop;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Finestra grafica dedicata al Docente per la valutazione degli elaborati finali di tesi.
 * Permette di visualizzare le tesi in attesa, aprire il file fisico caricato dallo studente
 * per la consultazione e procedere con l'approvazione o il refusal dell'elaborato.
 */
public class ValutaTesi extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Tesi> comboTesi;
    private JButton btnApprova;
    private JButton btnRifiuta;
    private JButton btnVisualizzaFile;
    private JLabel txt1;
    private JLabel lblLogoHome;

    /**
     * Costruttore della finestra.
     * Inizializza l'interfaccia grafica, popola il menu a tendina con le tesi attualmente
     * in attesa di valutazione e configura i listener per aprire il file nel sistema operativo
     * o per esprimere il giudizio finale (approvazione/rifiuto).
     */
    public ValutaTesi() {
        setContentPane(mainPanel);
        setTitle("Valutazione Tesi Finali");
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        Color coloreRosso = new Color(200, 0, 0);
        Font fontBottoni = new Font("Segoe UI", Font.BOLD, 14);

        if (txt1 != null) {
            txt1.setForeground(bluIstituzionale);
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

        JButton[] bottoniBlu = {btnApprova, btnVisualizzaFile};
        for (JButton btn : bottoniBlu) {
            btn.setBackground(bluIstituzionale);
            btn.setForeground(Color.WHITE);
            btn.setFont(fontBottoni);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);
        }

        btnRifiuta.setBackground(coloreRosso);
        btnRifiuta.setForeground(Color.WHITE);
        btnRifiuta.setFont(fontBottoni);
        btnRifiuta.setOpaque(true);
        btnRifiuta.setBorderPainted(false);
        btnRifiuta.setContentAreaFilled(true);

        aggiornaListaTesi();

        btnVisualizzaFile.addActionListener(e -> {
            Tesi selezionata = (Tesi) comboTesi.getSelectedItem();
            if (selezionata == null) return;

            try {
                File fileTesi = new File(selezionata.getFilePath());
                if (fileTesi.exists()) {
                    Desktop.getDesktop().open(fileTesi);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Impossibile trovare il file nel computer.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Errore durante l'apertura del file.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnApprova.addActionListener(e -> valuta(true));

        btnRifiuta.addActionListener(e -> valuta(false));
    }

    private void valuta(boolean approva) {
        Tesi selezionata = (Tesi) comboTesi.getSelectedItem();
        if (selezionata == null) return;

        Controller.getInstance().valutaTesiComeDocente(selezionata, approva);

        String msg = approva ? "Tesi Approvata!" : "Tesi Rifiutata.";
        JOptionPane.showMessageDialog(mainPanel, msg);

        aggiornaListaTesi();
    }

    private void aggiornaListaTesi() {
        comboTesi.removeAllItems();
        List<Tesi> inAttesa = Controller.getInstance().getTesiInAttesa();

        for (Tesi t : inAttesa) {
            comboTesi.addItem(t);
        }

        boolean ciSonoTesi = !inAttesa.isEmpty();
        btnApprova.setEnabled(ciSonoTesi);
        btnRifiuta.setEnabled(ciSonoTesi);
        comboTesi.setEnabled(ciSonoTesi);
        btnVisualizzaFile.setEnabled(ciSonoTesi);
    }
}