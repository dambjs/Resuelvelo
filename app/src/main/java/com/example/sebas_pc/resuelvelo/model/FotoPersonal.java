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


//        1- Cuando se añade un departamento poder poner más de una persona ---------------------------------------------------- HECHO
//        2- Cada incidencia creada ha de ser por empresa. ---------------------------------------------------------------------
//        3- Al darle a otros en crear incidencia ha de salir un Edit text. ---------------------------------------------------- HECHO
//        4- El ver incidencia  al clicarle se ha de ver extensa. -------------------------------------------------------------- HECHO
//        5- Spinner registrar empleado y que se muestre bien los datos.-------------------------------------------------------- medio HECHO
//        6- Eliminar empresas pulsando. --------------------------------------------------------------------------------------- HECHO, mini fallo
//        7- Perfil empresario mostrar las incidencias según su prioridad.------------------------------------------------------ medio
//        8- Imagen de perfil empresarios.--------------------------------------------------------------------------------------
//        9- Crear el has olvidado contraseña.---------------------------------------------------------------------------------- HECHO