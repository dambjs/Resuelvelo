package com.example.sebas_pc.resuelvelo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;

public class EmpresaViewHolder extends RecyclerView.ViewHolder{
    TextView nombreEmp;
    ImageView image;
    TextView fechaEmp;
    TextView director;
    TextView descEmp;

    public EmpresaViewHolder(View itemView) {
        super(itemView);

        nombreEmp = itemView.findViewById(R.id.nombreEmp);
        image = itemView.findViewById(R.id.image);
        fechaEmp = itemView.findViewById(R.id.fechaEmp);
        director = itemView.findViewById(R.id.director);
        descEmp = itemView.findViewById(R.id.descEmp);


    }
}
