package com.tabacapp.db;

import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Metodo para obtener y listar todos los productos junto con su proveedor
    public static List<Producto> obtenerTodos() {
        // Lista donde se guardarán todos los productos obtenidos de la base de datos
        List<Producto> lista = new ArrayList<>();

        // Consulta SQL que hace un JOIN entre productos y proveedores para obtener toda la info necesaria
        String sql = "SELECT * FROM productos p JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor";

        // Intentamos conectar a la base de datos, crear la sentencia y ejecutar la consulta
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Mientras haya resultados en la consulta
            while (rs.next()) {
                // Crear un objeto Proveedor con los datos del proveedor del producto actual
                Proveedor proveedor = new Proveedor(
                        rs.getInt("pr.id_proveedor"),
                        rs.getString("pr.nombre"),
                        rs.getString("pr.telefono"),
                        rs.getString("pr.email")
                );

                // Crear un objeto Producto con los datos obtenidos y el proveedor creado arriba
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

                // Añadir el producto a la lista que se devolverá
                lista.add(p);
            }

        } catch (SQLException e) {
            // Si hay error al consultar, mostrar mensaje de error
            System.err.println("❌ Error al obtener productos: " + e.getMessage());
        }

        // Devolver la lista con todos los productos encontrados (puede estar vacía si no hay datos o error)
        return lista;
    }

    // Metodo para agregar un nuevo producto a la base de datos
    public static boolean agregar(Producto p) {
        // Consulta SQL para insertar un nuevo producto con sus datos, usando placeholders (?) para evitar SQL injection
        String sql = "INSERT INTO productos (nombre, marca, tipo, precio, stock, fecha_alta, id_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Rellenar los placeholders con los valores del objeto Producto recibido
            pstmt.setString(1, p.getNombre());
            pstmt.setString(2, p.getMarca());
            pstmt.setString(3, p.getTipo());
            pstmt.setDouble(4, p.getPrecio());
            pstmt.setInt(5, p.getStock());
            // Convertir java.util.Date a java.sql.Date para la fecha
            pstmt.setDate(6, new java.sql.Date(p.getFechaAlta().getTime()));
            pstmt.setInt(7, p.getProveedor().getId());

            // Ejecutar la inserción en la base de datos
            pstmt.executeUpdate();

            // Si va bien, devolver true indicando que se agregó correctamente
            return true;

        } catch (SQLException e) {
            // En caso de error, mostrar mensaje y devolver false
            System.err.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    // Metodo para eliminar un producto de la base de datos por su ID
    public static boolean eliminar(int id) {
        // Consulta SQL para borrar el producto que tenga el id_producto igual al pasado como parámetro
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Rellenar el placeholder con el ID del producto a eliminar
            pstmt.setInt(1, id);

            // Ejecutar el borrado y devolver true si se eliminó al menos una fila (producto)
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // En caso de error, mostrar mensaje y devolver false
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

}
