package com.example.sebas_pc.resuelvelo.model;

public class Departamentos {
    public String displayNameDept;
    public String displayName;
    public String userId;



    public Departamentos(String userId, String displayNameDept, String displayName) {
        this.displayNameDept = displayNameDept;
        this.displayName = displayName;
        this.userId = userId;
    }

    public Departamentos(){}
}
