package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.example.sebas_pc.resuelvelo.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CrearIncidencia extends AppCompatActivity {

//    private String dropDownItemArr[] = {"Prioridad Baja", "Prioridad Media", "Prioridad Alta"};
    private ImageView image;
    private ActionBar actionBar;
    private DatabaseReference mDatabase, mDatabase2;
    private final static String[] letra = { "Fallo de impresora", "Fallo de Sistema Operativo", "Falla el ordenador",
            "Pantalla sin señal", "Proyector estropeado", "Otros" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        String uid = FirebaseAuth.getInstance().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("departamentos").child(uid);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("usuariosM");
        image = findViewById(R.id.image);

        Spinner jaja = (Spinner) findViewById(R.id.sp3);

        jaja.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("displayNameDept").getValue(String.class);
                    areas.add(areaName);
                }

                Spinner areaSpinner = (Spinner) findViewById(R.id.sp);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CrearIncidencia.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                    String areaName = dataSnapshot.child("nombre").getValue(String.class);
                    areas.add(areaName);


                Spinner areaSpinner = (Spinner) findViewById(R.id.sp2);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CrearIncidencia.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}