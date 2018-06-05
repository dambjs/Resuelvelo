package com.example.sebas_pc.resuelvelo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.example.sebas_pc.resuelvelo.R;
import com.example.sebas_pc.resuelvelo.model.Incidencia;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Tab1 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mDatabase;

    private String mParam1;
    private String mParam2, uid;
    TextView nombre;

    private OnFragmentInteractionListener mListener;

    public Tab1() { }

    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
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

        final View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        uid = FirebaseAuth.getInstance().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nombre = view.findViewById(R.id.nombre);

//                mDatabase.child("empleado/users").child(uid).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        User user = dataSnapshot.getValue(User.class);
//                        if(user != null) {
//                            //nombre.setText(user.displayName);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        System.out.println("The read failed: " + databaseError.getMessage());
//                    }
//                });



        RecyclerView recyclerView = view.findViewById(R.id.list_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(Tab1.this.getActivity()));

            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Incidencia>()
                    .setQuery(mDatabase.child("incidencia").child("alta").child(uid), Incidencia.class)
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
                            Intent intent = new Intent(Tab1.this.getActivity(), VerIncidenciaEmpleado.class);
                            intent.putExtra("INCIDENCIA_KEY", getRef(position).getKey());
                            startActivity(intent);
                        }
                    });



                    holder.tick.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab1.this.getActivity());
                            builder.setMessage("¿Estas seguro que se ha resuelto esta incidencia?");
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "Si",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            String postKey = getRef(position).getKey();

                                          mDatabase.child("incidencia/alta").child(uid).child(postKey).setValue(null);

                                        }
                                    });

                            builder.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder.create();
                            alert11.show();
                        }
                    });
                }


                @Override
                public IncidenciaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia_empleado, parent, false);
                    return new IncidenciaViewHolder(view);

                }
            };
            recyclerView.setAdapter(mAdapter);

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}