package com.example.sebas_pc.resuelvelo.model;

public class UsersG {

    public String userId;
    public String nombre;
    public String email;

    public UsersG() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsersG(String userId, String nombre, String email){
        this.userId = userId;
        this.nombre = nombre;
        this.email = email;
    }
}