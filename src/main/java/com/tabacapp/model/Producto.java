package com.tabacapp.model;

import java.time.LocalDate;

public class Producto {
    //    Atributos
    private Integer id;
    private String nombre;
    private String marca;
    private String tipo;
    private Double precio;
    private Integer stock;
    private LocalDate fechaAlta;
    private Integer idProveedor;

    //    Constructores
    public Producto() {
    }

    public Producto(Integer id, String nombre, String marca, String tipo, Double precio, Integer stock, LocalDate fechaAlta, Integer idProveedor) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
        this.fechaAlta = fechaAlta;
        this.idProveedor = idProveedor;
    }

    // Getters y Setters
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

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }
}
