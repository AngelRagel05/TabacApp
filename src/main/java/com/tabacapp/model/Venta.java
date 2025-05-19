package com.tabacapp.model;

import java.time.LocalDate;

public class Venta {

//    Atributos
    private Integer id;
    private Integer idCliente;
    private Integer idProducto;
    private LocalDate fecha;
    private Integer cantidad;
    private Double total;

//    Constructores
    public Venta() {
    }

    public Venta(Integer id, Integer idCliente, Integer idProducto, LocalDate fecha, Integer cantidad, Double total) {
        this.id = id;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.total = total;
    }

//    Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
