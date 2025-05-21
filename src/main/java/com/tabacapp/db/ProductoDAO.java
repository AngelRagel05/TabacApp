package com.tabacapp.dao;

// Importamos las clases necesarias
import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para manejar operaciones de base de datos
 * relacionadas con la tabla 'productos'.
 */
public class ProductoDAO {

    // Atributo para manejar la conexión a la base de datos
    private Connection conn;

//    Constructor que recibe una conexión ya establecida.
    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

//    Obtiene todos los productos junto con su proveedor desde la base de datos.
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        // Consulta que une productos con su proveedor
        String sql = "SELECT p.*, pr.nombre AS nombre_proveedor, pr.telefono, pr.email " +
                "FROM productos p JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor";

        // Se crea un Statement y se ejecuta la consulta
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iteramos por los resultados
            while (rs.next()) {
                // Creamos el objeto Proveedor con los datos del resultado
                Proveedor proveedor = new Proveedor(
                        rs.getInt("id_proveedor"),
                        rs.getString("nombre_proveedor"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );

                // Creamos el objeto Producto y lo asociamos con el proveedor
                Producto producto = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getDate("fecha_alta"),
                        proveedor
                );

                // Agregamos el producto a la lista
                productos.add(producto);
            }
        }

        // Devolvemos la lista de productos
        return productos;
    }


//     Inserta un nuevo producto en la base de datos.
    public void insertar(Producto producto) throws SQLException {
        // Consulta con parámetros (?) para insertar datos
        String sql = "INSERT INTO productos (nombre, marca, tipo, precio, stock, fecha_alta, id_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Usamos PreparedStatement para evitar inyecciones SQL
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Asignamos los valores al statement según el orden de los ?
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getMarca());
            stmt.setString(3, producto.getTipo());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getStock());

            // Convertimos java.util.Date a java.sql.Date
            stmt.setDate(6, new java.sql.Date(producto.getFechaAlta().getTime()));

            // ID del proveedor (relación foránea)
            stmt.setInt(7, producto.getProveedor().getId());

            // Ejecutamos la inserción
            stmt.executeUpdate();
        }
    }

//    Elimina un producto por su ID.
    public void eliminarPorId(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        // Ejecutamos la eliminación con el ID recibido
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
