package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Finestra di registrazione al sistema.
 */
public class RegistrazioneFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel lblLogo;
    private JComboBox<String> cmbTipoUtente;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtEmail;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtMatricola;
    private JButton btnRegistrati;


    public RegistrazioneFrame() {
        setContentPane(mainPanel);
        setTitle("Registrazione al Sistema");
        setSize(680, 700);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.setBorder(new EmptyBorder(10, 40, 10, 40));

        Color bluIstituzionale = new Color(0, 51, 102);

        for (Component c : mainPanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(bluIstituzionale);
                c.setFont(c.getFont().deriveFont(Font.BOLD));
            }
        }

        btnRegistrati.setBackground(bluIstituzionale);
        btnRegistrati.setForeground(Color.WHITE);
        btnRegistrati.setOpaque(true);
        btnRegistrati.setBorderPainted(false);
        btnRegistrati.setContentAreaFilled(true);
        UIManager.put("Button.background", bluIstituzionale);

        try {
            ImageIcon icon = new ImageIcon("logo.png");
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            lblLogo.setText("LOGO");
        }

        cmbTipoUtente.addItem("Studente");
        cmbTipoUtente.addItem("Docente");

        cmbTipoUtente.addActionListener(e -> {
            String scelta = (String) cmbTipoUtente.getSelectedItem();
            if ("Docente".equals(scelta)) {
                String codice = JOptionPane.showInputDialog(this, "Inserisci il codice di autorizzazione:", "Verifica Docente", JOptionPane.QUESTION_MESSAGE);
                if (!"codice".equals(codice)) {
                    JOptionPane.showMessageDialog(this, "Codice errato!");
                    cmbTipoUtente.setSelectedItem("Studente");
                } else {
                    txtMatricola.setEnabled(false);
                    txtMatricola.setText("");
                }
            } else {
                txtMatricola.setEnabled(true);
            }
        });

        btnRegistrati.addActionListener(e -> {
            String ruolo = (String) cmbTipoUtente.getSelectedItem();
            String password = new String(txtPassword.getPassword());

            Controller.getInstance().registraUtente(
                    txtNome.getText(),
                    txtCognome.getText(),
                    txtEmail.getText(),
                    txtUsername.getText(),
                    password,
                    txtMatricola.getText(),
                    ruolo
            );

            JOptionPane.showMessageDialog(this, "Registrazione effettuata con successo!");
            dispose();
        });
    }
}