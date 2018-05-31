package com.example.sebas_pc.resuelvelo.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrearIncidencia extends AppCompatActivity {

    //    private String dropDownItemArr[] = {"Prioridad Baja", "Prioridad Media", "Prioridad Alta"};
    private ImageView image, add;
    private Button enviar;
    private Spinner jaja;
    private EditText TextoOtros;
    private Spinner jeje;
    private Spinner areaSpinner2;
    private Spinner areaSpinner1;

    Uri mediaUri;
    Uri downloaderUrl;
    private final int RC_IMAGE_PICK = 5677;
    private DatabaseReference mDatabase, mDatabase2, mDatabase3, mDatabase4, mDatabase5, mDatabase6, mDatabase7, mDatabase8;
    private final static String[] otros = { "Fallo de impresora", "Fallo de Sistema Operativo", "Falla el ordenador",
            "Pantalla sin señal", "Proyector estropeado", "Otros" };

    private final static String[] prioridad = { "Prioridad Alta", "Prioridad Media", "Prioridad Baja"};
    String idEmpresa, idIncidencia;


    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        String uid = FirebaseAuth.getInstance().getUid();
        enviar = findViewById(R.id.enviar);
        add = findViewById(R.id.add);
        image = findViewById(R.id.image);


        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");
        idIncidencia = getIntent().getStringExtra("INCIDENCIA_KEY");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("empleado/users");
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("incidencia/alta").child(uid);
        mDatabase4 = FirebaseDatabase.getInstance().getReference().child("incidencia/media").child(uid);
        mDatabase5 = FirebaseDatabase.getInstance().getReference().child("incidencia/baja").child(uid);

        mDatabase6 = FirebaseDatabase.getInstance().getReference().child("empleado/incidencia/alta");

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar.setEnabled(false);
                if (mediaUri != null){
                    uploadFile();
                } else{
                    incidencia();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RC_IMAGE_PICK);
            }
        });


        jaja = (Spinner) findViewById(R.id.sp3);
        TextoOtros = (EditText) findViewById(R.id.textootros);

        jaja.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, otros));

        jaja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (jaja.getItemAtPosition(position).toString().equalsIgnoreCase("Otros")) {
                    TextoOtros.setVisibility(View.VISIBLE);
                } else {
                    TextoOtros.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        jeje = (Spinner) findViewById(R.id.sp4);

        jeje.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, prioridad));

        mDatabase.child("dep").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot prueba: dataSnapshot.getChildren()) {
                    for (DataSnapshot areaSnapshot: prueba.getChildren()){
                        String areaName = areaSnapshot.child("displayNameDept").getValue(String.class);
                        areas.add(areaName);
                    }

                }

                areaSpinner1 = (Spinner) findViewById(R.id.sp);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CrearIncidencia.this, android.R.layout.simple_spinner_dropdown_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner1.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("displayName").getValue(String.class);
                    areas.add(areaName);
                }


                areaSpinner2 = (Spinner) findViewById(R.id.sp2);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CrearIncidencia.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner2.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    void uploadFile(){
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("incidencia/" + UUID.randomUUID());
        fileRef.putFile(mediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloaderUrl = taskSnapshot.getDownloadUrl();
                incidencia();
            }
        });
    }

    private void incidencia() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String departamento = areaSpinner1.getSelectedItem().toString();
        String destinatario = areaSpinner2.getSelectedItem().toString();
        String motivo = jaja.getSelectedItem().toString();
        String otros = TextoOtros.getText().toString();
        String prioridad = jeje.getSelectedItem().toString();


        if(prioridad.equals("Prioridad Alta")){
            if(downloaderUrl == null) {
                mDatabase3.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, null));
                mDatabase6.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, null));

            } else {
                mDatabase3.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, downloaderUrl.toString()));
                mDatabase6.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, null));

            }

            finish();
        }


        if(prioridad.equals("Prioridad Media")){
            if(downloaderUrl == null) {
                mDatabase4.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, null));
            } else {
                mDatabase4.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, downloaderUrl.toString()));
            }
            finish();
        }

        if(prioridad.equals("Prioridad Baja")){
            if(downloaderUrl == null) {
                mDatabase5.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, null));
            } else {
                mDatabase5.push().setValue(new Incidencia(user.getUid(), departamento, destinatario, motivo, prioridad, otros, downloaderUrl.toString()));
            }
            finish();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RC_IMAGE_PICK) {
                mediaUri = data.getData();
                Glide.with(this).load(mediaUri).into(image);
            }
        }
    }
}