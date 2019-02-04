package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class CaidasHistoricoActivity extends AppCompatActivity {

    DatabaseReference databaseCaidas;

    ListView listViewCaidas;
    TextView textViewCedula, textViewNombres, textViewHabitacion;
    List<Caida> listaCaidas;
    List<Date> listaFechaCaidas;
    List<Integer> listaCantidadCaidas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caidas_historico);
        databaseCaidas = FirebaseDatabase.getInstance().getReference("Caida");
        listViewCaidas = (ListView) findViewById(R.id.caidas);
        textViewCedula = (TextView) findViewById(R.id.textViewCedula);
        textViewNombres = (TextView) findViewById(R.id.textViewNombresCompletos);
        textViewHabitacion = (TextView) findViewById(R.id.textViewHabitacion);
        listaCaidas = new ArrayList<>();
        listaFechaCaidas = new ArrayList<>();
        listaCantidadCaidas = new ArrayList<>();
        Intent intent = getIntent();

        String cedula = intent.getStringExtra(Pacientes.CEDULA);
        String nombres = intent.getStringExtra(Pacientes.NOMBRES);
        String habitacion = intent.getStringExtra(Pacientes.HABITACION);
        textViewNombres.setText(nombres);
        textViewHabitacion.setText("" + habitacion);
        databaseCaidas = FirebaseDatabase.getInstance().getReference("Caida").child(cedula);
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseCaidas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCaidas.clear();
                listaFechaCaidas.clear();

                for (DataSnapshot caidaSnapshot : dataSnapshot.getChildren()) {
                    Caida caida = caidaSnapshot.getValue(Caida.class);
                    switch (caida.getTipo_caida()) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        default:
                            break;
                    }
                    String fecha = caida.getFecha_hora();
                    fecha = fecha.replace(" ", "T");
                    fecha = fecha + "Z";
                    String[] separated = fecha.split("T");
                    fecha = separated[0];
                    Log.d("fecha", fecha);
                    String dtStart = fecha;
                    SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
                    try {
                        Date date = format.parse(dtStart);
                        listaFechaCaidas.add(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listaCaidas.add(caida);
                }
                HashSet<Date> listToSet = new HashSet<Date>(listaFechaCaidas);
                listaFechaCaidas = new ArrayList<Date>(listToSet);

                int repe = 0;
                int k = 0;
                List<Date> repetidos = new ArrayList<>();
                for (int x = 0; x < listaFechaCaidas.size(); x++) {
                    for (int y = 0; y < listaFechaCaidas.size() && !repetidos.contains(listaFechaCaidas.get(x)); y++) {
                        if (listaFechaCaidas.get(x).equals(listaFechaCaidas.get(y)))
                            repe += 1;
                    }
                    if (repe > 0) {
                        repetidos.add(k, listaFechaCaidas.get(x));
                        listaCantidadCaidas.add(k, repe + 1);
                        k++;
                    }
                    repe = 0;
                }
                ArrayAdapter adapter = new CaidaLista(CaidasHistoricoActivity.this, listaCaidas);

                Collections.reverse(listaCaidas);

                //ADAPTER
                adapter = new CaidaLista(CaidasHistoricoActivity.this, listaCaidas);
                listViewCaidas.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}



