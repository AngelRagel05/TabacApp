package com.tabacapp;

// Importación de clases necesarias
import com.tabacapp.db.DBConnection;  // Clase que gestiona la conexión a la base de datos
import com.tabacapp.gui.AdminWindow; // Ventana para el administrador (no se usa directamente aquí)
import com.tabacapp.gui.MenuWindow;  // Ventana principal del menú (la que se lanza)
import com.tabacapp.gui.ProductoPanel; // Panel de productos (no se usa directamente aquí)
import com.tabacapp.gui.UsuarioWindow; // Ventana para usuarios normales (no se usa directamente aquí)

import javax.swing.*;                // Librería para crear interfaces gráficas
import java.sql.Connection;          // Para manejar la conexión a la base de datos

public class Ejecutar {
    public static void main(String[] args) {
        try {
            // Establece el estilo visual del sistema operativo (opcional, solo para estética)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Se obtiene una única conexión a la base de datos desde DBConnection
            Connection conn = DBConnection.getConnection();

            // Ejecuta la interfaz gráfica en el hilo de eventos de Swing
            SwingUtilities.invokeLater(() -> new MenuWindow(conn)); // Abre la ventana de menú principal

        } catch (Exception e) {
            // Muestra errores en consola si algo falla al iniciar
            System.err.println("Error iniciando aplicación: " + e.getMessage());
        }
    }
}
