package com.example.sebas_pc.resuelvelo.model;

public class Incidencia {
    public String userId;
    public String departamento;
    public String destinatario;
    public String motivo;
    public String prioridad;
    public String otros;
    public String imagenIncidencia;



    public Incidencia(String userId, String departamento, String destinatario, String motivo, String prioridad , String otros, String imagenIncidencia) {
        this.userId = userId;
        this.departamento = departamento;
        this.destinatario = destinatario;
        this.motivo = motivo;
        this.prioridad = prioridad;
        this.otros = otros;
        this.imagenIncidencia = imagenIncidencia;
    }
    public Incidencia(){}
}
