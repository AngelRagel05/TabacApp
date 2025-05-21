package com.tabacapp.db;

import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos p JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                        rs.getInt("pr.id_proveedor"),
                        rs.getString("pr.nombre"),
                        rs.getString("pr.telefono"),
                        rs.getString("pr.email")
                );

                Producto p = new Producto(
                        rs.getInt("p.id_producto"),
                        rs.getString("p.nombre"),
                        rs.getString("p.marca"),
                        rs.getString("p.tipo"),
                        rs.getDouble("p.precio"),
                        rs.getInt("p.stock"),
                        rs.getDate("p.fecha_alta"),
                        proveedor
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener productos: " + e.getMessage());
        }

        return lista;
    }

    public static boolean agregar(Producto p) {
        String sql = "INSERT INTO productos (nombre, marca, tipo, precio, stock, fecha_alta, id_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getMarca());
            pstmt.setString(3, p.getTipo());
            pstmt.setDouble(4, p.getPrecio());
            pstmt.setInt(5, p.getStock());
            pstmt.setDate(6, new java.sql.Date(p.getFechaAlta().getTime()));
            pstmt.setInt(7, p.getProveedor().getId());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
}
