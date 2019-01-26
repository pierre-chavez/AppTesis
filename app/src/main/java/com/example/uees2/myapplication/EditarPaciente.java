package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditarPaciente extends AppCompatActivity {

    public static final String CEDULA = "pacienteid";
    public static final String NOMBRES = "pacientenombre";
    public static final String HABITACION = "pacientehabitacion";
    public static final String PACIENTE = "paciente";
    DatabaseReference databasePacientes;

    ListView listViewPacientes;
    List<Paciente> listaPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_paciente);

        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");
        listViewPacientes = findViewById(R.id.pacientes2);
        listaPacientes = new ArrayList<>();

        listViewPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paciente paciente = listaPacientes.get(position);
                Intent intent = new Intent(getApplicationContext(), EdicionPaciente.class);

                intent.putExtra(CEDULA, paciente.getCedula());
                intent.putExtra(NOMBRES, paciente.getNombres()+" "+paciente.getApellidos());
                intent.putExtra(HABITACION, ""+paciente.getHabitcion());
                //
                intent.putExtra(PACIENTE, paciente);
                startActivity(intent);
            }
        });
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
                ArrayAdapter adapter = new PacienteLista(EditarPaciente.this, listaPacientes);
                listViewPacientes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
