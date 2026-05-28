package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeStudente extends JFrame {
    private JPanel mainPanel;
    private JComboBox<ArgomentoTirocinio> comboArgomenti;
    private JButton btnRichiediTirocinio;
    private JLabel lblBenvenuto;
    private JLabel lblStatoRichiesta;
    private JButton btnAccediLaurea;

    public HomeStudente() {
        setContentPane(mainPanel);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Studente studenteLoggato = (Studente) Controller.getInstance().getUtenteLoggato();
        setTitle("Area Studente - " + studenteLoggato.getMatricola());
        lblBenvenuto.setText("Benvenuto nella tua area studente, " + studenteLoggato.getNome() + " " + studenteLoggato.getCognome() + "!");


        for (ArgomentoTirocinio arg : Controller.getInstance().getTuttiGliArgomenti()) {
            comboArgomenti.addItem(arg);
        }


        aggiornaInterfaccia();


        btnRichiediTirocinio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArgomentoTirocinio scelto = (ArgomentoTirocinio) comboArgomenti.getSelectedItem();
                if (scelto != null) {
                    Controller.getInstance().richiediTirocinioPerStudente(scelto);
                    JOptionPane.showMessageDialog(mainPanel, "Richiesta inviata! Tirocinio auto-approvato per il test.");
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
                    lblStatoRichiesta.setText("Stato: Richiesta RIFIUTATA. Scegli un altro argomento.");
                    comboArgomenti.setEnabled(true);
                    btnRichiediTirocinio.setEnabled(true);
                    btnAccediLaurea.setEnabled(false);
                    break;
                case APPROVATA:
                    lblStatoRichiesta.setText("Stato: Tirocinio ACCETTATO e in corso! Puoi procedere alla laurea.");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(true);  🎓
                    break;
            }
        }
    }
}