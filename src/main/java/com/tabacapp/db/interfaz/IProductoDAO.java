package com.tabacapp.db.interfaz;

import com.tabacapp.model.Producto;

import java.sql.SQLException;
import java.util.List;

public interface IProductoDAO {

    List<Producto> obtenerTodos() throws SQLException;

    List<Producto> buscar(String nombre, String marca, Double precioMin, Double precioMax, String nombreProveedor) throws SQLException;

    boolean insertar(Producto p) throws SQLException;

    boolean eliminar(int id) throws SQLException;

    double calcularBeneficioTotal() throws SQLException;
}