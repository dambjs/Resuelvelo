package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Departamentos;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PerfilEmpresa extends AppCompatActivity {

    private DatabaseReference mDatabase, mDatabase2, mDatabase3;
    private TextView nombreDept;
    private ImageView image;
    private TextView descEmp;
    private TextView nombreEmp;
    private TextView fechaEmp;
    private TextView director;

    String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresa);

        nombreDept = findViewById(R.id.nombreDept);
        image = findViewById(R.id.image);
        descEmp = findViewById(R.id.descEmp);
        nombreEmp = findViewById(R.id.nombreEmp);
        fechaEmp = findViewById(R.id.fechaEmp);
        director = findViewById(R.id.director);


        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");
        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("departamentos").child(uid);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("empresa").child(uid);
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("users").child(uid);


        cargarDepartamentos();
        cargarDatosEmpresa();
    }

    public void add(View view) {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nombre de Departamento")
                .setMessage("¿Que nombre quieres darle al departamento?")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final FirebaseUser departamentos = FirebaseAuth.getInstance().getCurrentUser();

                        final String nombre = taskEditText.getText().toString();
                        mDatabase.push().setValue(new Departamentos(departamentos.getUid(), nombre));

                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
        dialog.show();
    }

    void cargarDatosEmpresa(){
        mDatabase2.child(idEmpresa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Empresa empresa = dataSnapshot.getValue(Empresa.class);
                if (empresa != null) {
                    nombreEmp.setText(empresa.displayNameEmpresa);
                    fechaEmp.setText(empresa.date);
                    descEmp.setText(empresa.descripcionEmpresa);
                    Glide.with(PerfilEmpresa.this)
                            .load(empresa.photoEmpresaUrl)
                            .into(image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void cargarDepartamentos(){

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Departamentos dept = dataSnapshot.getValue(Departamentos.class);
                if (dept != null) {
                    nombreDept.setText(dept.displayNameDept);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        director.setText(user.displayName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

//        mDatabase2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                    Empresa empresa = noteDataSnapshot.getValue(Empresa.class);
//                    if (empresa != null) {
//                        nombreEmp.setText(empresa.displayNameEmpresa);
//                        fechaEmp.setText(empresa.date);
//                        descEmp.setText(empresa.descripcionEmpresa);
//                        Glide.with(PerfilEmpresa.this)
//                            .load(empresa.photoEmpresaUrl)
//                                .into(imagen);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });
//
//        mDatabase3.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        director.setText(user.displayName);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });




    }

    public void create(View view) {
        Intent intent = new Intent(this, CrearIncidencia.class);
        startActivity(intent);
    }
}
