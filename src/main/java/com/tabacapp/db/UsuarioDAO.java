package com.tabacapp.db;

import com.tabacapp.model.Cliente;
import com.tabacapp.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Metodo para obtener todos los usuarios junto con su cliente asociado (si lo tienen)
    public static List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        // Consulta SQL que une usuarios con clientes (LEFT JOIN para traer usuarios sin cliente también)
        String sql = "SELECT u.*, c.nombre as nombre_cliente, c.edad, c.email, c.telefono " +
                "FROM usuarios u LEFT JOIN clientes c ON u.id_cliente = c.id_cliente";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = null;
                // Si el usuario tiene un cliente asociado (id_cliente distinto de 0)
                if (rs.getInt("id_cliente") != 0) {
                    // Crear objeto Cliente con los datos del resultado
                    cliente = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre_cliente"),
                            rs.getInt("edad"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                }

                // Crear objeto Usuario con los datos y el cliente (puede ser null)
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        cliente
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuarios: " + e.getMessage());
        }

        // Devolver lista con todos los usuarios encontrados
        return lista;
    }

    // Metodo para buscar un usuario por su nombre de usuario (username)
    public static Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Poner el valor para el placeholder de nombre_usuario
            pstmt.setString(1, nombreUsuario);

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Si encuentra un usuario con ese nombre, devolver el objeto Usuario
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        null // no busca el cliente aquí, podría añadirse si se quiere
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
        }

        // Si no encuentra nada, devolver null
        return null;
    }
}
