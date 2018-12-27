package com.example.uees2.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pacientes extends AppCompatActivity {

    DatabaseReference databasePacientes;

    ListView listViewPacientes;
    List<Paciente> listaPacientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");
        listViewPacientes = (ListView) findViewById(R.id.pacientes);
        listaPacientes = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databasePacientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPacientes.clear();
                for(DataSnapshot pacienteSnapshot : dataSnapshot.getChildren()){
                    Paciente paciente = pacienteSnapshot.getValue(Paciente.class);
                    listaPacientes.add(paciente);
                }
                ArrayAdapter adapter = new PacienteLista(Pacientes.this, listaPacientes);
                listViewPacientes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
