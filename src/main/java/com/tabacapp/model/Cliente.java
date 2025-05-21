package com.tabacapp.model;

/**
 * Clase que representa un cliente de TabacApp.
 */
public class Cliente {

//    Atributos
    private Integer id;
    private String nombre;
    private Integer edad;
    private String email;
    private String telefono;

//    Constructores
    public Cliente() {
    }

    public Cliente(Integer id, String nombre, Integer edad, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.email = email;
        this.telefono = telefono;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

//    toString
    @Override
    public String toString() {
        return "👤 " + nombre + " | Edad: " + edad + " | 📧 " + email + " | 📞 " + telefono;
    }
}
