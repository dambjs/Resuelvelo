package com.example.sebas_pc.resuelvelo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LogueoEmpleado extends AppCompatActivity implements View.OnClickListener {

    private Button btnEntra; //ok
    private EditText etEmail; // ok
    private EditText etPassword; // ok
    private TextView tvRegistrarte; //ok
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo_empleado);

        etEmail = (EditText) findViewById(R.id.email); // ok
        etPassword = (EditText) findViewById(R.id.constraseña); //ok
        btnEntra = (Button) findViewById(R.id.entra); // ok
        tvRegistrarte = (TextView) findViewById(R.id.registrarte); // ok


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        btnEntra.setOnClickListener(this);
        tvRegistrarte.setOnClickListener(LogueoEmpleado.this);
        comeIn();
    }

    void comeIn() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            final String userId = firebaseUser.getUid();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("empleado/users").child(userId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user == null){
                        databaseReference.setValue(new User(userId, firebaseUser.getDisplayName(), firebaseUser.getEmail()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
            startActivity(new Intent(this, PerfilEmpleado.class));
            finish();
        }
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                comeIn();
//            } else {
////                 Sign in failed
//                if (response == null) {
//                    // User pressed back button
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//                    return;
//                }
//            }
//        }
//    }

    private void userLogin() {
        String emailP = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailP)) {
            etEmail.setError("Obligatorio");
            Toast.makeText(this, "Porfavor ponga un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Obligatorio");
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
                            startActivity(new Intent(getApplicationContext(), PerfilEmpleado.class));
                        } else{
                            progressDialog.setMessage("Error, algo está mal");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == btnEntra) {
            userLogin();
        }

        if (view == tvRegistrarte) {
            finish();
            startActivity(new Intent(this, RegistroEmpleado.class));
        }
    }


}