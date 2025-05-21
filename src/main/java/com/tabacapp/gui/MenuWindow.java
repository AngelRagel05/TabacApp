package com.tabacapp.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MenuWindow extends JFrame {

    private Connection conn;

    public MenuWindow(Connection conn) {
        this.conn = conn;

        setTitle("TabacApp - Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton btnAdmin = new JButton("Entrar como Admin");
        JButton btnUsuario = new JButton("Entrar como Usuario");

        panel.add(btnAdmin);
        panel.add(btnUsuario);

        add(panel);

        btnAdmin.addActionListener(e -> {
            new AdminWindow(this, conn); // pasa conexión también
            this.setVisible(false);
        });

        btnUsuario.addActionListener(e -> {
            new UsuarioWindow(this, conn);
            this.setVisible(false);
        });

        setVisible(true);
    }
}
