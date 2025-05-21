package com.tabacapp.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Ventana principal para el usuario administrador.
 * Aquí el admin podrá gestionar productos, clientes, ventas, etc.
 */
public class AdminWindow extends JFrame {

    private Connection conn;

//    Constructor que recibe la conexión a la base de datos.
    public AdminWindow(Connection conn) {
        this.conn = conn;

        setTitle("TabacApp - Panel Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);  // Centrar ventana

        // Pestañas para diferentes secciones (productos, clientes, ventas...)
        JTabbedPane pestañas = new JTabbedPane();

        // Añadimos la pestaña con el panel de productos
        pestañas.addTab("Productos", new ProductoPanel(conn));

        // Aquí puedes añadir más pestañas con otros paneles, por ejemplo:
        // pestañas.addTab("Clientes", new ClientePanel(conn));
        // pestañas.addTab("Ventas", new VentaPanel(conn));

        add(pestañas);

        setVisible(true);
    }
}
