package com.tabacapp.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MenuWindow extends JFrame {

    private Connection conn; // Guarda la conexión a la base de datos

    // Constructor que recibe la conexión para usarla en otras ventanas
    public MenuWindow(Connection conn) {
        this.conn = conn;

        // Configuraciones básicas de la ventana principal
        setTitle("TabacApp - Menú"); // Título de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra la app al cerrar la ventana
        setSize(480, 800); // Tamaño de la ventana (estilo móvil grande)
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        getContentPane().setBackground(new Color(0x4E342E)); // Color marrón oscuro de fondo

        // Panel principal con layout vertical (los elementos se apilan uno debajo del otro)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0x4E342E)); // Mismo color que el fondo de la ventana
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Margen interno (padding) de 30px en todos lados

        // Icono pequeño en la barra de título
        ImageIcon iconoPeque = new ImageIcon("src/main/resources/img/Logo1.png");
        Image iconImage = iconoPeque.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(iconImage);

        // Imagen grande dentro de la ventana
        ImageIcon logoGrande = new ImageIcon("src/main/resources/img/TabacApp.png");
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(logoGrande.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH)));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);


        panel.add(Box.createVerticalStrut(30)); // Espacio vertical extra entre componentes

        // Creación de botones para admin, usuario y salir con emojis para estilo
        JButton btnAdmin = new JButton("🔐 Admin");
        JButton btnUsuario = new JButton("👤 Usuario");
        JButton btnSalir = new JButton("🚪 Salir");

        // Configura estilo y comportamiento común de los botones
        configurarBoton(btnAdmin);
        configurarBoton(btnUsuario);
        configurarBoton(btnSalir);

        // Añade los botones al panel con espacio vertical entre ellos
        panel.add(btnAdmin);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnUsuario);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnSalir);
        panel.add(Box.createVerticalStrut(30)); // Más espacio al final

        // Etiqueta con un eslogan, con fuente serif en cursiva y color claro
        JLabel slogan = new JLabel("El Tabaco No Mata");
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setFont(new Font("Serif", Font.ITALIC, 18));
        slogan.setForeground(new Color(0xFFF8E1));
        panel.add(slogan);

        add(panel); // Añade el panel principal a la ventana

        // Eventos para los botones:

        // Botón Admin: pide contraseña en un cuadro emergente
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
                if ("123456".equals(pass)) { // Contraseña fija '123456'
                    new AdminWindow(this, conn); // Abre ventana admin
                    this.setVisible(false); // Oculta esta ventana
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Botón Usuario: abre ventana usuario y oculta esta
        btnUsuario.addActionListener(e -> {
            new UsuarioWindow(this, conn);
            this.setVisible(false);
        });

        // Botón Salir: pregunta si confirma salir y cierra la aplicación si sí
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setVisible(true); // Hace visible la ventana al final de t@do
    }

    // Metodo para configurar estilo y comportamiento común en botones
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 20)); // Fuente grande y negrita
        boton.setBackground(new Color(0x8D6E63)); // Color marrón claro
        boton.setForeground(new Color(0x000000)); // Texto negro
        boton.setFocusPainted(false); // Sin borde de foco al clicar
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x6D4C41), 2)); // Borde marrón oscuro
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor tipo mano al pasar
        boton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado horizontal
        boton.setMaximumSize(new Dimension(240, 50)); // Tamaño máximo para que sean grandes

        // Cambia colores al pasar el ratón para efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0xD7CCC8)); // Fondo claro al hover
                boton.setForeground(Color.BLACK);         // Texto negro
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0x8D6E63)); // Vuelve al marrón original
                boton.setForeground(new Color(0x000000)); // Texto negro
            }
        });
    }
}