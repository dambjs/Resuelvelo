package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.sebas_pc.resuelvelo.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class PerfilEmpresario extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private FirebaseAuth firebaseAuth;

    TextView nom;
    TextView correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresario);


        correo = (TextView)findViewById(R.id.email);
        nom = (TextView)findViewById(R.id.nombre);

    }


    public void salir(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(PerfilEmpresario.this, Logueo.class));
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PerfilEmpresario.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

//    @Override
//    public void onDataChange(DataSnapshot snapshot) {
//        for (DataSnapshot chatSnapshot: snapshot.getChildren()) {
//            String key = chatSnapshot.getKey();
//            System.out.println(key);
//        }
//    }

}