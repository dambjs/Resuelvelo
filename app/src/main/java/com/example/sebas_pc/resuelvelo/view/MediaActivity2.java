package com.example.sebas_pc.resuelvelo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.FotoPersonal;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MediaActivity2 extends AppCompatActivity {

    private ImageView imagen;
    private DatabaseReference mDatabase;
    String idIncidencia, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media2);

        idIncidencia = getIntent().getStringExtra("INCIDENCIA_KEY");

        uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        imagen = findViewById(R.id.imagen);

        mDatabase.child("incidencia/alta").child(uid).child(idIncidencia).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);
                ImageView imageView = findViewById(R.id.imageview);
                Glide.with(MediaActivity2.this)
                        .load(incidencia.imagenIncidencia)
                        .into(imageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
