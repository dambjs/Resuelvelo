package com.example.sebas_pc.resuelvelo.model;

public class Empresa {
    public String userId;
    public String displayNameEmpresa;
    public String date;
    public String photoEmpresaUrl;

    public Empresa(String userId, String displayNameEmpresa, String date, String photoEmpresaUrl) {
        this.userId = userId;
        this.displayNameEmpresa = displayNameEmpresa;
        this.date = date;
        this.photoEmpresaUrl = photoEmpresaUrl;
    }

    public Empresa(){}

}
