package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UsuarioWindow extends JFrame {

    private ProductoDAO productoDAO;
    private JTextArea textArea;
    private MenuWindow menuWindow;

    public UsuarioWindow(MenuWindow menuWindow, Connection conn) {
        this.menuWindow = menuWindow;
        this.productoDAO = new ProductoDAO(conn);

        setTitle("TabacApp - Usuario");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel botonesPanel = new JPanel();
        JButton btnNombre = new JButton("🔍 Buscar por nombre");
        // Añade otros botones aquí (marca, proveedor, precio) si quieres

        botonesPanel.add(btnNombre);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);

        add(botonesPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnNombre.addActionListener(e -> buscarPorNombre());

        JButton btnVolver = new JButton("Volver al menú");
        btnVolver.addActionListener(e -> {
            dispose();
            menuWindow.setVisible(true);
        });
        add(btnVolver, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void buscarPorNombre() {
        try {
            String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre del producto:");
            List<Producto> productos = productoDAO.buscar(nombre, null, null, null, null);
            mostrarProductos(productos);
        } catch (Exception e) {
            mostrarError("Error en búsqueda por nombre");
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
