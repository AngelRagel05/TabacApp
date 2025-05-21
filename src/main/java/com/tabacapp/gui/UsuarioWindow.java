package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClienteWindow extends JFrame {

    private ProductoDAO productoDAO;
    private JTextArea textArea;

    public ClienteWindow(Connection conn) {
        this.productoDAO = new ProductoDAO(conn);

        setTitle("Panel Cliente - Ver Productos");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel botonesPanel = new JPanel();
        JButton btnNombre = new JButton("🔍 Buscar por nombre");
        JButton btnFecha = new JButton("📅 Buscar por fecha");

        botonesPanel.add(btnNombre);
        botonesPanel.add(btnFecha);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);

        add(botonesPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnNombre.addActionListener(e -> buscarPorNombre());
        btnFecha.addActionListener(e -> buscarPorFecha());

        setVisible(true);
    }

    private void buscarPorNombre() {
        try {
            String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre del producto:");
            List<Producto> productos = productoDAO.buscarPorNombre(nombre);
            mostrarProductos(productos);
        } catch (Exception e) {
            mostrarError("Error en búsqueda por nombre");
        }
    }

    private void buscarPorFecha() {
        try {
            String input = JOptionPane.showInputDialog(this, "Introduce la fecha (YYYY-MM-DD):");
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(input);
            List<Producto> productos = productoDAO.buscarPorFecha(fecha);
            mostrarProductos(productos);
        } catch (Exception e) {
            mostrarError("Error en búsqueda por fecha");
        }
    }

    private void mostrarProductos(List<Producto> productos) {
        textArea.setText("");
        for (Producto p : productos) {
            textArea.append(p + "\n");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
