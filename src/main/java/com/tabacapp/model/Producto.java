package com.tabacapp.model;

import java.util.Date;

/**
 * Clase que representa un producto.
 */
public class Producto {

//    Atributos
    private Integer id;
    private String nombre;
    private String marca;
    private String tipo;
    private Double precio;
    private Integer stock;
    private Date fechaAlta;
    private Proveedor proveedor;

//    Constructores
    public Producto() {
    }

    public Producto(Integer id, String nombre, String marca, String tipo, Double precio, Integer stock, Date fechaAlta, Proveedor proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
        this.fechaAlta = fechaAlta;
        this.proveedor = proveedor;
    }

//    Gettes y Settes
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

//    toString
    @Override
    public String toString() {
        return "🛒 " + nombre + " (" + tipo + " - " + marca + ") | 💲" + precio + " | Stock: " + stock;
    }
}
