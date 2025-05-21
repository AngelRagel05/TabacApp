package com.tabacapp.db;

import com.tabacapp.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    // Metodo para obtener todas las ventas junto con los nombres de cliente y producto
    public static List<Venta> obtenerTodas() {
        List<Venta> lista = new ArrayList<>();

        // Consulta que une ventas con clientes y productos para obtener info completa
        String sql = "SELECT v.*, c.nombre as nombre_cliente, p.nombre as nombre_producto " +
                "FROM ventas v " +
                "JOIN clientes c ON v.id_cliente = c.id_cliente " +
                "JOIN productos p ON v.id_producto = p.id_producto";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Por cada fila, crear Cliente y Producto con sus datos básicos
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre_cliente"));

                Producto producto = new Producto();
                producto.setId(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre_producto"));

                // Crear objeto Venta con todos los datos y añadir a la lista
                Venta venta = new Venta(
                        rs.getInt("id_venta"),
                        cliente,
                        producto,
                        rs.getDate("fecha"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total")
                );
                lista.add(venta);
            }

        } catch (SQLException e) {
            // Mostrar error si algo falla
            System.err.println("❌ Error al obtener ventas: " + e.getMessage());
        }

        // Devolver la lista con todas las ventas encontradas
        return lista;
    }

    // Metodo para registrar una nueva venta en la base de datos
    public static boolean registrarVenta(Venta v) {
        // Consulta SQL con placeholders para insertar una venta
        String sql = "INSERT INTO ventas (id_cliente, id_producto, fecha, cantidad, total) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Rellenar los placeholders con los datos de la venta
            pstmt.setInt(1, v.getCliente().getId());
            pstmt.setInt(2, v.getProducto().getId());
            pstmt.setDate(3, new java.sql.Date(v.getFecha().getTime()));
            pstmt.setInt(4, v.getCantidad());
            pstmt.setDouble(5, v.getTotal());

            // Ejecutar inserción
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Mostrar error y devolver false si falla
            System.err.println("❌ Error al registrar venta: " + e.getMessage());
            return false;
        }
    }
}
