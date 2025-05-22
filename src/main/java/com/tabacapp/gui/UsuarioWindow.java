package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class UsuarioWindow extends JFrame {

    private ProductoDAO productoDAO;       // Acceso a la base de datos de productos
    private ProductoPanel productoPanel;   // Panel con tabla de productos
    private MenuWindow menuWindow2;        // Ventana de menú principal para volver

    public UsuarioWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow2 = menuWindow2;
        this.productoDAO = new ProductoDAO(conn); // Crear DAO con la conexión

        setTitle("TabacApp - Usuario");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra el progama
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x4E342E)); // Fondo marrón oscuro

        // Panel con botones de búsqueda en la parte superior
        JPanel botonesPanel = new JPanel(new FlowLayout());
        botonesPanel.setBackground(new Color(0x4E342E)); // mismo color fondo

        // Crear botones con iconos y texto
        JButton btnNombre = crearBoton("🔍 Buscar por nombre");
        JButton btnMarca = crearBoton("🔍 Buscar por marca");
        JButton btnProveedor = crearBoton("🔍 Buscar por proveedor");
        JButton btnPrecioMax = crearBoton("🔍 Buscar por precio máximo");
        JButton btnMostrarTodos = crearBoton("🔁 Mostrar todos");

        // Dar tamaño uniforme a los botones
        btnNombre.setPreferredSize(new Dimension(190, 40));
        btnMarca.setPreferredSize(new Dimension(190, 40));
        btnProveedor.setPreferredSize(new Dimension(190, 40));
        btnPrecioMax.setPreferredSize(new Dimension(190, 40));
        btnMostrarTodos.setPreferredSize(new Dimension(190, 40));

        // Añadir botones al panel superior
        botonesPanel.add(btnNombre);
        botonesPanel.add(btnMarca);
        botonesPanel.add(btnProveedor);
        botonesPanel.add(btnPrecioMax);
        botonesPanel.add(btnMostrarTodos);

        add(botonesPanel, BorderLayout.NORTH); // Panel arriba

        // Panel central con la tabla de productos
        productoPanel = new ProductoPanel(productoDAO);
        add(productoPanel, BorderLayout.CENTER);

        // Botón para volver al menú, abajo
        JButton btnVolver = crearBoton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            menuWindow2.setVisible(true); // Muestra el menú principal
        });
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(0x4E342E));
        panelSur.add(btnVolver);
        add(panelSur, BorderLayout.SOUTH);

        // Eventos de los botones de búsqueda

        // Buscar productos por nombre (pide texto, filtra con nombre)
        btnNombre.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre:");
            if (nombre != null) {
                productoPanel.buscarProductos(nombre, null, null, null);
            }
        });

        // Buscar productos por marca
        btnMarca.addActionListener(e -> {
            String marca = JOptionPane.showInputDialog(this, "Introduce la marca:");
            if (marca != null) {
                productoPanel.buscarProductos(null, marca, null, null);
            }
        });

        // Buscar productos por proveedor
        btnProveedor.addActionListener(e -> {
            String proveedor = JOptionPane.showInputDialog(this, "Introduce el proveedor:");
            if (proveedor != null) {
                productoPanel.buscarProductos(null, null, proveedor, null);
            }
        });

        // Buscar productos por precio máximo (pide número, controla error)
        btnPrecioMax.addActionListener(e -> {
            String precioStr = JOptionPane.showInputDialog(this, "Introduce el precio máximo:");
            if (precioStr != null && !precioStr.isEmpty()) {
                try {
                    double precioMax = Double.parseDouble(precioStr);
                    productoPanel.buscarProductos(null, null, null, precioMax);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Mostrar todos los productos sin filtro
        btnMostrarTodos.addActionListener(e -> productoPanel.cargarProductos());

        setVisible(true); // Mostrar ventana
    }

    // Metodo para crear botones con estilo consistente
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(new Color(0x8D6E63)); // Marrón claro
        boton.setForeground(new Color(0x000000)); // Negro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x6D4C41), 2)); // Borde marrón medio
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 40));

        // Cambia color fondo y texto al pasar el ratón (hover)
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0xD7CCC8)); // Beige claro
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0x8D6E63)); // Marrón claro original
            }
        });

        return boton;
    }
}