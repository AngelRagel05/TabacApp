package com.tabacapp.gui;

import com.tabacapp.db.ProductoDAO;
import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Date;

public class AdminWindow extends JFrame {

    private ProductoDAO productoDAO;    // Objeto para operaciones BD con productos
    private ProductoPanel productoPanel; // Panel con tabla de productos
    private JTextArea textArea;          // Área para mensajes al usuario
    private Connection conn;             // Conexión a BD (no se usa directamente aquí)
    private MenuWindow menuWindow;       // Referencia a ventana menú para volver

    // Constructor que recibe la ventana menú y conexión a BD
    public AdminWindow(MenuWindow menuWindow2, Connection conn) {
        this.menuWindow = menuWindow2;
        this.productoDAO = new ProductoDAO(conn); // Inicializa DAO con la conexión

        // Configuración básica de la ventana
        setTitle("TabacApp - Panel Administrador");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x4E342E)); // Fondo marrón oscuro

        // Crea y añade el panel con la tabla de productos en el centro
        productoPanel = new ProductoPanel(productoDAO);
        add(productoPanel, BorderLayout.CENTER);

        // Panel superior con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(0x4E342E)); // Fondo marrón oscuro
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Distribución horizontal con espacio

        // Crear botones con texto e iconos
        JButton btnAgregar = crearBoton("➕ Agregar producto");
        JButton btnEliminar = crearBoton("❌ Eliminar producto");
        JButton btnBeneficio = crearBoton("💰 Calcular beneficio total");
        btnBeneficio.setPreferredSize(new Dimension(280, 40));  // Botón más ancho
        JButton btnSalir = crearBoton("Salir al menú");

        // Añade los botones al panel superior
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBeneficio);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.NORTH);

        // Área de texto en la parte inferior para mostrar mensajes
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false); // Solo lectura
        textArea.setBackground(new Color(0x4E342E)); // Marrón oscuro
        textArea.setForeground(new Color(0xFFF8E1)); // Texto beige claro
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(textArea), BorderLayout.SOUTH); // Scroll si hay mucho texto

        // Asigna acciones a los botones
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBeneficio.addActionListener(e -> calcularBeneficio());
        btnSalir.addActionListener(e -> {
            dispose();               // Cierra ventana actual
            menuWindow2.setVisible(true); // Vuelve a mostrar menú principal
        });

        setVisible(true); // Mostrar ventana
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

        // Cambiar color al pasar ratón (hover)
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

    // Metodo para agregar un producto pidiendo datos al usuario con cuadros de diálogo
    private void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del producto:");
            if (nombre == null || nombre.trim().isEmpty()) return; // Si cancela o vacío, salir

            String marca = JOptionPane.showInputDialog(this, "Marca:");
            if (marca == null) marca = "";

            String tipo = JOptionPane.showInputDialog(this, "Tipo:");
            if (tipo == null) tipo = "";

            String precioStr = JOptionPane.showInputDialog(this, "Precio:");
            if (precioStr == null) return; // Cancelar
            double precio = Double.parseDouble(precioStr); // Convertir a double

            String stockStr = JOptionPane.showInputDialog(this, "Stock:");
            if (stockStr == null) return; // Cancelar
            int stock = Integer.parseInt(stockStr); // Convertir a entero

            Date fechaAlta = new Date(); // Fecha actual para alta del producto

            String nombreProveedor = JOptionPane.showInputDialog(this, "Proveedor (nombre):");
            if (nombreProveedor == null) nombreProveedor = "";

            // Crear objeto Proveedor y asignar nombre
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(nombreProveedor);

            // Crear nuevo producto con id null (lo asigna la BD)
            Producto nuevo = new Producto(null, nombre, marca, tipo, precio, stock, fechaAlta, proveedor);

            productoDAO.insertar(nuevo);          // Insertar producto en la base de datos
            productoPanel.cargarProductos();      // Actualizar tabla de productos
            textArea.setText("Producto agregado correctamente."); // Mensaje éxito

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para eliminar el producto seleccionado en la tabla
    private void eliminarProducto() {
        int fila = productoPanel.getTabla().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            return;
        }
        Integer idProducto = (Integer) productoPanel.getTabla().getValueAt(fila, 0); // ID del producto
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar el producto con ID " + idProducto + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                productoDAO.eliminar(idProducto);   // Elimina producto de BD
                productoPanel.cargarProductos();    // Actualiza tabla
                textArea.setText("Producto eliminado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Metodo para calcular beneficio total: suma (precio * stock) de todos productos
    private void calcularBeneficio() {
        try {
            double beneficio = productoDAO.calcularBeneficioTotal();
            textArea.setText("Beneficio total (suma precios x stock): " + beneficio + " €");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al calcular beneficio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
