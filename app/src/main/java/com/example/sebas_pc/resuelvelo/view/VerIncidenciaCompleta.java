package com.example.sebas_pc.resuelvelo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerIncidenciaCompleta extends AppCompatActivity {

    String idEmpresa, uid, idPrueba;
    private DatabaseReference mDatabase,mDatabase2,mDatabase3;

    private ImageView imagen;
    private TextView prioridad;
    private TextView departamento;
    private TextView destinatario;
    private TextView motivo;
    private TextView otros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_incidencia_completa);

        uid = FirebaseAuth.getInstance().getUid();

        idPrueba = getIntent().getStringExtra("PRUEBA_KEY");



//        mDatabase = FirebaseDatabase.getInstance().getReference();

        imagen = findViewById(R.id.imagen);
        prioridad = findViewById(R.id.prioridad);
        departamento = findViewById(R.id.departamento);
        destinatario = findViewById(R.id.destinatario);
        motivo = findViewById(R.id.motivo);
        otros = findViewById(R.id.otros);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("incidencia/alta").child(uid).child(idPrueba).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                if (incidencia != null) {
                    prioridad.setText(incidencia.prioridad);
                    departamento.setText(incidencia.departamento);
                    destinatario.setText(incidencia.destinatario);
                    motivo.setText(incidencia.motivo);
                    otros.setText(incidencia.otros);
                    Glide.with(VerIncidenciaCompleta.this)
                            .load(incidencia.imagenIncidencia)
                            .into(imagen);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mDatabase.child("incidencia/media").child(uid).child(idEmpresa).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
//                if (incidencia != null) {
//                    prioridad.setText(incidencia.prioridad);
//                    departamento.setText(incidencia.departamento);
//                    destinatario.setText(incidencia.destinatario);
//                    motivo.setText(incidencia.motivo);
//                    otros.setText(incidencia.otros);
//                    Glide.with(VerIncidenciaCompleta.this)
//                            .load(incidencia.imagenIncidencia)
//                            .into(imagen);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mDatabase.child("incidencia/baja").child(uid).child(idEmpresa).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
//                if (incidencia != null) {
//                    prioridad.setText(incidencia.prioridad);
//                    departamento.setText(incidencia.departamento);
//                    destinatario.setText(incidencia.destinatario);
//                    motivo.setText(incidencia.motivo);
//                    otros.setText(incidencia.otros);
//                    Glide.with(VerIncidenciaCompleta.this)
//                            .load(incidencia.imagenIncidencia)
//                            .into(imagen);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
}