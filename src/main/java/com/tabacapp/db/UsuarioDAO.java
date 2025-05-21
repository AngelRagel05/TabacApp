package com.tabacapp.db;

import com.tabacapp.model.Cliente;
import com.tabacapp.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, c.nombre as nombre_cliente, c.edad, c.email, c.telefono " +
                "FROM usuarios u LEFT JOIN clientes c ON u.id_cliente = c.id_cliente";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = null;
                if (rs.getInt("id_cliente") != 0) {
                    cliente = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre_cliente"),
                            rs.getInt("edad"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                }

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

        return lista;
    }

    public static Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        null // puedes buscar cliente si hace falta
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }
}
