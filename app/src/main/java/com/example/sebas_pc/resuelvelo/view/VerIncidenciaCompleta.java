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

    String idIncidencia, idEmpresa, uid;
    private DatabaseReference mDatabase,mDatabase2,mDatabase3;

    private ImageView imagen;
    private TextView prioridad;
    private TextView departamento;
    private TextView destinatario;
    private TextView motivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_incidencia_completa);

        uid = FirebaseAuth.getInstance().getUid();

        idIncidencia = getIntent().getStringExtra("INCIDENCIA_KEY");
        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");



//        mDatabase = FirebaseDatabase.getInstance().getReference();

        imagen = findViewById(R.id.imagen);
        prioridad = findViewById(R.id.prioridad);
        departamento = findViewById(R.id.departamento);
        destinatario = findViewById(R.id.destinatario);
        motivo = findViewById(R.id.motivo);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("incidencia").child("alta").child(idIncidencia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                if (incidencia != null) {
                    prioridad.setText(incidencia.prioridad);
                    departamento.setText(incidencia.departamento);
                    destinatario.setText(incidencia.destinatario);
                    motivo.setText(incidencia.motivo);
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