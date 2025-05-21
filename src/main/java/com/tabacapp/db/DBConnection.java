package com.tabacapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Datos para conectarse a la base de datos MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/TabacApp?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Objeto Connection estático, para reutilizar la conexión
    private static Connection connection;

    // Metodo para obtener la conexión (singleton)
    public static Connection getConnection() {
        try {
            // Si no hay conexión o está cerrada, crear una nueva
            if (connection == null || connection.isClosed()) {
                // Cargar el driver JDBC de MySQL (puede que no haga falta en versiones nuevas)
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Conectar con los datos de URL, usuario y contraseña
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión exitosa a la base de datos.");
            }
        } catch (ClassNotFoundException e) {
            // Error si no se encuentra el driver JDBC
            System.err.println("❌ Driver JDBC no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            // Error si falla la conexión
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
        }
        // Devolver la conexión (puede ser null si hubo error)
        return connection;
    }

    // Metodo para cerrar la conexión, si está abierta
    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔒 Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    // Metodo para forzar una reconexión: cierra y abre la conexión otra vez
    public static void reconectar() {
        cerrarConexion();
        getConnection();
    }
}
