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

public class CrearEmpresa extends AppCompatActivity implements View.OnClickListener {

    private EditText nombreEmp;
    private EditText fechaEmp;
    private EditText descEmp;
    private ImageView add;
    private ImageView image;
    private Button registrarse;
    int RC_IMAGE_PICK = 5677;
    Uri mediaUri;
    Uri downloaderUrl;
    String mediaTYPE;

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
        registrarse.setOnClickListener(this);

        registrarse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                registrarse.setEnabled(false);
                creaEmpresa();
                if (mediaUri != null){
                    uploadFile();
                }else{
                    writeNewPost();
                }
                finish();
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

    void writeNewPost() {
        String postKey = mDatabase.child("posts").push().getKey();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post;
        if(downloaderUrl == null){
            post = new Post(FirebaseAuth.getInstance().getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString());
        }else {
            post =  new Post(FirebaseAuth.getInstance().getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString(),downloaderUrl.toString(),mediaTYPE.toString());
        }
        mDatabase.child("posts/data").child(postKey).setValue(post);
//        mDatabase.child("posts/all-posts").child(postKey).setValue(true);
//        mDatabase.child("posts/user-posts").child(FirebaseAuth.getInstance().getUid()).child(postKey).setValue(true);
    }


    void uploadFile(){
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(mediaTYPE + "/" + UUID.randomUUID() + "-" + mediaUri.getLastPathSegment());
        fileRef.putFile(mediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloaderUrl = taskSnapshot.getDownloadUrl();

                writeNewPost();
            }
        });
    }

    private void creaEmpresa() {

        final FirebaseUser empresa = FirebaseAuth.getInstance().getCurrentUser();

        String nombre = nombreEmp.getText().toString();
        String fecha = fechaEmp.getText().toString();
        mDatabase.child("empresa").child(empresa.getUid()).setValue(new Empresa(empresa.getUid(), nombre, fecha));
//        finish();
    }

    @Override
    public void onClick(View view) {

        if(view == registrarse){
            startActivity(new Intent(this, PerfilEmpresario.class));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RC_IMAGE_PICK) {
                mediaUri = data.getData();
                mediaTYPE = "logos_empresas";
                Glide.with(this).load(mediaUri).into(image);
            }
        }

    }
}