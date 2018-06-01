package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.FotoPersonal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MediaActivity extends AppCompatActivity {

    private ImageView imagenP2;
    private DatabaseReference mDatabase3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        imagenP2 = findViewById(R.id.imagenP2);

        final String uid = FirebaseAuth.getInstance().getUid();

        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("imagenPersonal").child(uid);

        imagenP2 = findViewById(R.id.imagenP2);
        mDatabase3.addValueEventListener(new ValueEventListener() {
             public void onDataChange(DataSnapshot dataSnapshot) {
                 FotoPersonal fotoPersonal = dataSnapshot.getValue(FotoPersonal.class);
                 ImageView imageView = findViewById(R.id.imageview);
                 Glide.with(MediaActivity.this)
                         .load(fotoPersonal.imagenP2)
                         .into(imageView);

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

    }
}
