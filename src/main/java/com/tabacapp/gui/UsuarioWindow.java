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
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel superior con botones de búsqueda
        JPanel botonesPanel = new JPanel(new FlowLayout());

        JButton btnNombre = new JButton("🔍 Buscar por nombre");
        JButton btnMarca = new JButton("🔍 Buscar por marca");
        JButton btnProveedor = new JButton("🔍 Buscar por proveedor");
        JButton btnPrecioMax = new JButton("🔍 Buscar por precio máximo");
        JButton btnMostrarTodos = new JButton("🔁 Mostrar todos");

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
        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose();
            menuWindow2.setVisible(true);
        });
        add(btnVolver, BorderLayout.SOUTH);

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
}
