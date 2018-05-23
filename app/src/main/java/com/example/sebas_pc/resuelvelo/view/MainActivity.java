package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sebas_pc.resuelvelo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void empresario(View view) {
        Intent intent = new Intent(this, LogueoEmpresario.class);
        startActivity(intent);
    }

    public void empleado(View view) {
        Intent intent = new Intent(this, LogueoEmpleado.class);
        startActivity(intent);
    }
}
