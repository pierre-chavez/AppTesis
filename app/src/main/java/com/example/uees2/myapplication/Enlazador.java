package com.example.uees2.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Enlazador extends AppCompatActivity {

    DatabaseReference databasePacientes;
    DatabaseReference databasePulseras;
    Spinner spinnerCedula, spinnerIdPulsera;
    Button buttonEnlazador, buttonDesenlazador;
    List<PacientePulsera> listaPulsera;
    FirebaseAuth mAuth;
    DatabaseReference databaseUsuarios;
    Usuario usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlazador);
        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");
        databasePulseras = FirebaseDatabase.getInstance().getReference("Pulsera");
        spinnerCedula = findViewById(R.id.spinnerPacientes);
        spinnerIdPulsera = findViewById(R.id.spinnerPulseras);

        buttonEnlazador = findViewById(R.id.buttonEnlazador);
        buttonDesenlazador = findViewById(R.id.buttonDesenlazador);
        listaPulsera = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuario").child(mAuth.getUid());


        buttonEnlazador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnlazarPacientePulsera();
            }
        });

        buttonDesenlazador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.getRol().equals("Admin")) {
                    quitarPaciente();
                }else{
                    Toast.makeText(view.getContext(), "Usuario no tiene permitido desenlazar paciente", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databasePacientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<>();

                for (DataSnapshot cedulaSnapshot : dataSnapshot.getChildren()) {
                    String cedula = cedulaSnapshot.child("cedula").getValue(String.class);
                    areas.add(cedula);
                }

                Spinner cedulaSpinner = (Spinner) findViewById(R.id.spinnerPacientes);
                ArrayAdapter<String> cedulasAdapter = new ArrayAdapter<>(Enlazador.this, android.R.layout.simple_spinner_item, areas);
                cedulasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cedulaSpinner.setAdapter(cedulasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databasePulseras.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<>();

                listaPulsera.clear();

                for (DataSnapshot idPulseraSnapshot : dataSnapshot.getChildren()) {

                    PacientePulsera pulsera = idPulseraSnapshot.getValue(PacientePulsera.class);
                    listaPulsera.add(pulsera);

                    String idPulsera = idPulseraSnapshot.child("idPulsera").getValue(String.class);

                    areas.add(idPulsera);
                }

                Spinner idPulseraSpinner = (Spinner) findViewById(R.id.spinnerPulseras);
                ArrayAdapter<String> idPulserasAdapter = new ArrayAdapter<>(Enlazador.this, android.R.layout.simple_spinner_item, areas);
                idPulserasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idPulseraSpinner.setAdapter(idPulserasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue(Usuario.class);

                if(usuario.getRol().equals("Admin")){
                    buttonDesenlazador.setVisibility(View.VISIBLE);
                }else{
                    buttonDesenlazador.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void EnlazarPacientePulsera() {

        String cedula = spinnerCedula.getSelectedItem().toString();
        String idPulsera = spinnerIdPulsera.getSelectedItem().toString();


        if (cedula.isEmpty()) {
            spinnerCedula.requestFocus();
            return;
        }

        if (idPulsera.isEmpty()) {
            spinnerIdPulsera.requestFocus();
            return;
        }

        PacientePulsera pacientePulsera = new PacientePulsera(idPulsera, cedula, 0);
        PacientePulsera pulsera = buscarPulsera(idPulsera);
        PacientePulsera paciente = buscarPaciente(cedula);

        if(pulsera != null) {
            //Toast.makeText(this, "pulsera:"+pulsera.getIdPulsera(), Toast.LENGTH_SHORT).show();

            if(pulsera.getCedula() == null || pulsera.getCedula().equals(null) || pulsera.getCedula().equals("")) {

                if(paciente == null) {

                    databasePulseras.child(idPulsera).setValue(pacientePulsera);
                    finish();
                    Toast.makeText(this, "Enlace exitoso", Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(this, "Paciente ya esta enlazado a una pulsera", Toast.LENGTH_SHORT).show();

                }


            }else{

                Toast.makeText(this, "Pulsera ya esta enlazada", Toast.LENGTH_SHORT).show();

            }

        }
    }
    private  void quitarPaciente(){

        String idPulsera = spinnerIdPulsera.getSelectedItem().toString();

        if (idPulsera.isEmpty()) {
            spinnerIdPulsera.requestFocus();
            return;
        }

        PacientePulsera pulsera = buscarPulsera(idPulsera);

        pulsera.setCedula("");

        databasePulseras.child(idPulsera).setValue(pulsera);
        Toast.makeText(this, "Paciente Desenlazado", Toast.LENGTH_SHORT).show();




    }
    PacientePulsera buscarPulsera(String id){
        for(PacientePulsera pulsera : listaPulsera) {
            if(pulsera.getIdPulsera().equals(id)) {
                return pulsera;
            }
        }
        return null;
    }


    PacientePulsera buscarPaciente(String cedula){
        for(PacientePulsera pulsera : listaPulsera) {
            if(pulsera.getCedula().equals(cedula)) {
                return pulsera;
            }
        }
        return null;
    }


}
