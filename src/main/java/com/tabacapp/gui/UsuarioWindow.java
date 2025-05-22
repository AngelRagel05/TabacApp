package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class UsuarioWindow extends JFrame {

    private ProductoDAO productoDAO;       // Acceso a BD para productos
    private ProductoPanel productoPanel;   // Panel que muestra la tabla con productos
    private MenuWindow menuWindow;        // Ventana menú para volver

    // Constructor recibe ventana menú y conexión a BD
    public UsuarioWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow = menuWindow2;
        this.productoDAO = new ProductoDAO(conn); // Crear DAO con conexión

        // Configuración ventana
        setTitle("TabacApp - Usuario");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra t@do al salir
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x4E342E)); // Fondo marrón oscuro

        // Panel superior con botones de búsqueda
        JPanel botonesPanel = new JPanel(new FlowLayout());
        botonesPanel.setBackground(new Color(0x4E342E));

        // Crear botones de búsqueda con iconos
        JButton btnNombre = crearBoton("🔍 Buscar por nombre");
        JButton btnMarca = crearBoton("🔍 Buscar por marca");
        JButton btnProveedor = crearBoton("🔍 Buscar por proveedor");
        JButton btnPrecioMax = crearBoton("🔍 Buscar por precio máximo");
        JButton btnMostrarTodos = crearBoton("🔁 Mostrar todos");

        // Ajustar tamaño uniforme a los botones
        btnNombre.setPreferredSize(new Dimension(190, 40));
        btnMarca.setPreferredSize(new Dimension(190, 40));
        btnProveedor.setPreferredSize(new Dimension(190, 40));
        btnPrecioMax.setPreferredSize(new Dimension(230, 40));
        btnMostrarTodos.setPreferredSize(new Dimension(150, 40));

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

        // Botón para volver al menú principal (abajo)
        JButton btnVolver = crearBoton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            menuWindow2.setVisible(true); // Muestra menú principal
        });
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(0x4E342E));
        panelSur.add(btnVolver);
        add(panelSur, BorderLayout.SOUTH);

        // Eventos para los botones de búsqueda:

        // Buscar por nombre
        btnNombre.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre:");
            if (nombre != null) {
                productoPanel.buscarProductos(nombre, null, null, null);
            }
        });

        // Buscar por marca
        btnMarca.addActionListener(e -> {
            String marca = JOptionPane.showInputDialog(this, "Introduce la marca:");
            if (marca != null) {
                productoPanel.buscarProductos(null, marca, null, null);
            }
        });

        // Buscar por proveedor
        btnProveedor.addActionListener(e -> {
            String proveedor = JOptionPane.showInputDialog(this, "Introduce el proveedor:");
            if (proveedor != null) {
                productoPanel.buscarProductos(null, null, proveedor, null);
            }
        });

        // Buscar por precio máximo (validando número)
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

    // Metodo para crear botones con estilo y efecto hover
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(new Color(0x8D6E63)); // Marrón claro
        boton.setForeground(new Color(0x000000)); // Texto negro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x6D4C41), 2)); // Borde marrón
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 40));

        // Cambia color al pasar ratón (hover)
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0xD7CCC8)); // Beige claro
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0x8D6E63)); // Marrón claro
            }
        });

        return boton;
    }
}
