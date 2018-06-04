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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CrearIncidencia extends AppCompatActivity {

    private ImageView image, add;
    private Button enviar;
    private Spinner jaja;
    private EditText TextoOtros;
    private Spinner jeje;
    private Spinner areaSpinner2;
    private Spinner areaSpinner1;

// https://openwebinars.net/blog/como-hacer-notificaciones-push-en-android-facil/ notis
    Uri mediaUri;
    String downloaderUrl, idDepartamento;
    private final int RC_IMAGE_PICK = 5677;
    private DatabaseReference mDatabase, mDatabase2;
    private final static String[] otros = { "Fallo de impresora", "Fallo de Sistema Operativo", "Falla el ordenador",
            "Pantalla sin señal", "Proyector estropeado", "Otros" };

    private final static String[] prioridad = { "Prioridad Alta", "Prioridad Media", "Prioridad Baja"};
    String idEmpresa, idIncidencia;
    HashMap<String, String> hashMapDepartamentos = new HashMap<>();
    HashMap<String, String> hashMapEmpleados = new HashMap<>();
    String uid;

    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        uid = FirebaseAuth.getInstance().getUid();
        enviar = findViewById(R.id.enviar);
        add = findViewById(R.id.add);
        image = findViewById(R.id.image);

        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");
        idDepartamento = getIntent().getStringExtra("DEPARTAMENTO_KEY");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("empleado/users");

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar.setEnabled(false);
                if (mediaUri != null) {
                    uploadFile();
                } else {
                    incidencia();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RC_IMAGE_PICK);
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

        mDatabase.child("dep").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> departamentos = new ArrayList<String>();

                for (DataSnapshot prueba: dataSnapshot.getChildren()) {
                    for (DataSnapshot areaSnapshot: prueba.getChildren()){
                        String displayNameDept = areaSnapshot.child("displayNameDept").getValue(String.class);
                        departamentos.add(displayNameDept);
                        hashMapDepartamentos.put(displayNameDept, areaSnapshot.getKey());
                    }
                }
                areaSpinner1 = (Spinner) findViewById(R.id.sp);
                ArrayAdapter<String> departamentosAdapter = new ArrayAdapter<String>(CrearIncidencia.this, android.R.layout.simple_spinner_dropdown_item, departamentos);
                departamentosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner1.setAdapter(departamentosAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> empleados = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String displayName = areaSnapshot.child("displayName").getValue(String.class);
                    empleados.add(displayName);
                    hashMapEmpleados.put(displayName, areaSnapshot.getKey());
                }

                areaSpinner2 = (Spinner) findViewById(R.id.sp2);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CrearIncidencia.this, android.R.layout.simple_spinner_item, empleados);
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
                downloaderUrl = taskSnapshot.getDownloadUrl().toString();
                incidencia();
            }
        });
    }

    private void incidencia() {
        String departamento = areaSpinner1.getSelectedItem().toString();
        String destinatario = areaSpinner2.getSelectedItem().toString();
        String motivo = jaja.getSelectedItem().toString();
        String otros = TextoOtros.getText().toString();
        String prioridad = jeje.getSelectedItem().toString();

        String carpeta = "alta";
        if(prioridad.equals("Prioridad Media")) {
            carpeta = "media";
        } else if(prioridad.equals("Prioridad Baja")) {
            carpeta = "baja";
        }

        Incidencia incidencia = new Incidencia(uid, departamento, destinatario, motivo, prioridad, otros, downloaderUrl);

        String key = mDatabase.push().getKey();

        mDatabase.child("incidencia").child(carpeta).child(uid).child(idEmpresa).child(key).setValue(incidencia);
        mDatabase.child("incidencia").child(carpeta).child(hashMapEmpleados.get(destinatario)).child(key).setValue(incidencia);

        finish();
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