package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Departamentos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DepartamentoViewHolder extends RecyclerView.ViewHolder{
    TextView nombreDep;
    ImageView mas;
    TextView nombrePer;
    public DepartamentoViewHolder(View itemView) {
        super(itemView);

        nombreDep = itemView.findViewById(R.id.nombreDep);
        mas = itemView.findViewById(R.id.mas);
        nombrePer = itemView.findViewById(R.id.nombrePer);

    }

}
