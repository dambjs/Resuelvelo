package com.example.sebas_pc.resuelvelo.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class CrearEmpresa extends AppCompatActivity {

    private EditText nombreEmp;
    private TextView fechaEmp;
    private EditText descEmp;
    private ImageView add;
    private ImageView image;
    private ImageView calendario;
    private Button registrarse;
    private Calendar calendar;
    private int year, month, day;

    Uri mediaUri;
    Uri downloaderUrl;
    private final int RC_IMAGE_PICK = 5677;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_empresa);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nombreEmp = (EditText) findViewById(R.id.nombreEmp);
        descEmp = (EditText) findViewById(R.id.descEmp);
        fechaEmp = (TextView) findViewById(R.id.fechaEmp);
        registrarse = (Button) findViewById(R.id.registrarse);
        calendario = (ImageView) findViewById(R.id.calendario);
        add = (ImageView) findViewById(R.id.add);
        image = (ImageView) findViewById(R.id.image);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarse.setEnabled(false);
                if (mediaUri != null){
                    uploadFile();
                } else{
                    creaEmpresa();
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

    void uploadFile(){
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("logos/" + nombreEmp.getText());
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
        String nombre = nombreEmp.getText().toString();
        String fecha = fechaEmp.getText().toString();
        String desc = descEmp.getText().toString();
        if(downloaderUrl == null) {
            mDatabase.child("empresa").child(user.getUid()).push().setValue(new Empresa(user.getUid(), nombre, fecha, desc, null));
        } else {
            mDatabase.child("empresa").child(user.getUid()).push().setValue(new Empresa(user.getUid(), nombre, fecha, desc, downloaderUrl.toString()));
        }
        finish();
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Veamos tu fecha", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        fechaEmp.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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