package com.example.sebas_pc.resuelvelo.model;

public class Departamentos {
    public String displayNameDept;
    public String displayName;
    public String correo;
    public String userId;



    public Departamentos(String userId, String displayNameDept, String displayName, String correo) {
        this.displayNameDept = displayNameDept;
        this.displayName = displayName;
        this.correo = correo;
        this.userId = userId;
    }

    public Departamentos(){}
}
