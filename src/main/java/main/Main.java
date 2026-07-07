package main;

import gui.LoginFrame;
import javax.swing.*;
import java.awt.Font;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setGlobalFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginFrame finestraLogin = new LoginFrame();
        finestraLogin.setVisible(true);
    }

    private static void setGlobalFont(javax.swing.plaf.FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}