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
        setSize(350, 200);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0x4E342E)); // Fondo marrón estanco

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(new Color(0x4E342E));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAdmin = new JButton("🔐 Entrar como Admin");
        JButton btnUsuario = new JButton("👤 Entrar como Usuario");

        // Estilo de botones
        Color beige = new Color(0xFFF3E0);
        Font fuente = new Font("SansSerif", Font.BOLD, 14);

        configurarBoton(btnAdmin, fuente, beige);
        configurarBoton(btnUsuario, fuente, beige);

        panel.add(btnAdmin);
        panel.add(btnUsuario);
        add(panel);

        // Acción con contraseña para Admin
        btnAdmin.addActionListener(e -> {
            JPasswordField pwdField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    this,
                    pwdField,
                    "Introduce la contraseña de administrador:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                String pass = new String(pwdField.getPassword());
                if ("123456".equals(pass)) {
                    new AdminWindow(this, conn);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnUsuario.addActionListener(e -> {
            new UsuarioWindow(this, conn);
            this.setVisible(false);
        });

        setVisible(true);
    }

    private void configurarBoton(JButton boton, Font fuente, Color fondo) {
        boton.setFont(fuente);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x795548), 2)); // Borde marrón más claro
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
