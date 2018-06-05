package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerIncidenciaEmpleado extends AppCompatActivity {

    String idIncidencia, uid;
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

        idIncidencia = getIntent().getStringExtra("INCIDENCIA_KEY");



//        mDatabase = FirebaseDatabase.getInstance().getReference();

        imagen = findViewById(R.id.imagen);
        prioridad = findViewById(R.id.prioridad);
        departamento = findViewById(R.id.departamento);
        destinatario = findViewById(R.id.destinatario);
        motivo = findViewById(R.id.motivo);
        otros = findViewById(R.id.otros);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("incidencia/alta").child(uid).child(idIncidencia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                if (incidencia != null) {
                    prioridad.setText(incidencia.prioridad);
                    departamento.setText(incidencia.departamento);
                    destinatario.setText(incidencia.destinatario);
                    motivo.setText(incidencia.motivo);
                    otros.setText(incidencia.otros);
                    Glide.with(VerIncidenciaEmpleado.this)
                            .load(incidencia.imagenIncidencia)
                            .into(imagen);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("incidencia/media").child(uid).child(idIncidencia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                if (incidencia != null) {
                    prioridad.setText(incidencia.prioridad);
                    departamento.setText(incidencia.departamento);
                    destinatario.setText(incidencia.destinatario);
                    motivo.setText(incidencia.motivo);
                    otros.setText(incidencia.otros);
                    Glide.with(VerIncidenciaEmpleado.this)
                            .load(incidencia.imagenIncidencia)
                            .into(imagen);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("incidencia/baja").child(uid).child(idIncidencia).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                if (incidencia != null) {
                    prioridad.setText(incidencia.prioridad);
                    departamento.setText(incidencia.departamento);
                    destinatario.setText(incidencia.destinatario);
                    motivo.setText(incidencia.motivo);
                    otros.setText(incidencia.otros);
                    Glide.with(VerIncidenciaEmpleado.this)
                            .load(incidencia.imagenIncidencia)
                            .into(imagen);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(VerIncidenciaEmpleado.this, MediaActivity2.class);
                intent.putExtra("INCIDENCIA_KEY", idIncidencia);
                startActivity(intent);
            }
        });

//        resuelta.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerIncidenciaEmpleado.this);
//                builder1.setMessage("¿Estas seguro que la incidencia ha sido resuelta?");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Si",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
////                                String postKey = getRef(position).getKey();
//
////                                mDatabase.setValue(mDatabase.child("Resueltas"));
//
//                                //mReference.child("posts/data").child(postkey).setValue(null);
//                                //Falta que no pete cuando se elimina el ultimo item.
//                            }
//                        });
//
//                builder1.setNegativeButton(
//                        "No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();
//            }
//        });





    }
}