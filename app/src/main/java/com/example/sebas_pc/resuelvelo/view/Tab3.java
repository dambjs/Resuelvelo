package com.example.sebas_pc.resuelvelo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Tab3 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mDatabase;
    private OnFragmentInteractionListener mListener;

    public Tab3() { }


    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        String uid = FirebaseAuth.getInstance().getUid();

        RecyclerView recyclerView = view.findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(Tab3.this.getActivity()));

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Incidencia>()
                .setQuery(mDatabase.child("incidencia/bajar").child(uid), Incidencia.class)
                .setLifecycleOwner(this)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Incidencia, IncidenciaViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IncidenciaViewHolder holder, final int position, @NonNull final Incidencia empresa) {

                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.departamento.setText(empresa.departamento);
                holder.motivo.setText(empresa.motivo);
                holder.prioridad.setText(empresa.prioridad);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Tab3.this.getActivity(), VerIncidenciaCompleta.class);
                        intent.putExtra("INCIDENCIA_KEY", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public IncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia, parent, false);
                return new IncidenciaViewHolder(view);

            }
        };
        recyclerView.setAdapter(mAdapter);
        return view;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
