package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeStudente extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(HomeStudente.class.getName());
    private static final String FONT_FAMILY = "Segoe UI";

    private JPanel mainPanel;
    private JComboBox<ArgomentoTirocinio> comboArgomenti;
    private JButton btnRichiediTirocinio;
    private JLabel lblBenvenuto;
    private JLabel lblStatoRichiesta;
    private JButton btnAccediLaurea;
    private JButton btnLogout;
    private JLabel txt1;
    private JLabel txt2;
    private JLabel lblTitolo;
    private JLabel lblLogo;

    public HomeStudente() {
        setContentPane(mainPanel);
        setSize(950, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Color bluIstituzionale = new Color(0, 51, 102);
        Color coloreRosso = new Color(200, 0, 0);
        Font fontBottoni = new Font(FONT_FAMILY, Font.BOLD, 16);

        try {
            ImageIcon originalIcon = new ImageIcon("logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Immagine logo non trovata.", e);
        }

        lblTitolo.setText("HOME");
        lblTitolo.setForeground(bluIstituzionale);
        lblTitolo.setFont(new Font(FONT_FAMILY, Font.BOLD, 40));

        txt1.setForeground(bluIstituzionale);
        txt2.setForeground(bluIstituzionale);

        Studente studenteLoggato = (Studente) Controller.getInstance().getUtenteLoggato();
        setTitle("Area Studente - " + studenteLoggato.getMatricola());
        lblBenvenuto.setText("Benvenuto nella tua area studente, " + studenteLoggato.getNome() + " " + studenteLoggato.getCognome() + "!");
        lblBenvenuto.setForeground(bluIstituzionale);
        lblBenvenuto.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));

        JButton[] bottoniBlu = {btnRichiediTirocinio, btnAccediLaurea};
        for (JButton btn : bottoniBlu) {
            btn.setBackground(bluIstituzionale);
            btn.setForeground(Color.WHITE);
            btn.setFont(fontBottoni);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(true);
        }

        btnLogout.setBackground(coloreRosso);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(fontBottoni);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(true);

        comboArgomenti.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ArgomentoTirocinio) {
                    ArgomentoTirocinio arg = (ArgomentoTirocinio) value;
                    setText(arg.getTitolo() + " (" + arg.getTipo() + ") - Prof. " + arg.getDocente().getCognome());
                }
                return this;
            }
        });

        for (ArgomentoTirocinio arg : Controller.getInstance().getTuttiGliArgomenti()) {
            comboArgomenti.addItem(arg);
        }

        aggiornaInterfaccia();

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        btnRichiediTirocinio.addActionListener(e -> {
            ArgomentoTirocinio scelto = (ArgomentoTirocinio) comboArgomenti.getSelectedItem();
            if (scelto != null) {
                Controller.getInstance().richiediTirocinioPerStudente(scelto);
                Controller.getInstance().setStudenteAvvisatoRifiuto(false);
                JOptionPane.showMessageDialog(mainPanel, "Richiesta inviata!");
                aggiornaInterfaccia();
            }
        });

        btnAccediLaurea.addActionListener(e -> {
            PaginaLaurea paginaLaurea = new PaginaLaurea();
            paginaLaurea.setVisible(true);
            dispose();
        });
    }

    private void aggiornaInterfaccia() {
        Studente studente = (Studente) Controller.getInstance().getUtenteLoggato();
        RichiestaTirocinio richiesta = Controller.getInstance().getRichiestaAggiornataPerStudente(studente.getId());

        lblStatoRichiesta.setForeground(Color.BLACK);

        if (richiesta == null) {
            lblStatoRichiesta.setText("Stato: Nessun tirocinio richiesto.");
            comboArgomenti.setEnabled(true);
            btnRichiediTirocinio.setEnabled(true);
            btnAccediLaurea.setEnabled(false);
        } else {
            switch (richiesta.getStato()) {
                case IN_ATTESA:
                    lblStatoRichiesta.setText("<html>Stato: Richiesta in attesa<br>di valutazione.</html>");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(false);
                    break;
                case RIFIUTATA:
                    lblStatoRichiesta.setForeground(Color.RED);
                    lblStatoRichiesta.setText("<html>Stato: Richiesta RIFIUTATA.<br>Scegli un altro argomento.</html>");
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
                    lblStatoRichiesta.setText("<html>Stato: Tirocinio ACCETTATO e in corso!<br>Puoi procedere alla laurea.</html>");
                    comboArgomenti.setEnabled(false);
                    btnRichiediTirocinio.setEnabled(false);
                    btnAccediLaurea.setEnabled(true);
                    break;
            }
        }
    }
}