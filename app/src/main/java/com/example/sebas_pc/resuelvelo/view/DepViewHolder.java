package com.example.sebas_pc.resuelvelo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;

public class DepViewHolder extends RecyclerView.ViewHolder{
    TextView nombreDep;

    public DepViewHolder(View itemView) {
        super(itemView);

        nombreDep = itemView.findViewById(R.id.nombreDep);


    }

}