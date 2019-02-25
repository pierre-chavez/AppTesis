package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    public static final String FAMILIARIDS = "playerIds";
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
                Log.d("Editar", "Dialogo editar o eliminar");
                showUpdateDialog(paciente);
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

    private void showUpdateDialog(final Paciente paciente) {
        Log.d("Editar", "Dialogo editar o eliminar");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_dialog, null);
        final Button btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) view.findViewById(R.id.btnDelete);
        builder.setView(view);
        builder.setTitle("Actualizar Paciente " + paciente.getCedula());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePaciente(paciente);
                alertDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePaciente(paciente);
                alertDialog.dismiss();
            }
        });

    }

    public void deletePaciente(Paciente paciente) {
        DatabaseReference databasePacientes = FirebaseDatabase.getInstance().getReference("Persona").child(paciente.getCedula());
        DatabaseReference databaseCaidas = FirebaseDatabase.getInstance().getReference("Caida").child(paciente.getCedula());

        databasePacientes.removeValue();
        databaseCaidas.removeValue();

        Toast.makeText(this, "Paciente eliminado!", Toast.LENGTH_SHORT).show();
    }

    public void updatePaciente(Paciente paciente) {
        Intent intent = new Intent(getApplicationContext(), EdicionPaciente.class);

        intent.putExtra(CEDULA, paciente.getCedula());
        intent.putExtra(NOMBRES, paciente.getNombres() + " " + paciente.getApellidos());
        intent.putExtra(HABITACION, "" + paciente.getHabitcion());
        intent.putExtra(PACIENTE, paciente);
        intent.putExtra(FAMILIARIDS, paciente);
        startActivity(intent);
    }
}
