package com.example.sebas_pc.resuelvelo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.UsersG;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class Logueo extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;

    private Button entra; //ok
    private EditText email; // ok
    private EditText constraseña; // ok
    private TextView registrarte; //ok
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo);

        email = (EditText) findViewById(R.id.email); // ok
        constraseña = (EditText) findViewById(R.id.constraseña); //ok
        entra = (Button) findViewById(R.id.entra); // ok
        registrarte = (TextView) findViewById(R.id.registrarte); // ok

        findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        entra.setOnClickListener(this);
        registrarte.setOnClickListener(Logueo.this);
        comeIn();
    }

    void comeIn() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            final String userId = firebaseUser.getUid();

            FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .setValue(new UsersG(userId, firebaseUser.getDisplayName(), firebaseUser.getEmail()));

            startActivity(new Intent(this, PerfilEmpresario.class));
            finish();
        }
    }

    void signIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                comeIn();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    return;
                }
            }
        }
    }

    private void userLogin() {
        String emailP = email.getText().toString().trim();
        String password = constraseña.getText().toString().trim();


        //checking if bordes and passwords are empty

        if (TextUtils.isEmpty(emailP)) {
            Toast.makeText(this, "Porfavor ponga un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Porfavor ponga su contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        //if the bordes and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Entrando, espere por favor...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(emailP, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if (task.isSuccessful()) {
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), PerfilEmpresario.class));
                        } else{
                            progressDialog.setMessage("Error, algo está mal");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == entra) {
            userLogin();
        }

        if (view == registrarte) {
            finish();
            startActivity(new Intent(this, Registro.class));
        }
    }
}