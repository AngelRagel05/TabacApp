package com.tabacapp.db;

import com.tabacapp.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
            System.err.println("❌ Error al obtener clientes: " + e.getMessage());
        }

        return lista;
    }

    public static boolean agregar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, edad, email, telefono) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setInt(2, cliente.getEdad());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getTelefono());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
