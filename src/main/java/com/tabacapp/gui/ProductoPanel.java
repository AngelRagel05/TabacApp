package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setDefaultRenderer(Object.class, new ZebraRenderer()); // 👈 Aquí aplicamos la clase personalizada

        tabla.setRowHeight(25); // Opcional: altura de las filas
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

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

    public void buscarProductos(String nombre, String marca, String proveedor, Double precioMaximo) {
        try {
            modelo.setRowCount(0);
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

    // 🔽 Clase interna para renderizado en zebra (gris / blanco)
    private static class ZebraRenderer extends DefaultTableCellRenderer {
        private static final Color GRIS_CLARO = new Color(190, 190, 190);
        private static final Color BLANCO = Color.WHITE;
        private static final Color SELECCION = new Color(163, 201, 239);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(SELECCION);
                c.setForeground(Color.BLACK); // 👈 texto negro al seleccionar
            } else {
                c.setForeground(Color.BLACK); // 👈 texto negro por defecto
                c.setBackground((row % 2 == 0) ? GRIS_CLARO : BLANCO);
            }

            return c;
        }
    }

}
