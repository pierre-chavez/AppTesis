package com.example.uees2.myapplication;

import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Enlazador extends AppCompatActivity {

    DatabaseReference databasePacientes;
    DatabaseReference databasePulseras;
    Spinner spinnerCedula, spinnerIdPulsera;
    Button buttonEnlazador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlazador);
        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");
        databasePulseras = FirebaseDatabase.getInstance().getReference("Pulsera");
        spinnerCedula = findViewById(R.id.spinnerPacientes);
        spinnerIdPulsera = findViewById(R.id.spinnerPulseras);

        buttonEnlazador = findViewById(R.id.buttonEnlazador);


        buttonEnlazador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnlazarPacientePulsera();
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
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot cedulaSnapshot : dataSnapshot.getChildren()) {
                    String cedula = cedulaSnapshot.child("cedula").getValue(String.class);
                    areas.add(cedula);
                }

                Spinner cedulaSpinner = (Spinner) findViewById(R.id.spinnerPacientes);
                ArrayAdapter<String> cedulasAdapter = new ArrayAdapter<String>(Enlazador.this, android.R.layout.simple_spinner_item, areas);
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
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot idPulseraSnapshot : dataSnapshot.getChildren()) {
                    String idPulsera = idPulseraSnapshot.child("idPulsera").getValue(String.class);
                    areas.add(idPulsera);
                }

                Spinner idPulseraSpinner = (Spinner) findViewById(R.id.spinnerPulseras);
                ArrayAdapter<String> idPulserasAdapter = new ArrayAdapter<String>(Enlazador.this, android.R.layout.simple_spinner_item, areas);
                idPulserasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idPulseraSpinner.setAdapter(idPulserasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        databasePulseras.child(idPulsera).setValue(pacientePulsera);

        Toast.makeText(this, "Enlace exitoso", Toast.LENGTH_SHORT).show();


    }

}
