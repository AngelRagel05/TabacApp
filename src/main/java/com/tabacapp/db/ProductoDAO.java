package com.tabacapp.db;

import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private Connection conn;  // Guarda la conexión a la base de datos para hacer consultas y actualizaciones

    public ProductoDAO(Connection conn) {
        this.conn = conn;  // Recibe la conexión desde fuera para usarla aquí
    }

    /**
     * Devuelve una lista con todos los productos de la base de datos.
     * Trae también la información del proveedor si existe.
     */
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        // Consulta SQL para obtener productos junto con su proveedor (si tiene)
        String sql = "SELECT p.id_producto, p.nombre, p.marca, p.tipo, p.precio, p.stock, p.fecha_alta, " +
                "pr.id_proveedor, pr.nombre AS nombre_proveedor " +
                "FROM productos p LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor";

        // Preparamos y ejecutamos la consulta
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Por cada fila que devuelve la consulta, convertimos a objeto Producto y lo añadimos a la lista
            while (rs.next()) {
                Producto p = mapearProducto(rs);
                productos.add(p);
            }
        }

        return productos;
    }

    /**
     * Busca productos filtrando por varios criterios:
     * nombre, marca, rango de precio mínimo y máximo, y nombre del proveedor.
     * Los parámetros pueden ser nulos o vacíos para ignorar ese filtro.
     */
    public List<Producto> buscar(String nombre, String marca, Double precioMin, Double precioMax, String nombreProveedor) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        // Construimos la consulta base, con LEFT JOIN para incluir productos sin proveedor también
        StringBuilder sql = new StringBuilder(
                "SELECT p.id_producto, p.nombre, p.marca, p.tipo, p.precio, p.stock, p.fecha_alta, " +
                        "pr.id_proveedor, pr.nombre AS nombre_proveedor " +
                        "FROM productos p LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor WHERE 1=1 ");

        // Añadimos filtros solo si los parámetros están definidos y no vacíos
        if (nombre != null && !nombre.isEmpty()) sql.append("AND p.nombre LIKE ? ");
        if (marca != null && !marca.isEmpty()) sql.append("AND p.marca LIKE ? ");
        if (precioMin != null) sql.append("AND p.precio >= ? ");
        if (precioMax != null) sql.append("AND p.precio <= ? ");
        if (nombreProveedor != null && !nombreProveedor.isEmpty()) sql.append("AND pr.nombre LIKE ? ");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            // Asignamos los valores a los parámetros de la consulta, en orden
            if (nombre != null && !nombre.isEmpty()) ps.setString(idx++, "%" + nombre + "%");
            if (marca != null && !marca.isEmpty()) ps.setString(idx++, "%" + marca + "%");
            if (precioMin != null) ps.setDouble(idx++, precioMin);
            if (precioMax != null) ps.setDouble(idx++, precioMax);
            if (nombreProveedor != null && !nombreProveedor.isEmpty()) ps.setString(idx++, "%" + nombreProveedor + "%");

            // Ejecutamos y leemos los resultados
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        }

        return productos;
    }

    /**
     * Inserta un producto nuevo en la base de datos.
     * Si el proveedor indicado no existe, lo crea y usa su ID.
     * Devuelve true si se insertó bien, false si no.
     */
    public boolean insertar(Producto p) throws SQLException {
        Integer idProveedor = null;

        // Si el producto tiene proveedor, buscamos o creamos su ID
        if (p.getProveedor() != null && p.getProveedor().getNombre() != null && !p.getProveedor().getNombre().isEmpty()) {
            idProveedor = getOrCreateProveedor(p.getProveedor().getNombre());
            p.getProveedor().setId(idProveedor);  // Guardamos el ID en el objeto para sincronizar
        }

        // Sentencia SQL para insertar el producto con todos sus datos
        String sql = "INSERT INTO productos (nombre, marca, tipo, precio, stock, fecha_alta, id_proveedor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getTipo());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setDate(6, new java.sql.Date(p.getFechaAlta().getTime()));

            // Si no hay proveedor, ponemos NULL en la base de datos
            if (idProveedor != null) ps.setInt(7, idProveedor);
            else ps.setNull(7, Types.INTEGER);

            // Ejecutamos la inserción y devolvemos si fue exitosa
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    /**
     * Busca un proveedor por nombre.
     * Si no existe, lo crea.
     * Devuelve el ID del proveedor encontrado o creado.
     */
    private Integer getOrCreateProveedor(String nombreProveedor) throws SQLException {
        // Primero intentamos buscar el proveedor
        String selectSql = "SELECT id_proveedor FROM proveedores WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, nombreProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_proveedor");  // Si existe, devolvemos el ID
                }
            }
        }

        // Si no existe, insertamos uno nuevo
        String insertSql = "INSERT INTO proveedores (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreProveedor);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new SQLException("Error insertando proveedor");

            // Obtenemos la clave generada (ID) del nuevo proveedor
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener id del proveedor insertado");
                }
            }
        }
    }

    /**
     * Elimina un producto por su ID.
     * Devuelve true si se eliminó correctamente.
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    /**
     * Calcula el beneficio total sumando el precio por el stock de todos los productos.
     * Devuelve 0 si no hay productos o error.
     */
    public double calcularBeneficioTotal() throws SQLException {
        String sql = "SELECT SUM(precio * stock) AS beneficio FROM productos";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("beneficio");
            }
        }

        return 0.0;
    }

    /**
     * Metodo interno que convierte una fila del ResultSet en un objeto Producto.
     * También crea el objeto Proveedor si está presente.
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();

        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setMarca(rs.getString("marca"));
        p.setTipo(rs.getString("tipo"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setFechaAlta(rs.getDate("fecha_alta"));

        int idProveedor = rs.getInt("id_proveedor");
        String nombreProveedor = rs.getString("nombre_proveedor");

        // Si el producto tiene proveedor, lo crea y lo asigna
        if (idProveedor > 0) {
            Proveedor proveedor = new Proveedor();
            proveedor.setId(idProveedor);
            proveedor.setNombre(nombreProveedor);
            p.setProveedor(proveedor);
        } else {
            p.setProveedor(null);
        }

        return p;
    }
}