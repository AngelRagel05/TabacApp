package com.tabacapp.db;

import com.tabacapp.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    // Metodo para obtener todos los proveedores de la base de datos
    public static List<Proveedor> obtenerTodos() {
        // Lista donde se guardarán los proveedores
        List<Proveedor> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los proveedores
        String sql = "SELECT * FROM proveedores";

        // Conectar a la BBDD, crear la sentencia y ejecutar la consulta
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Mientras haya resultados, crear objeto Proveedor y añadirlo a la lista
            while (rs.next()) {
                Proveedor p = new Proveedor(
                        rs.getInt("id_proveedor"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            // Mostrar error si falla la consulta
            System.err.println("❌ Error al obtener proveedores: " + e.getMessage());
        }

        // Devolver la lista (vacía si no hay o error)
        return lista;
    }

    // Metodo para agregar un nuevo proveedor a la base de datos
    public static boolean agregar(Proveedor p) {
        // Consulta SQL para insertar nuevo proveedor con valores protegidos
        String sql = "INSERT INTO proveedores (nombre, telefono, email) VALUES (?, ?, ?)";

        // Conectar a la BBDD y preparar la sentencia
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores del proveedor a los placeholders
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getTelefono());
            pstmt.setString(3, p.getEmail());

            // Ejecutar inserción
            pstmt.executeUpdate();

            // Si va bien devolver true
            return true;

        } catch (SQLException e) {
            // Mostrar error y devolver false en caso de fallo
            System.err.println("❌ Error al agregar proveedor: " + e.getMessage());
            return false;
        }
    }

    // Metodo para eliminar un proveedor por su ID
    public static boolean eliminar(int id) {
        // Consulta SQL para borrar el proveedor con ese id
        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";

        // Conectar y preparar la sentencia
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el ID al placeholder
            pstmt.setInt(1, id);

            // Ejecutar borrado y devolver true si eliminó al menos un registro
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Mostrar error y devolver false en caso de fallo
            System.err.println("❌ Error al eliminar proveedor: " + e.getMessage());
            return false;
        }
    }
}
