package com.example.sebas_pc.resuelvelo.model;

public class Empresa {
    public String userId;
    public String displayNameEmpresa;
    public String date;
    public String descripcionEmpresa;
    public String photoEmpresaUrl;


    public Empresa(String userId, String displayNameEmpresa, String date, String descripcionEmpresa, String photoEmpresaUrl) {
        this.userId = userId;
        this.displayNameEmpresa = displayNameEmpresa;
        this.date = date;
        this.descripcionEmpresa = descripcionEmpresa;
        this.photoEmpresaUrl = photoEmpresaUrl;
    }

    public Empresa(){}
}
