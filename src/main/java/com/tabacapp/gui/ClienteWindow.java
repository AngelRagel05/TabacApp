package com.tabacapp.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

/**
 * Ventana para clientes del sistema.
 * Permite al cliente ver productos disponibles y realizar acciones básicas.
 */
public class ClienteWindow extends JFrame {

    private Connection conn;
    private String nombreCliente; // Nombre para mostrar en la ventana

    /**
     * Constructor que recibe la conexión y el nombre del cliente.
     * @param conn conexión a la base de datos
     * @param nombreCliente nombre del cliente para mostrar en la ventana
     */
    public ClienteWindow(Connection conn, String nombreCliente) {
        this.conn = conn;
        this.nombreCliente = nombreCliente;

        setTitle("TabacApp - Bienvenido, " + nombreCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);  // Centrar ventana

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Etiqueta arriba con saludo al cliente
        JLabel saludo = new JLabel("Hola, " + nombreCliente + ". Aquí están los productos disponibles:");
        saludo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(saludo, BorderLayout.NORTH);

        // Panel productos (reutilizamos ProductoPanel para mostrar productos)
        ProductoPanel productosPanel = new ProductoPanel(conn);
        panelPrincipal.add(productosPanel, BorderLayout.CENTER);

        add(panelPrincipal);

        setVisible(true);
    }
}
