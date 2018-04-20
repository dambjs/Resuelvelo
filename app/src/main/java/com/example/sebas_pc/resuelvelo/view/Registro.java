package com.example.sebas_pc.resuelvelo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.UsersE;
import com.example.sebas_pc.resuelvelo.model.UsersG;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText nombre;
    private EditText email;
    private EditText contraseña;
    private EditText confirmarContra;
    private Button registrarse;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView entre;

    private ProgressDialog progressDialog;

    //
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    //

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), PerfilEmpresario.class));
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

        //initializing views
        nombre = (EditText) findViewById(R.id.nombre);
        email = (EditText) findViewById(R.id.email);
        confirmarContra = (EditText) findViewById(R.id.confirmarContra);
        contraseña = (EditText) findViewById(R.id.contraseña);
        entre = (TextView) findViewById(R.id.entre);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registrarse = (Button) findViewById(R.id.registrarse);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        registrarse.setOnClickListener(this);
        entre.setOnClickListener(this);

    }




    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void registerUser(){

        //getting bordes and password from edit texts
        final String nombreP = nombre.getText().toString().trim();
        String emailP = email.getText().toString().trim();
        String confPass = confirmarContra.getText().toString().trim();
        String password  = contraseña.getText().toString().trim();

//        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference userNameRef =  myRootRef.child("users/nombre");
//        userNameRef.setValue(nombreP);


        if(TextUtils.isEmpty(nombreP)){
            Toast.makeText(this,"Porfavor ponga un nombre", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(emailP)){
            Toast.makeText(this,"Porfavor ponga un bordes", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Porfavor ponga una contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(this,"Porfavor introduzca una contraseña de más de 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(confPass)){
            Toast.makeText(this,"Porfavor introduzca una contraseña identica", Toast.LENGTH_LONG).show();
            return;
        }

        //if the bordes and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registrando, espere por favor...");
        progressDialog.show();




        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(emailP, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                            finish();
                            startActivity(new Intent(getApplicationContext(), PerfilEmpresario.class));
                        }else{
                            //display some message here
                            Toast.makeText(Registro.this,"Registro Erroneo", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }

                });

    }

    private void onAuthSuccess(FirebaseUser usersE) {

        String correo = email.getText().toString();

        // Write new user
        writeNewUser(usersE.getUid(),correo);

        // Go to MainActivity
        startActivity(new Intent(Registro.this, PerfilEmpresario.class));
        finish();
    }

    private void writeNewUser(String id, String correo) {
        String nom = nombre.getText().toString();

        UsersE usersE = new UsersE(id,nom,correo);

        mDatabase.child("usersEmail").child(id).setValue(usersE);
    }

    @Override
    public void onClick(View view) {

        if(view == registrarse){
            registerUser();
        }

        if(view == entre){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, Logueo.class));
        }
    }

}