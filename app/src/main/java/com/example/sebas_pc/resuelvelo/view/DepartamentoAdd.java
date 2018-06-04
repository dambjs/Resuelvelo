package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.EditText;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Dep;
import com.example.sebas_pc.resuelvelo.model.Departamentos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DepartamentoAdd extends AppCompatActivity {

    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mDatabase;
    String idDepartamento;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamento_add);

        idDepartamento = getIntent().getStringExtra("DEPARTAMENTO_KEY");

        String uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("departamentos").child(uid).child(idDepartamento);

        RecyclerView recyclerView = findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query postsQuery = mDatabase;

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Departamentos>()
                .setQuery(postsQuery, Departamentos.class)
                .setLifecycleOwner(this)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Departamentos, DepartamentoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DepartamentoViewHolder holder, final int position, @NonNull final Departamentos empresa) {
                holder.nombrePer.setText(empresa.displayName);
                holder.correo.setText(empresa.correo);

            }

            @Override
            public DepartamentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_departamento, parent, false);
                return new DepartamentoViewHolder(view);

            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    public void add(View view) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);


        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput2);

        final EditText userInput2 = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput3);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseUser departamentos = FirebaseAuth.getInstance().getCurrentUser();

                                final String nombre = userInput.getText().toString();
                                final String correo = userInput2.getText().toString();

                                mDatabase.push().setValue(new Departamentos(departamentos.getUid(), nombre, correo));
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
}
