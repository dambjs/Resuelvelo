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
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.FotoPersonal;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;

public class EditarPerfilEmpresario extends AppCompatActivity {

    Uri mediaUri;
    Uri downloaderUrl;
    private final int RC_IMAGE_PICK = 5677;
    private EditText nombre;
    private ImageView add;
    private ImageView imagenP;
    private Button guardarF;
    private Button guardarN;
    protected DatabaseReference mDatabase, mDatabase2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_empresario);

        nombre = findViewById(R.id.nombre);
        add = findViewById(R.id.add);
        imagenP = findViewById(R.id.imagenP);
        guardarF = findViewById(R.id.guardarF);
        guardarN = findViewById(R.id.guardarN);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();

        guardarF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarF.setEnabled(false);
                if (mediaUri != null){
                    uploadFile();
                } else{
                    creaEmpresa();
                }
                finish();
                startActivity(new Intent(EditarPerfilEmpresario.this, PerfilEmpresario.class));
            }
        });

        guardarN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                mDatabase.child("users").child(user.getUid()).setValue(new User(user.getUid(), nombre.getText().toString(),user.getEmail()));
                finish();
                startActivity(new Intent(EditarPerfilEmpresario.this, PerfilEmpresario.class));
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

    void uploadFile(){
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("imagepP/" + UUID.randomUUID());
        fileRef.putFile(mediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloaderUrl = taskSnapshot.getDownloadUrl();
                creaEmpresa();
            }
        });
    }

    private void creaEmpresa() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(downloaderUrl == null) {
            mDatabase.child("imagenPersonal").child(user.getUid()).setValue(new FotoPersonal(user.getUid(), null, null, null, null));
        } else {
            mDatabase.child("imagenPersonal").child(user.getUid()).setValue(new FotoPersonal(user.getUid(), null, downloaderUrl.toString(), null, null));
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RC_IMAGE_PICK) {
                mediaUri = data.getData();
                Glide.with(this).load(mediaUri).into(imagenP);
            }
        }
    }


    public void volver(View view) {
        Intent intent = new Intent(this, PerfilEmpresario.class);
        startActivity(intent);
    }

}