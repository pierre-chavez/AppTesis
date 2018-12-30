package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Caidas extends AppCompatActivity {

    DatabaseReference databaseCaidas;

    ListView listViewCaidas;
    TextView textViewCedula, textViewNombres, textViewHabitacion;
    List<Caida> listaCaidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caidas);
        databaseCaidas = FirebaseDatabase.getInstance().getReference("Caida");
        listViewCaidas = (ListView) findViewById(R.id.caidas);
        textViewCedula = (TextView) findViewById(R.id.textViewCedula);
        textViewNombres = (TextView) findViewById(R.id.textViewNombresCompletos);
        textViewHabitacion = (TextView) findViewById(R.id.textViewHabitacion);
        listaCaidas = new ArrayList<>();

        Intent intent = getIntent();

        String cedula = intent.getStringExtra(Pacientes.CEDULA);
        String nombres = intent.getStringExtra(Pacientes.NOMBRES);
        String habitacion = intent.getStringExtra(Pacientes.HABITACION);
        textViewCedula.setText(cedula);
        textViewNombres.setText(nombres);
        textViewHabitacion.setText(""+habitacion);
        databaseCaidas = FirebaseDatabase.getInstance().getReference("Caida").child(cedula);

    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseCaidas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCaidas.clear();
                for(DataSnapshot caidaSnapshot : dataSnapshot.getChildren()){
                    Caida caida = caidaSnapshot.getValue(Caida.class);
                    listaCaidas.add(caida);
                }
                ArrayAdapter adapter = new CaidaLista(Caidas.this, listaCaidas);
                listViewCaidas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
