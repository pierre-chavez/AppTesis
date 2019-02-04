package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportePacientesActivity extends AppCompatActivity {
    public static final String CEDULA = "pacienteid";
    public static final String NOMBRES = "pacientenombre";
    public static final String HABITACION = "pacientehabitacion";
    DatabaseReference databasePacientes;

    ListView listViewPacientes;
    List<Paciente> listaPacientes;
    Spinner spnTipoReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_pacientes);

        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");
        listViewPacientes = findViewById(R.id.pacientes);
        listaPacientes = new ArrayList<>();
        spnTipoReporte = findViewById(R.id.spn_tipo_grafico);
        listViewPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paciente paciente = listaPacientes.get(position);
                String spinner = spnTipoReporte.getSelectedItem().toString();
                switch (spinner) {
                    case "Gráfico":
                        Intent intent1 = new Intent(getApplicationContext(), CaidasGraficoActivity.class);
                        intent1.putExtra(CEDULA, paciente.getCedula());
                        intent1.putExtra(NOMBRES, paciente.getNombres() + " " + paciente.getApellidos());
                        intent1.putExtra(HABITACION, "" + paciente.getHabitcion());
                        startActivity(intent1);
                        break;
                    case "Histórico":
                        Intent intent2 = new Intent(getApplicationContext(), CaidasHistoricoActivity.class);
                        intent2.putExtra(CEDULA, paciente.getCedula());
                        intent2.putExtra(NOMBRES, paciente.getNombres() + " " + paciente.getApellidos());
                        intent2.putExtra(HABITACION, "" + paciente.getHabitcion());
                        startActivity(intent2);
                        break;
                }
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
                for (DataSnapshot pacienteSnapshot : dataSnapshot.getChildren()) {
                    Paciente paciente = pacienteSnapshot.getValue(Paciente.class);
                    listaPacientes.add(paciente);
                }
                ArrayAdapter adapter = new PacienteLista(ReportePacientesActivity.this, listaPacientes);
                listViewPacientes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

