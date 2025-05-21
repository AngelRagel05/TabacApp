package com.tabacapp.model;

/**
 * Clase que representa un usuario del sistema.
 */
public class Usuario {

    //    Atributos
    private Integer id;
    private String nombreUsuario;
    private String contraseña;
    private String rol;
    private Cliente cliente;

    //    Constructores
    public Usuario() {
    }

    public Usuario(Integer id, String nombreUsuario, String contraseña, String rol, Cliente cliente) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.cliente = cliente;
    }

    //    Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    //    ToString
    @Override
    public String toString() {
        return "🔐 Usuario: " + nombreUsuario + " | Rol: " + rol + (cliente != null ? " | Cliente: " + cliente.getNombre() : " (Admin)");
    }
}
