package com.example.sebas_pc.resuelvelo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sebas_pc.resuelvelo.R;

public class CrearIncidencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
