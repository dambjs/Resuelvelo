package com.example.sebas_pc.resuelvelo.model;

public class UsersG {

    public String userId;
    public String nombre;
    public String email;

    public UsersG(String nombre, String email) {}

    public UsersG(String userId, String nombre, String email){
        this.userId = userId;
        this.nombre = nombre;
        this.email = email;
    }
}