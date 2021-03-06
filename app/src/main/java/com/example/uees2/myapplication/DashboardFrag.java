package com.example.uees2.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button buttonRegistrar;
    Button buttonEditar;
    Button buttonEnlazar;
    Button buttonWifi;
    Button buttonReporte;
    ImageView imageViewReporte;
    ImageView imageViewWifi;
    ImageView imageViewEnlazar;
    ImageView imageViewRegistrar;
    ImageView imageViewInformacion;
    ImageView imageViewPerfil;
    ImageView imageViewUsuarios;

    FirebaseAuth mAuth;
    Usuario usuario;

    DatabaseReference databaseUsuarios;


    private OnFragmentInteractionListener mListener;

    public DashboardFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFrag newInstance(String param1, String param2) {
        DashboardFrag fragment = new DashboardFrag();
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        if (mAuth.getUid() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }


        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuario").child(mAuth.getUid());

        databaseUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue(Usuario.class);


                if(usuario.getRol().equals("Admin")){
                    imageViewUsuarios.setVisibility(View.VISIBLE);
                }else{
                    imageViewUsuarios.setVisibility(View.GONE);
                }

                if(usuario.getRol().equals("Admin") || usuario.getRol().equals("Enfermero") ){
                    Log.d("","");


                }else{

                    Intent intent = new Intent(getContext(), InicioFamiliar.class);
                    startActivity(intent);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);



        buttonRegistrar = myFragmentView.findViewById(R.id.buttonRegistrar);
        buttonEditar = myFragmentView.findViewById(R.id.buttonEditar);
        buttonEnlazar = myFragmentView.findViewById(R.id.buttonEnlazar);
        buttonReporte = myFragmentView.findViewById(R.id.buttonReporte);
        buttonWifi = myFragmentView.findViewById(R.id.buttonWifi);
        imageViewReporte = myFragmentView.findViewById(R.id.imageViewReporte);
        imageViewInformacion = myFragmentView.findViewById(R.id.imageViewInformacion);
        imageViewRegistrar = myFragmentView.findViewById(R.id.imageViewRegistrar);
        imageViewEnlazar = myFragmentView.findViewById(R.id.imageViewEnlazar);
        imageViewWifi = myFragmentView.findViewById(R.id.imageViewWifi);
        imageViewPerfil = myFragmentView.findViewById(R.id.imageViewPerfil);
        imageViewUsuarios = myFragmentView.findViewById(R.id.imageViewUsuarios);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivity(intent);
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarPaciente.class);
                startActivity(intent);
            }
        });

        buttonReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReportePacientesActivity.class);
                startActivity(intent);
            }
        });

        buttonEnlazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Enlazador.class);
                startActivity(intent);
            }
        });

        buttonWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WifiActivity.class);
                startActivity(intent);
            }
        });

        imageViewReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReportePacientesActivity.class);
                startActivity(intent);
            }
        });

        imageViewRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivity(intent);
            }
        });


        imageViewEnlazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Enlazador.class);
                startActivity(intent);
            }
        });

        imageViewWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WifiActivity.class);
                startActivity(intent);
            }
        });
        imageViewInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Informacion.class);
                startActivity(intent);
            }
        });

        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        imageViewUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListarUsuariosActivity.class);
                startActivity(intent);
            }
        });


        return myFragmentView;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
