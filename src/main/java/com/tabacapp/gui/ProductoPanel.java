package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoPanel extends JPanel {

    private ProductoDAO productoDAO;
    private JTable tabla;
    private DefaultTableModel modelo;

    public ProductoPanel(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;

        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Marca", "Tipo", "Precio", "Stock", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Para que no se edite la tabla directamente
            }
        };

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnRecargar = new JButton("Recargar Productos");
        btnRecargar.addActionListener(e -> cargarProductos());
        add(btnRecargar, BorderLayout.SOUTH);

        cargarProductos();
    }

    public void cargarProductos() {
        try {
            modelo.setRowCount(0);
            List<Producto> productos = productoDAO.obtenerTodos();

            for (Producto p : productos) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getMarca(),
                        p.getTipo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getProveedor() != null ? p.getProveedor().getNombre() : "N/A"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca productos filtrando por nombre, marca, proveedor o precio máximo.
     * Carga en la tabla solo los que cumplan.
     */
    public void buscarProductos(String nombre, String marca, String proveedor, Double precioMaximo) {
        try {
            modelo.setRowCount(0);

            // precioMin = null, precioMax = precioMaximo
            List<Producto> productos = productoDAO.buscar(nombre, marca, null, precioMaximo, proveedor);

            for (Producto p : productos) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getMarca(),
                        p.getTipo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getProveedor() != null ? p.getProveedor().getNombre() : "N/A"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTabla() {
        return tabla;
    }
}
