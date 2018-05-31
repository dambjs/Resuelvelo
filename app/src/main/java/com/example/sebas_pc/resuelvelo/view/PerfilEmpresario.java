package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Empresa;
import com.example.sebas_pc.resuelvelo.model.FotoPersonal;
import com.example.sebas_pc.resuelvelo.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PerfilEmpresario extends AppCompatActivity {

    private TextView nom;
    private TextView correo;
    private ImageView imagenP;
    private TextView txt1;
    private DatabaseReference mDatabase, mDatabase2, mDatabase3;
    private FirebaseRecyclerAdapter mAdapter;
    String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_empresario);

        idEmpresa = getIntent().getStringExtra("EMPRESA_KEY");
        final String uid = FirebaseAuth.getInstance().getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("empresa").child(uid);
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("imagenPersonal").child(uid);


        correo = findViewById(R.id.email);
        nom = findViewById(R.id.displayNameEmpresa);
        imagenP = findViewById(R.id.imagenP);
        txt1 = findViewById(R.id.txt1);


        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FotoPersonal fotoPersonal = dataSnapshot.getValue(FotoPersonal.class);
                if (fotoPersonal != null) {
                    Glide.with(PerfilEmpresario.this)
                            .load(fotoPersonal.imagenP)
                            .into(imagenP);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

        final RecyclerView recyclerView = findViewById(R.id.list_view);
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

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PerfilEmpresario.this);
                        builder1.setMessage("¿ Estas seguro que deseas eliminar esta empresa?.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Si",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String postKey = getRef(position).getKey();
                                        mDatabase2.child(postKey).setValue(null);
                                        //Falta que no pete cuando se elimina el ultimo item.
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        return true;
                    }
                });
            }

            @Override
            public EmpresaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empresa, parent, false);
                return new EmpresaViewHolder(view);

            }


        };
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás");
                finish();
                return true;
            case R.id.editar_perfil:
                Intent intent = new Intent(this, EditarPerfilEmpresario.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void salir(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(PerfilEmpresario.this, LogueoEmpresario.class));
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
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
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