package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class UsuarioWindow extends JFrame {

    private ProductoDAO productoDAO;
    private ProductoPanel productoPanel;
    private MenuWindow menuWindow2;

    public UsuarioWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow2 = menuWindow2;
        this.productoDAO = new ProductoDAO(conn);

        setTitle("TabacApp - Usuario");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x4E342E)); // Marrón oscuro

        // Panel superior con botones de búsqueda
        JPanel botonesPanel = new JPanel(new FlowLayout());
        botonesPanel.setBackground(new Color(0x4E342E)); // Marrón oscuro

        JButton btnNombre = crearBoton("🔍 Buscar por nombre");
        JButton btnMarca = crearBoton("🔍 Buscar por marca");
        JButton btnProveedor = crearBoton("🔍 Buscar por proveedor");
        JButton btnPrecioMax = crearBoton("🔍 Buscar por precio máximo");
        JButton btnMostrarTodos = crearBoton("🔁 Mostrar todos");

        btnNombre.setPreferredSize(new Dimension(190, 40));
        btnMarca.setPreferredSize(new Dimension(190, 40));
        btnProveedor.setPreferredSize(new Dimension(190, 40));
        btnPrecioMax.setPreferredSize(new Dimension(190, 40));
        btnMostrarTodos.setPreferredSize(new Dimension(190, 40));

        botonesPanel.add(btnNombre);
        botonesPanel.add(btnMarca);
        botonesPanel.add(btnProveedor);
        botonesPanel.add(btnPrecioMax);
        botonesPanel.add(btnMostrarTodos);

        add(botonesPanel, BorderLayout.NORTH);

        // Panel central con tabla de productos
        productoPanel = new ProductoPanel(productoDAO);
        add(productoPanel, BorderLayout.CENTER);

        // Botón volver
        JButton btnVolver = crearBoton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose();
            menuWindow2.setVisible(true);
        });
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(0x4E342E));
        panelSur.add(btnVolver);
        add(panelSur, BorderLayout.SOUTH);

        // Acciones de botones
        btnNombre.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre:");
            if (nombre != null) {
                productoPanel.buscarProductos(nombre, null, null, null);
            }
        });

        btnMarca.addActionListener(e -> {
            String marca = JOptionPane.showInputDialog(this, "Introduce la marca:");
            if (marca != null) {
                productoPanel.buscarProductos(null, marca, null, null);
            }
        });

        btnProveedor.addActionListener(e -> {
            String proveedor = JOptionPane.showInputDialog(this, "Introduce el proveedor:");
            if (proveedor != null) {
                productoPanel.buscarProductos(null, null, proveedor, null);
            }
        });

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

        btnMostrarTodos.addActionListener(e -> productoPanel.cargarProductos());

        setVisible(true);
    }

    // Metodo para estilizar botones con colores iguales a AdminWindow
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(new Color(0x8D6E63)); // Marrón claro
        boton.setForeground(new Color(0x000000)); // Negro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x6D4C41), 2)); // Marrón medio
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 40));

        // Efecto hover
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
