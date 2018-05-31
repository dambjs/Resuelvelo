package com.example.sebas_pc.resuelvelo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegistroEmpleado extends AppCompatActivity implements View.OnClickListener {

    private EditText etDisplayName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword2;
    private Button btnRegistrarse;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView tvEntrar;
    private TextView dominio;
    private Spinner areaSpinner;
//    final HashMap<String,String> empresasKey = new HashMap<>();

    private ProgressDialog progressDialog;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empleado);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), PerfilEmpleado.class));
        }

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    System.out.println("");
                }
            }
        };

        etDisplayName = (EditText) findViewById(R.id.displayName);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword2 = (EditText) findViewById(R.id.password2);
        etPassword = (EditText) findViewById(R.id.password);
        tvEntrar = (TextView) findViewById(R.id.entrar);
        btnRegistrarse = (Button) findViewById(R.id.registrarse);
        dominio = (TextView) findViewById(R.id.dominio); // ok



        progressDialog = new ProgressDialog(this);

        btnRegistrarse.setOnClickListener(this);
        tvEntrar.setOnClickListener(this);

        mDatabase.child("empresa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> empresas = new ArrayList<String>();


                for (DataSnapshot empresario: dataSnapshot.getChildren()) {
                    for(DataSnapshot empresa: empresario.getChildren()){
//                        String empresaKey = empresa.getKey();
                        String nombreempresa = empresa.child("displayNameEmpresa").getValue(String.class);
                        empresas.add(nombreempresa);
//                        empresasKey.put(nombreempresa, empresaKey);
                    }
                }

                areaSpinner = (Spinner) findViewById(R.id.sp5);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(RegistroEmpleado.this, android.R.layout.simple_spinner_item, empresas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void registerUser(){

        final String displayName = etDisplayName.getText().toString().trim();

        String email = etEmail.getText()+dominio.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();

        //String nombreEmpresa = spinner.getValue();

        //String empresaKey = empresasKey.get(nombreEmpresa);

        if(TextUtils.isEmpty(displayName)){
            Toast.makeText(this,"Porfavor ponga un nombre de empresa", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Porfavor ponga un bordes", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Porfavor ponga una Password", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(this,"Porfavor introduzca una ePassword de más de 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(password2)){
            Toast.makeText(this,"Porfavor introduzca una Password identica", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registrando, espere por favor...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                            finish();
                            startActivity(new Intent(getApplicationContext(), PerfilEmpleado.class));
                        }else{
                            Toast.makeText(RegistroEmpleado.this,"Registro Erroneo", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String email = etEmail.getText().toString();
        String nombre = etDisplayName.getText().toString();
        mDatabase.child("empleado/users").child(user.getUid()).setValue(new User(user.getUid(), nombre, email+"@resuelvelo.es"));

        startActivity(new Intent(RegistroEmpleado.this, PerfilEmpleado.class));
        finish();
    }

    @Override
    public void onClick(View view) {

        if(view == btnRegistrarse){
            registerUser();
        }

        if(view == tvEntrar){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LogueoEmpleado.class));
        }
    }
}