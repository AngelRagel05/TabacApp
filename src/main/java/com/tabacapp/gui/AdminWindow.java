package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Date;

public class AdminWindow extends JFrame {

    private ProductoDAO productoDAO;    // DAO para gestionar productos en BD
    private ProductoPanel productoPanel; // Panel que muestra la tabla de productos
    private JTextArea textArea;          // Área para mostrar mensajes al usuario
    private Connection conn;             // Conexión a BD (no se usa directamente aquí)
    private MenuWindow menuWindow;       // Ventana menú para volver a ella

    public AdminWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow = menuWindow2;
        this.productoDAO = new ProductoDAO(conn); // Inicializa DAO con conexión

        // Configuración ventana
        setTitle("TabacApp - Panel Administrador");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x4E342E)); // Fondo marrón oscuro

        // Panel con la tabla de productos
        productoPanel = new ProductoPanel(productoDAO);
        add(productoPanel, BorderLayout.CENTER);

        // Panel con botones arriba, fondo marrón oscuro
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x4E342E));
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botones principales con iconos y texto
        JButton btnAgregar = crearBoton("➕ Agregar producto");
        JButton btnEliminar = crearBoton("❌ Eliminar producto");
        JButton btnBeneficio = crearBoton("💰 Calcular beneficio total");
        btnBeneficio.setPreferredSize(new Dimension(280, 40));  // Más ancho que los demás
        JButton btnSalir = crearBoton("Salir al menú");

        // Añade los botones al panel de botones
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBeneficio);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.NORTH);

        // Área de texto para mensajes debajo, estilo acorde al fondo
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        textArea.setBackground(new Color(0x4E342E)); // Marrón oscuro
        textArea.setForeground(new Color(0xFFF8E1)); // Beige claro
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(textArea), BorderLayout.SOUTH);

        // Acciones para botones
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBeneficio.addActionListener(e -> calcularBeneficio());
        btnSalir.addActionListener(e -> {
            dispose();               // Cierra esta ventana
            menuWindow2.setVisible(true); // Muestra menú principal otra vez
        });

        setVisible(true);
    }

    // Metodo para crear botones con estilo uniforme y efecto hover
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(new Color(0x8D6E63)); // Marrón claro
        boton.setForeground(new Color(0x000000)); // Texto negro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0x6D4C41), 2)); // Borde marrón medio
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(180, 40));

        // Cambia el color de fondo al pasar el ratón por encima
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0xD7CCC8)); // Beige claro (hover)
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0x8D6E63)); // Marrón claro (normal)
            }
        });

        return boton;
    }

    // Agrega un producto pidiendo datos mediante cuadros de diálogo
    private void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) return; // Cancelar si vacío

            String marca = JOptionPane.showInputDialog(this, "Marca:");
            if (marca == null) marca = "";

            String tipo = JOptionPane.showInputDialog(this, "Tipo:");
            if (tipo == null) tipo = "";

            String precioStr = JOptionPane.showInputDialog(this, "Precio:");
            if (precioStr == null) return; // Cancelar
            double precio = Double.parseDouble(precioStr); // Convierte a double

            String stockStr = JOptionPane.showInputDialog(this, "Stock:");
            if (stockStr == null) return; // Cancelar
            int stock = Integer.parseInt(stockStr); // Convierte a int

            Date fechaAlta = new Date(); // Fecha actual para alta producto

            String nombreProveedor = JOptionPane.showInputDialog(this, "Proveedor (nombre):");
            if (nombreProveedor == null) nombreProveedor = "";

            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(nombreProveedor);

            // Crea el producto nuevo con id null porque lo asigna la BD
            Producto nuevo = new Producto(null, nombre, marca, tipo, precio, stock, fechaAlta, proveedor);

            productoDAO.insertar(nuevo);  // Inserta en BD
            productoPanel.cargarProductos(); // Refresca tabla
            textArea.setText("Producto agregado correctamente."); // Mensaje confirmación

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Elimina el producto seleccionado en la tabla
    private void eliminarProducto() {
        int fila = productoPanel.getTabla().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            return;
        }
        Integer idProducto = (Integer) productoPanel.getTabla().getValueAt(fila, 0); // ID de la fila seleccionada
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar el producto con ID " + idProducto + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                productoDAO.eliminar(idProducto);   // Elimina de BD
                productoPanel.cargarProductos();    // Actualiza tabla
                textArea.setText("Producto eliminado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Calcula el beneficio total sumando (precio * stock) de todos los productos
    private void calcularBeneficio() {
        try {
            double beneficio = productoDAO.calcularBeneficioTotal();
            textArea.setText("Beneficio total (suma precios x stock): " + beneficio + " €");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al calcular beneficio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}