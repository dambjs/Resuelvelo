package com.example.sebas_pc.resuelvelo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;

public class EmpresaViewHolder extends RecyclerView.ViewHolder{
    TextView nombreEmp;
    ImageView image;


    public EmpresaViewHolder(View itemView) {
        super(itemView);

        nombreEmp = itemView.findViewById(R.id.nombreEmp);
        image = itemView.findViewById(R.id.image);

    }
}
