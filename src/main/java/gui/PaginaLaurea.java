package gui;

import controller.Controller;
import model.SedutaLaurea;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PaginaLaurea extends JFrame {
    private JPanel mainPanel;
    private JComboBox<SedutaLaurea> comboSedute;
    private JButton btnCaricaTesi;
    private JButton btnSfoglia;

    private String percorsoSelezionato = "";

    public PaginaLaurea() {
        setContentPane(mainPanel);
        setTitle("Segreteria Digitale - Caricamento Tesi Finali");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        for (SedutaLaurea seduta : Controller.getInstance().getTutteLeSedute()) {
            comboSedute.addItem(seduta);
        }

        btnSfoglia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleziona il PDF della tua Tesi");

                int risultato = fileChooser.showOpenDialog(mainPanel);

                if (risultato == JFileChooser.APPROVE_OPTION) {
                    File fileScelto = fileChooser.getSelectedFile();


                    percorsoSelezionato = fileScelto.getAbsolutePath();


                    btnSfoglia.setText("File scelto: " + fileScelto.getName());
                }
            }
        });


        btnCaricaTesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SedutaLaurea sedutaScelta = (SedutaLaurea) comboSedute.getSelectedItem();

                if (percorsoSelezionato.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Devi prima selezionare un file cliccando su 'Sfoglia...'!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                } else {
                    Controller.getInstance().caricaTesiPerStudente(percorsoSelezionato, sedutaScelta);
                    JOptionPane.showMessageDialog(mainPanel, "Tesi consegnata con successo! File registrato alla commissione.");


                    btnCaricaTesi.setEnabled(false);
                    btnSfoglia.setEnabled(false);
                }
            }
        });
    }
}