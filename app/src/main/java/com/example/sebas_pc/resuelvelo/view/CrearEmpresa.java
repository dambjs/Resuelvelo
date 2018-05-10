package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CrearEmpresa extends AppCompatActivity implements View.OnClickListener {

    private EditText nombreEmp;
    private EditText fechaEmp;
    private EditText descEmp;
    private ImageView add;
    private ImageView image;
    private Button registrarse;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empresa);

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        nombreEmp = (EditText) findViewById(R.id.nombreEmp);
        fechaEmp = (EditText) findViewById(R.id.fechaEmp);
        registrarse = (Button) findViewById(R.id.registrarse);
        registrarse.setOnClickListener(this);

    }


        private void creaEmpresa() {

            final FirebaseUser empresa = FirebaseAuth.getInstance().getCurrentUser();

            String nombre = nombreEmp.getText().toString();
            String fecha = fechaEmp.getText().toString();
            mDatabase.child("empresa").child(empresa.getUid()).setValue(new Empresa(empresa.getUid(), nombre, fecha));
            finish();
    }


    @Override
    public void onClick(View view) {

        if(view == registrarse){
            creaEmpresa();
            startActivity(new Intent(this, PerfilEmpresario.class));
        }


    }
}
