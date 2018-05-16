package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Departamentos;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilEmpresa extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView nombreDept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresa);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombreDept = findViewById(R.id.nombreDept);

        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("departamentos").child(uid);
        prueba();
    }

    public void add(View view) {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nombre de Departamento")
                .setMessage("¿Que nombre quieres darle al departamento?")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final FirebaseUser departamentos = FirebaseAuth.getInstance().getCurrentUser();

                        final String nombre = taskEditText.getText().toString();
                        mDatabase.push().setValue(new Departamentos(departamentos.getUid(), nombre));

                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
        dialog.show();
    }

    void prueba(){

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Departamentos dept = noteDataSnapshot.getValue(Departamentos.class);
                    if (dept != null) {
                        nombreDept.setText(dept.displayNameDept);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }


}
