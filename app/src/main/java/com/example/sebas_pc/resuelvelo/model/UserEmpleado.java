package com.example.sebas_pc.resuelvelo.model;


public class UserEmpleado {
    public String userId;
    public String displayName;
    public String email;
    public String empresa;

    public UserEmpleado(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
    }

    public UserEmpleado(String userId, String displayName, String email, String empresa) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.empresa = empresa;
    }

    public UserEmpleado(){}


}
