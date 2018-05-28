package com.example.sebas_pc.resuelvelo.model;

public class Incidencia {
    public String userId;
    public String departamento;
    public String destinatario;
    public String motivo;
    public String motivo2;
    public String prioridad;
    public String imagenIncidencia;

    public Incidencia(String userId, String departamento, String destinatario, String motivo, String motivo2, String prioridad, String imagenIncidencia) {
        this.userId = userId;
        this.departamento = departamento;
        this.destinatario = destinatario;
        this.motivo = motivo;
        this.motivo2 = motivo2;
        this.prioridad = prioridad;
        this.imagenIncidencia = imagenIncidencia;
    }
    public Incidencia(){}
}
