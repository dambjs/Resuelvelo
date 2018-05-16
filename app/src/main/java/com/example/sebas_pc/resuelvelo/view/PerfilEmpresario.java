package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PerfilEmpresario extends AppCompatActivity {

    private TextView nom;
    private TextView correo;
    private DatabaseReference mDatabase, mDatabase2;
    private FirebaseRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresario);

        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("empresa").child(uid);

        correo = findViewById(R.id.email);
        nom = findViewById(R.id.displayNameEmpresa);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    nom.setText(user.displayName);
                    correo.setText(user.email);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        RecyclerView recyclerView = findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query postsQuery = mDatabase2;

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Empresa>()
                .setQuery(postsQuery, Empresa.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Empresa, EmpresaViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EmpresaViewHolder holder, final int position, @NonNull final Empresa empresa) {
                holder.nombreEmp.setText(empresa.displayNameEmpresa);
                if(empresa.photoEmpresaUrl != null) {
                    holder.image.setVisibility(View.VISIBLE);
                    Glide.with(PerfilEmpresario.this)
                            .load(empresa.photoEmpresaUrl)
                            .into(holder.image);
                }else{
                    holder.image.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PerfilEmpresario.this, PerfilEmpresa.class);
                        intent.putExtra("EMPRESA_KEY", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public EmpresaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresas, parent, false);
                return new EmpresaViewHolder(view);

            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    public void salir(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(PerfilEmpresario.this, Logueo.class));
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("¿Seguro que desea salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PerfilEmpresario.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public void add(View view) {
        Intent intent = new Intent(this, CrearEmpresa.class);
        startActivity(intent);
    }

}