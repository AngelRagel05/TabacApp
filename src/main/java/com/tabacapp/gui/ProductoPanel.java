package com.tabacapp.gui;

import com.tabacapp.dao.ProductoDAO;
import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

/**
 * Panel reutilizable para mostrar y gestionar productos.
 * Se puede incluir dentro de una ventana como AdminWindow.
 */
public class ProductoPanel extends JPanel {

    private ProductoDAO productoDAO;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public ProductoPanel(Connection conn) {
        this.productoDAO = new ProductoDAO(conn);

        setLayout(new BorderLayout());

        // Configuración de la tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Marca", "Tipo", "Precio", "Stock", "Proveedor"}, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // Botón recargar
        JButton btnRecargar = new JButton("Recargar productos");
        btnRecargar.addActionListener(e -> cargarProductos());
        add(btnRecargar, BorderLayout.SOUTH);

        // Cargar productos al iniciar
        cargarProductos();
    }

//    Carga todos los productos desde la base de datos y los muestra en la tabla.
    private void cargarProductos() {
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
            List<Producto> productos = productoDAO.obtenerTodos();

            for (Producto p : productos) {
                modeloTabla.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getMarca(),
                        p.getTipo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getProveedor().getNombre()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
