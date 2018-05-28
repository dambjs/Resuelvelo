package com.example.sebas_pc.resuelvelo.model;

public class FotoPersonal {
    public String userId;
    public String imagenP;

    public FotoPersonal(String userId, String imagenP) {
        this.userId = userId;
        this.imagenP = imagenP;
    }

    public FotoPersonal(){}
}


//        1- Cuando se añade un departamento poder poner más de una persona
//        2- Cada incidencia creada ha de ser por empresa.
//        3- Al darle a otros en crear incidencia ha de salir un Edit text.
//        4- El ver incidencia ha de ser por empresa y ademas al clicarle se ha de ver extensa.
//        5- Spinner registrar empleado y que se muestre bien los datos.
//        6- Eliminar empresas pulsando. --> Falta hacer un for.
//        7- Perfil empresario mostrar las incidencias según su prioridad.
//        8- Imagen de perfil empresarios.