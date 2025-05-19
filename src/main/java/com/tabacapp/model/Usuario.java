package com.tabacapp.model;

public class Usuario {

//    Atributos
    private Integer id;
    private String nombreUsuario;
    private String contraseña;
    private String rol;
    private Integer idCliente; // null si es admin

//    Constructores
    public Usuario() {
    }

    public Usuario(Integer id, String nombreUsuario, String contraseña, String rol, Integer idCliente) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.idCliente = idCliente;
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

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
}
