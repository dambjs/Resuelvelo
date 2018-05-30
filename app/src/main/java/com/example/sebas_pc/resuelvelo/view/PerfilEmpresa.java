package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.Context;
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
import com.example.sebas_pc.resuelvelo.model.Dep;
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
    private ImageView image;
    private TextView descEmp;
    private TextView nombreEmp;
    private TextView fechaEmp;
    private TextView director;
    private FirebaseRecyclerAdapter mAdapter;
    String idEmpresa;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresa);

        image = findViewById(R.id.image);
        descEmp = findViewById(R.id.descEmp);
        nombreEmp = findViewById(R.id.nombreEmp);
        fechaEmp = findViewById(R.id.fechaEmp);
        director = findViewById(R.id.director);


        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");

        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("dep").child(idEmpresa);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("empresa").child(uid);
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        cargarDepartamentos();
        cargarDatosEmpresa();

        RecyclerView recyclerView = findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query postsQuery = mDatabase;

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Dep>()
                .setQuery(postsQuery, Dep.class)
                .setLifecycleOwner(this)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Dep, DepViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DepViewHolder holder, final int position, @NonNull final Dep empresa) {
                holder.nombreDep.setText(empresa.displayNameDept);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PerfilEmpresa.this, DepartamentoAdd.class);
                        intent.putExtra("DEPARTAMENTO_KEY", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public DepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dep, parent, false);
                return new DepViewHolder(view);

            }
        };
        recyclerView.setAdapter(mAdapter);


    }

    public void add(View view) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dep, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);


        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        final EditText userInput2 = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput2);

        final EditText userInput3 = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput3);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseUser dep = FirebaseAuth.getInstance().getCurrentUser();

                                final String departamento = userInput.getText().toString();

                                mDatabase.push().setValue(new Dep(dep.getUid(), departamento));

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
    }

//    https://medium.com/mobiletech/firebase-authentication-sample-371b5940ba93


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
    }

    public void create(View view) {
        Intent intent = new Intent(this, CrearIncidencia.class);
        startActivity(intent);
    }

    public void ver(View view) {
        Intent intent = new Intent(this, VerIncidencia.class);
        startActivity(intent);
    }
}