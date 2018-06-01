package com.example.sebas_pc.resuelvelo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;

public class IncidenciaViewHolder extends RecyclerView.ViewHolder{
    TextView departamento;
    TextView prioridad;
    TextView motivo;
    ImageView basura;

    public IncidenciaViewHolder(View itemView) {
        super(itemView);

        departamento = itemView.findViewById(R.id.departamento);
        prioridad = itemView.findViewById(R.id.prioridad);
        motivo = itemView.findViewById(R.id.motivo);
        basura = itemView.findViewById(R.id.basura);

    }
}