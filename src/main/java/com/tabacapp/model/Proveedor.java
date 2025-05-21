package com.tabacapp.model;

/**
 * Clase que representa un proveedor en el sistema TabacApp.
 */
public class Proveedor {

    //    Atributos
    private Integer id;
    private String nombre;
    private String telefono;
    private String email;

    //    Cosntructores
    public Proveedor() {
    }

    public Proveedor(Integer id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    //    Getters y Setters
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //    toString
    @Override
    public String toString() {
        return "🏭 " + nombre + " | 📧 " + email + " | ☎ " + telefono;
    }
}
