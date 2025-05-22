package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Date;

public class AdminWindow extends JFrame {

    private ProductoDAO productoDAO;
    private ProductoPanel productoPanel;
    private JTextArea textArea;
    private Connection conn;
    private MenuWindow menuWindow2;

    public AdminWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow2 = this.menuWindow2;
        this.productoDAO = new ProductoDAO(conn);

        setTitle("TabacApp - Panel Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productoPanel = new ProductoPanel(productoDAO);
        add(productoPanel, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("➕ Agregar producto");
        JButton btnEliminar = new JButton("❌ Eliminar producto");
        JButton btnBeneficio = new JButton("💰 Calcular beneficio total");
        JButton btnSalir = new JButton("Salir al menú");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBeneficio);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.NORTH);

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBeneficio.addActionListener(e -> calcularBeneficio());
        btnSalir.addActionListener(e -> {
            dispose();
            menuWindow2.setVisible(true);
        });

        setVisible(true);
    }

    private void agregarProducto() {
        // Aquí debes implementar un diálogo para pedir datos de nuevo producto
        // Por ejemplo, JOptionPane.showInputDialog o un formulario personalizado
        try {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) return;

            String marca = JOptionPane.showInputDialog(this, "Marca:");
            if (marca == null) marca = "";

            String tipo = JOptionPane.showInputDialog(this, "Tipo:");
            if (tipo == null) tipo = "";

            String precioStr = JOptionPane.showInputDialog(this, "Precio:");
            if (precioStr == null) return;
            double precio = Double.parseDouble(precioStr);

            String stockStr = JOptionPane.showInputDialog(this, "Stock:");
            if (stockStr == null) return;
            int stock = Integer.parseInt(stockStr);

            Date fechaAlta = new Date();

            String nombreProveedor = JOptionPane.showInputDialog(this, "Proveedor (nombre):");
            if (nombreProveedor == null) nombreProveedor = "";

// Crear objeto Proveedor con solo el nombre
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(nombreProveedor);

            Producto nuevo = new Producto(null, nombre, marca, tipo, precio, stock, fechaAlta, proveedor);

            productoDAO.insertar(nuevo);
            productoPanel.cargarProductos();
            textArea.setText("Producto agregado correctamente.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int fila = productoPanel.getTabla().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            return;
        }
        Integer idProducto = (Integer) productoPanel.getTabla().getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar el producto con ID " + idProducto + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                productoDAO.eliminar(idProducto);
                productoPanel.cargarProductos();
                textArea.setText("Producto eliminado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void calcularBeneficio() {
        try {
            double beneficio = productoDAO.calcularBeneficioTotal();
            textArea.setText("Beneficio total (suma precios x stock): " + beneficio + " €");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al calcular beneficio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
