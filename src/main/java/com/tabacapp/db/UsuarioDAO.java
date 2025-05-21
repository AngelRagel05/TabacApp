package com.tabacapp.db;

import com.tabacapp.model.Cliente;
import com.tabacapp.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection conn;

    // Constructor que recibe una conexión a la base de datos para usar en métodos no estáticos
    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    //    Metodo estático para obtener todos los usuarios junto con su cliente asociado (si tienen).
//    Utiliza LEFT JOIN para traer también usuarios que no tienen cliente.
    public static List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, c.nombre as nombre_cliente, c.edad, c.email, c.telefono " +
                "FROM usuarios u LEFT JOIN clientes c ON u.id_cliente = c.id_cliente";

        // Aquí se abre una conexión propia, no usa la de la instancia.
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Recorremos todos los resultados
            while (rs.next()) {
                Cliente cliente = null;
                // Si el usuario tiene cliente (id_cliente distinto de 0)
                if (rs.getInt("id_cliente") != 0) {
                    // Creamos el objeto Cliente con los datos de la consulta
                    cliente = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre_cliente"),
                            rs.getInt("edad"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                }

                // Creamos el objeto Usuario con su cliente (puede ser null)
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        cliente
                );

                // Añadimos el usuario a la lista para devolver
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuarios: " + e.getMessage());
        }

        // Devolvemos la lista con todos los usuarios encontrados
        return lista;
    }


    //    Metodo estático para buscar un usuario solo por su nombre de usuario.
//    No devuelve cliente asociado.
    public static Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Solo crea el usuario, sin cliente
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        null
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
        }

        // Si no encuentra nada devuelve null
        return null;
    }

    //    Metodo para buscar un usuario por nombre y contraseña.
//    Devuelve usuario con cliente asociado si existe.
    public Usuario buscarPorNombreYContrasena(String nombreUsuario, String contraseña) throws SQLException {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.contraseña, u.rol, " +
                "c.id_cliente, c.nombre AS nombre_cliente, c.edad, c.email, c.telefono " +
                "FROM usuarios u LEFT JOIN clientes c ON u.id_cliente = c.id_cliente " +
                "WHERE u.nombre_usuario = ? AND u.contraseña = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contraseña);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = null;
                    if (rs.getInt("id_cliente") != 0) {
                        // Creamos cliente solo si existe
                        cliente = new Cliente(
                                rs.getInt("id_cliente"),
                                rs.getString("nombre_cliente"),
                                rs.getInt("edad"),
                                rs.getString("email"),
                                rs.getString("telefono")
                        );
                    }

                    // Creamos y devolvemos usuario con cliente (si tiene)
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            rs.getString("contraseña"),
                            rs.getString("rol"),
                            cliente
                    );
                } else {
                    // No encontrado
                    return null;
                }
            }
        }
    }
}
