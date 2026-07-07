package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Finestra grafica che permette al Coordinatore di inserire
 * una nuova seduta di laurea programmata nel sistema.
 */
public class CreaSeduta extends JFrame {
    private JPanel mainPanel;
    private JTextField txtData;
    private JTextField txtOra;
    private JTextField txtLuogo;
    private JButton btnSalva;
    private JLabel lblLogoHome;
    private JLabel txt1;
    private JLabel txt2;
    private JLabel txt3;
    private JLabel txt4;

    /**
     * Costruttore della finestra.
     * Inizializza i componenti grafici, imposta le azioni dei bottoni
     * e gestisce la navigazione rapida tra i campi di testo tramite tastiera.
     */
    public CreaSeduta() {
        setContentPane(mainPanel);
        setTitle("Nuova Seduta di Laurea");
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        Color bluIstituzionale = new Color(0, 51, 102);

        JLabel[] etichette = {txt1, txt2, txt3, txt4};
        for (JLabel lbl : etichette) {
            if (lbl != null) lbl.setForeground(bluIstituzionale);
        }

        try {
            ImageIcon icona = new ImageIcon("logo.png");
            Image imgScalata = icona.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
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

        btnSalva.setBackground(bluIstituzionale);
        btnSalva.setForeground(Color.WHITE);
        btnSalva.setOpaque(true);
        btnSalva.setBorderPainted(false);
        btnSalva.setFocusPainted(false);
        btnSalva.setContentAreaFilled(true);

        btnSalva.addActionListener(e -> eseguiSalvataggio());
    }

    private void eseguiSalvataggio() {
        try {
            DateTimeFormatter formattatoreData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate data = LocalDate.parse(txtData.getText().trim(), formattatoreData);
            LocalTime ora = LocalTime.parse(txtOra.getText().trim());
            String luogo = txtLuogo.getText().trim();

            if (luogo.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Inserisci il luogo della seduta!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Controller.getInstance().aggiungiSeduta(data, ora, luogo);
            JOptionPane.showMessageDialog(mainPanel, "Seduta inserita con successo!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Errore! Usa il formato GG-MM-AAAA per la data e HH:MM per l'ora.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}