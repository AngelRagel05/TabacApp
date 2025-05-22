package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoPanel extends JPanel {

    private ProductoDAO productoDAO; // Objeto para acceder a datos de productos
    private JTable tabla;             // Tabla para mostrar productos
    private DefaultTableModel modelo; // Modelo de datos para la tabla

    // Constructor recibe el DAO para poder obtener productos de la base de datos
    public ProductoPanel(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;

        setLayout(new BorderLayout()); // Layout con áreas para componentes (Norte, Sur, Centro, etc)

        // Define el modelo de la tabla con columnas específicas y sin edición directa
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Marca", "Tipo", "Precio", "Stock", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que el usuario pueda modificar celdas
            }
        };

        tabla = new JTable(modelo); // Crea la tabla con el modelo definido

        // Aplica el render personalizado para filas "zebra" (gris y blanco alternado)
        tabla.setDefaultRenderer(Object.class, new ZebraRenderer());

        tabla.setRowHeight(25); // Ajusta la altura de filas para mejor visual
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Encabezados con fuente más grande y negrita

        add(new JScrollPane(tabla), BorderLayout.CENTER); // Añade la tabla dentro de un scroll al centro del panel

        // Botón para recargar la tabla con los productos actuales
        JButton btnRecargar = new JButton("Recargar Productos");
        btnRecargar.addActionListener(e -> cargarProductos()); // Cuando se clickea, llama a cargarProductos()
        add(btnRecargar, BorderLayout.SOUTH); // Coloca el botón abajo

        cargarProductos(); // Carga los productos desde la base de datos nada más crear el panel
    }

    // Metodo para cargar todos los productos y mostrarlos en la tabla
    public void cargarProductos() {
        try {
            modelo.setRowCount(0); // Limpia la tabla antes de llenar
            List<Producto> productos = productoDAO.obtenerTodos(); // Trae todos los productos

            // Añade una fila por cada producto con sus datos
            for (Producto p : productos) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getMarca(),
                        p.getTipo(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getProveedor() != null ? p.getProveedor().getNombre() : "N/A" // Si no tiene proveedor muestra "N/A"
                });
            }
        } catch (Exception ex) {
            // Muestra un mensaje de error si algo falla
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para buscar productos filtrando por nombre, marca, proveedor y precio máximo
    public void buscarProductos(String nombre, String marca, String proveedor, Double precioMaximo) {
        try {
            modelo.setRowCount(0); // Limpia tabla
            // Llama al metodo buscar del DAO, pasando los filtros (tipo se pasa como null)
            List<Producto> productos = productoDAO.buscar(nombre, marca, null, precioMaximo, proveedor);

            // Añade los productos encontrados a la tabla
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
            // Mensaje de error si falla la búsqueda
            JOptionPane.showMessageDialog(this, "Error al buscar productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTabla() {
        return tabla; // Devuelve la tabla (puede usarse para agregar listeners externos o personalizar)
    }

    // Clase interna para hacer filas con colores alternados tipo "zebra" y controlar colores al seleccionar
    private static class ZebraRenderer extends DefaultTableCellRenderer {
        private static final Color GRIS_CLARO = new Color(190, 190, 190);
        private static final Color BLANCO = Color.WHITE;
        private static final Color SELECCION = new Color(163, 201, 239); // Azul claro cuando seleccionas

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(SELECCION); // Fondo azul claro si está seleccionado
                c.setForeground(Color.BLACK); // Texto negro para buen contraste
            } else {
                c.setForeground(Color.BLACK); // Texto negro siempre
                // Alterna color de fondo entre gris claro y blanco según fila par o impar
                c.setBackground((row % 2 == 0) ? GRIS_CLARO : BLANCO);
            }

            return c; // Devuelve el componente con el estilo aplicado
        }
    }
}