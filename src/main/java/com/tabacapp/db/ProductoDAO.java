package com.tabacapp.db;

import com.tabacapp.model.Producto;
import com.tabacapp.model.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    // Listar todos los productos
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.marca, p.tipo, p.precio, p.stock, p.fecha_alta, " +
                "pr.id_proveedor, pr.nombre AS nombre_proveedor " +
                "FROM productos p LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = mapearProducto(rs);
                productos.add(p);
            }
        }

        return productos;
    }

    // Buscar productos por nombre, marca, precio (rangos)
    public List<Producto> buscar(String nombre, String marca, Double precioMin, Double precioMax, String nombreProveedor) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id_producto, p.nombre, p.marca, p.tipo, p.precio, p.stock, p.fecha_alta, " +
                        "pr.id_proveedor, pr.nombre AS nombre_proveedor " +
                        "FROM productos p LEFT JOIN proveedores pr ON p.id_proveedor = pr.id_proveedor WHERE 1=1 ");

        if (nombre != null && !nombre.isEmpty()) {
            sql.append("AND p.nombre LIKE ? ");
        }
        if (marca != null && !marca.isEmpty()) {
            sql.append("AND p.marca LIKE ? ");
        }
        if (precioMin != null) {
            sql.append("AND p.precio >= ? ");
        }
        if (precioMax != null) {
            sql.append("AND p.precio <= ? ");
        }
        if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
            sql.append("AND pr.nombre LIKE ? ");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (nombre != null && !nombre.isEmpty()) {
                ps.setString(idx++, "%" + nombre + "%");
            }
            if (marca != null && !marca.isEmpty()) {
                ps.setString(idx++, "%" + marca + "%");
            }
            if (precioMin != null) {
                ps.setDouble(idx++, precioMin);
            }
            if (precioMax != null) {
                ps.setDouble(idx++, precioMax);
            }
            if (nombreProveedor != null && !nombreProveedor.isEmpty()) {
                ps.setString(idx++, "%" + nombreProveedor + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        }

        return productos;
    }

    // Insertar un producto nuevo (con control de proveedor)
    public boolean insertar(Producto p) throws SQLException {
        // Primero obtener o crear proveedor y tener su id
        Integer idProveedor = null;
        if (p.getProveedor() != null && p.getProveedor().getNombre() != null && !p.getProveedor().getNombre().isEmpty()) {
            idProveedor = getOrCreateProveedor(p.getProveedor().getNombre());
            p.getProveedor().setId(idProveedor); // Seteamos id en el objeto
        }

        String sql = "INSERT INTO productos (nombre, marca, tipo, precio, stock, fecha_alta, id_proveedor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getTipo());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setDate(6, new java.sql.Date(p.getFechaAlta().getTime()));

            if (idProveedor != null) {
                ps.setInt(7, idProveedor);
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // Método para buscar o crear proveedor y devolver su id
    private Integer getOrCreateProveedor(String nombreProveedor) throws SQLException {
        // Buscar proveedor
        String selectSql = "SELECT id_proveedor FROM proveedores WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, nombreProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_proveedor");
                }
            }
        }

        // Si no existe, crear proveedor nuevo
        String insertSql = "INSERT INTO proveedores (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreProveedor);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("Error insertando proveedor");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el id del proveedor insertado");
                }
            }
        }
    }

    // Eliminar un producto por id
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    // Calcular beneficio total sumando todos los precios * stock
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

    // Método privado para mapear un ResultSet a Producto
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
