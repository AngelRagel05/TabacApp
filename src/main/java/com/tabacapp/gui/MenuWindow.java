package com.tabacapp.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MenuWindow extends JFrame {

    private Connection conn;

    public MenuWindow(Connection conn) {
        this.conn = conn;

        setTitle("TabacApp - Menú");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(280, 480); // Tamaño tipo móvil
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0x4E342E)); // Marrón oscuro fondo

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0x4E342E));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cargar imagen (colócala en resources y ajusta esta ruta si es necesario)
        ImageIcon icon = new ImageIcon("src/main/resources/img/TabacApp.png");  // Ruta de la imagen
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);

        panel.add(Box.createVerticalStrut(20)); // Espacio debajo de la imagen

        JButton btnAdmin = new JButton("🔐 Admin");
        JButton btnUsuario = new JButton("👤 Usuario");

        configurarBoton(btnAdmin);
        configurarBoton(btnUsuario);

        panel.add(btnAdmin);
        panel.add(Box.createVerticalStrut(15)); // Espacio entre botones
        panel.add(btnUsuario);

        add(panel);

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

    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 16));
        boton.setBackground(new Color(0xFFF3E0)); // Beige claro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x795548), 2));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 40)); // Botón más pequeño
    }
}
