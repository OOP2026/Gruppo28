package gui;

import controller.Controller;
import model.SedutaLaurea;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaLaurea extends JFrame {
    private JPanel mainPanel;
    private JComboBox<SedutaLaurea> comboSedute;
    private JTextField txtFilePath;
    private JButton btnSfoglia;
    private JButton btnCaricaTesi;
    private JButton btnIndietro;

    public PaginaLaurea() {
        setContentPane(mainPanel);
        setTitle("Caricamento Tesi e Laurea");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        for (SedutaLaurea seduta : Controller.getInstance().getTutteLeSedute()) {
            comboSedute.addItem(seduta);
        }

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeStudente().setVisible(true);
                dispose();
            }
        });

        btnSfoglia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(mainPanel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    txtFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        btnCaricaTesi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = txtFilePath.getText();
                SedutaLaurea seduta = (SedutaLaurea) comboSedute.getSelectedItem();

                if (path.isEmpty() || seduta == null) {
                    JOptionPane.showMessageDialog(mainPanel, "Seleziona un file e una seduta!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Controller.getInstance().caricaTesiPerStudente(path, seduta);
                JOptionPane.showMessageDialog(mainPanel, "Operazione completata! Controlla la console per l'esito.");
            }
        });
    }
}