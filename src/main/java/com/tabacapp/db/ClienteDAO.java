package com.tabacapp.db;

import com.tabacapp.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

//    Metodo para listas los Clientes
    public static List<Cliente> obtenerTodos() {

//        Creo la lista
        List<Cliente> lista = new ArrayList<>();
//        Hago la consulta
        String sql = "SELECT * FROM clientes";

//        Concecto la bbdd y saco el resultado
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

//            Mientras siga habiendo siguiente me crea un cliente y lo añade en la lista
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

//    Metodo para agregar Cliente
    public static boolean agregar(Cliente cliente) {
//        Consulta sql
        String sql = "INSERT INTO clientes (nombre, edad, email, telefono) VALUES (?, ?, ?, ?)";

//        Conexion a BBDD
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

//            Asignamos los valores
            pstmt.setString(1, cliente.getNombre());
            pstmt.setInt(2, cliente.getEdad());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getTelefono());

//            Ejecuto la sentencia
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

//    Metodo para eliminar Cliente
    public static boolean eliminar(int id) {
//        Consulta sql
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

//        Conexion BBDD
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

//            Asigno valores
            pstmt.setInt(1, id);

//            Ejecuto la sentencia
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
