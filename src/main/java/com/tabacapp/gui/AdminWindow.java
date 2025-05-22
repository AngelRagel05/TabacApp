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
    private MenuWindow menuWindow;

    public AdminWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow = menuWindow2;
        this.productoDAO = new ProductoDAO(conn);

        setTitle("TabacApp - Panel Administrador");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x4E342E)); // Marrón oscuro

        productoPanel = new ProductoPanel(productoDAO);
        add(productoPanel, BorderLayout.CENTER);

        // Panel de botones con fondo oscuro
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x4E342E)); // Marrón oscuro
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botones estilizados
        JButton btnAgregar = crearBoton("➕ Agregar producto");
        JButton btnEliminar = crearBoton("❌ Eliminar producto");
        JButton btnBeneficio = crearBoton("💰 Calcular beneficio total");
        btnBeneficio.setPreferredSize(new Dimension(280, 40));  // Más ancho para que no se corte
        JButton btnSalir = crearBoton("Salir al menú");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBeneficio);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.NORTH);

        // Área de texto con estilo
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        textArea.setBackground(new Color(0x4E342E)); // Marrón oscuro
        textArea.setForeground(new Color(0xFFF8E1)); // Beige claro
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(textArea), BorderLayout.SOUTH);

        // Listeners
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBeneficio.addActionListener(e -> calcularBeneficio());
        btnSalir.addActionListener(e -> {
            dispose();
            menuWindow2.setVisible(true);
        });

        setVisible(true);
    }

    // Método para estilizar botones
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

    private void agregarProducto() {
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
