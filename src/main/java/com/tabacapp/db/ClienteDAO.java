package com.tabacapp.db;

import com.tabacapp.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Metodo para obtener todos los clientes de la base de datos
    public static List<Cliente> obtenerTodos() {

        // Lista donde guardaremos los clientes que obtengamos
        List<Cliente> lista = new ArrayList<>();

        // Consulta SQL para seleccionar todos los clientes
        String sql = "SELECT * FROM clientes";

        // Conectar a la base de datos, crear la consulta y ejecutar
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Mientras haya un siguiente resultado, crear un cliente y añadirlo a la lista
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getString("email"),
                        rs.getString("telefono")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            // En caso de error, imprimir mensaje con el detalle
            System.err.println("❌ Error al obtener clientes: " + e.getMessage());
        }

        // Devolver la lista con todos los clientes (vacía si falla o no hay datos)
        return lista;
    }

    // Metodo para agregar un nuevo cliente a la base de datos
    public static boolean agregar(Cliente cliente) {
        // Consulta SQL para insertar nuevo cliente, con placeholders para evitar inyección SQL
        String sql = "INSERT INTO clientes (nombre, edad, email, telefono) VALUES (?, ?, ?, ?)";

        // Conectar a la base de datos y preparar la consulta
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Rellenar los placeholders con los datos del cliente
            pstmt.setString(1, cliente.getNombre());
            pstmt.setInt(2, cliente.getEdad());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getTelefono());

            // Ejecutar la inserción
            pstmt.executeUpdate();

            // Devolver true si se agregó correctamente
            return true;

        } catch (SQLException e) {
            // En caso de error, mostrar mensaje y devolver false
            System.err.println("❌ Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    // Metodo para eliminar un cliente por su ID
    public static boolean eliminar(int id) {
        // Consulta SQL para borrar el cliente con el id indicado
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        // Conectar a la base de datos y preparar la consulta
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el ID al placeholder
            pstmt.setInt(1, id);

            // Ejecutar el borrado y devolver true si se eliminó al menos un registro
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Mostrar error y devolver false si falla
            System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
