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

    private FirebaseRecyclerAdapter mAdapter, mAdapter2;
    String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresa);

        nombreDept = findViewById(R.id.nombreDept);

        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");
        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = mDatabase.child("empresa").child(uid);
        mDatabase3 = mDatabase.child("users").child(uid);

        cargarDepartamentos();
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
        mDatabase.child(FirebaseAuth.getInstance().getUid()).child(idEmpresa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Empresa empresa = dataSnapshot.getValue(Empresa.class);

                nombreEmp.setText(empresa.displayNameEmpresa);
                        fechaEmp.setText(empresa.date);
                        descEmp.setText(empresa.descripcionEmpresa);
                        Glide.with(PerfilEmpresa.this)
                            .load(empresa.photoEmpresaUrl)
                                .into(imagen);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })
    }

    void cargarDepartamentos(){

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Departamentos dept = noteDataSnapshot.getValue(Departamentos.class);
                    if (dept != null) {
                        nombreDept.setText(dept.displayNameDept);
                    }
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

        RecyclerView recyclerView = findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query postsQuery = mDatabase2;
        Query postsQuery2 = mDatabase3;


        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Empresa>()
                .setQuery(postsQuery, Empresa.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerOptions options2 = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(postsQuery2, User.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EmpresaViewHolder holder, final int position, @NonNull final Empresa empresa) {
                holder.nombreEmp.setText(empresa.displayNameEmpresa);
                holder.fechaEmp.setText(empresa.date);
                holder.descEmp.setText(empresa.descripcionEmpresa);

                if(empresa.photoEmpresaUrl != null) {
                    holder.image.setVisibility(View.VISIBLE);
                    Glide.with(PerfilEmpresa.this)
                            .load(empresa.photoEmpresaUrl)
                            .into(holder.image);
                }else{
                    holder.image.setVisibility(View.GONE);
                }
            }

            @Override
            public EmpresaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresario, parent, false);
                return new EmpresaViewHolder(view);

            }
        };

        mAdapter2 = new FirebaseRecyclerAdapter<User, EmpresaViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull EmpresaViewHolder holder, final int position, @NonNull final User user) {
                holder.director.setText(user.displayName);

            }

            @Override
            public EmpresaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresario, parent, false);
                return new EmpresaViewHolder(view);

            }
        };

        recyclerView.setAdapter(mAdapter);

        recyclerView.setAdapter(mAdapter2);


    }

    public void create(View view) {
        Intent intent = new Intent(this, CrearIncidencia.class);
        startActivity(intent);
    }
}
