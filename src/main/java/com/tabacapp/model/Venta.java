package com.tabacapp.model;

import java.util.Date;

/**
 * Clase que representa una venta de productos.
 */
public class Venta {

    //    Atributos
    private Integer id;
    private Cliente cliente;
    private Producto producto;
    private Date fecha;
    private Integer cantidad;
    private Double total;

    //    Constructores
    public Venta() {
    }

    public Venta(Integer id, Cliente cliente, Producto producto, Date fecha, Integer cantidad, Double total) {
        this.id = id;
        this.cliente = cliente;
        this.producto = producto;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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

    //    toString
    @Override
    public String toString() {
        return "🧾 Venta #" + id + " | " + cliente.getNombre() + " compró " + cantidad + " x " + producto.getNombre() + " | Total: 💵 " + total + " | 📅 " + fecha;
    }
}
