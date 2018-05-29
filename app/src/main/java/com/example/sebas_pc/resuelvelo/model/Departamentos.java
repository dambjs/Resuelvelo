package com.example.sebas_pc.resuelvelo.model;

public class Departamentos {
    public String displayName;
    public String correo;
    public String userId;



    public Departamentos(String userId, String displayName, String correo) {
        this.displayName = displayName;
        this.correo = correo;
        this.userId = userId;
    }

    public Departamentos(){}
}
