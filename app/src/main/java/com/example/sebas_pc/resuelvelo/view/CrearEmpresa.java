package com.example.sebas_pc.resuelvelo.view;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CrearEmpresa extends AppCompatActivity {

    private EditText nombreEmp;
    private EditText fechaEmp;
    private EditText descEmp;
    private ImageView add;
    private ImageView image;
    private Button registrarse;
    Uri mediaUri;
    Uri downloaderUrl;
    String mediaTYPE;
    private final int RC_IMAGE_PICK = 5677;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empresa);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nombreEmp = (EditText) findViewById(R.id.nombreEmp);
        fechaEmp = (EditText) findViewById(R.id.fechaEmp);
        registrarse = (Button) findViewById(R.id.registrarse);
        add = (ImageView) findViewById(R.id.add);
        image = (ImageView) findViewById(R.id.image);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creaEmpresa();
                registrarse.setEnabled(false);
                if (mediaUri != null){
                    uploadFile();
                }
                else{
                    writeNewPost();
                }
                finish();
                startActivity(new Intent(CrearEmpresa.this, PerfilEmpresario.class));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RC_IMAGE_PICK);
            }
        });
    }

    private void creaEmpresa() {
        final FirebaseUser empresa = FirebaseAuth.getInstance().getCurrentUser();
        String nombre = nombreEmp.getText().toString();
        String fecha = fechaEmp.getText().toString();
        mDatabase.child("empresa").child(empresa.getUid()).setValue(new Empresa(empresa.getUid(), nombre, fecha));
        finish();
    }

    void writeNewPost() {
        String postKey = mDatabase.child("logoEmpresas").push().getKey();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post;
        if(downloaderUrl == null){
            post = new Post(firebaseUser.getPhotoUrl().toString());
        }else {
            post =  new Post(firebaseUser.getPhotoUrl().toString(),downloaderUrl.toString(),mediaTYPE.toString());
        }
        mDatabase.child("logoEmpresas").child(postKey).setValue(post);
    }

    void uploadFile(){
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(mediaTYPE + "/" + nombreEmp.getText());
        fileRef.putFile(mediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloaderUrl = taskSnapshot.getDownloadUrl();
                writeNewPost();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RC_IMAGE_PICK) {
                mediaUri = data.getData();
                mediaTYPE = "logoEmpresas";
                Glide.with(this).load(mediaUri).into(image);
            }
        }
    }
}