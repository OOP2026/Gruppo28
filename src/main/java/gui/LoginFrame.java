package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {

        setContentPane(mainPanel);
        setTitle("Sistema Gestione Lauree - Accesso");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());


                boolean successo = Controller.getInstance().login(username, password);

                if (successo) {
                    JOptionPane.showMessageDialog(mainPanel, "Accesso eseguito!");
                    dispose();

                    Controller.getInstance().apriHomeUtente();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
