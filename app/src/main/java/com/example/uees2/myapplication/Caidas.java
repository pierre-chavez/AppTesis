package com.example.uees2.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class Caidas extends AppCompatActivity {

    DatabaseReference databaseCaidas;

    ListView listViewCaidas;
    TextView textViewCedula, textViewNombres, textViewHabitacion;
    List<Caida> listaCaidas;
    List<Date> listaFechaCaidas;
    List<Integer> listaCantidadCaidas;

    GraphView graphCaida;
    GraphView graphFecha;
    private int mNumLabels = 4;
    int cantidadCaida = 0;
    int caidaTipo1 = 0;
    int caidaTipo2 = 0;
    int caidaTipo3 = 0;
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
        listaFechaCaidas = new ArrayList<>();
        listaCantidadCaidas = new ArrayList<>();
        Intent intent = getIntent();

        String cedula = intent.getStringExtra(Pacientes.CEDULA);
        String nombres = intent.getStringExtra(Pacientes.NOMBRES);
        String habitacion = intent.getStringExtra(Pacientes.HABITACION);
        //textViewCedula.setText(cedula);
        textViewNombres.setText(nombres);
        textViewHabitacion.setText(""+habitacion);
        databaseCaidas = FirebaseDatabase.getInstance().getReference("Caida").child(cedula);

        graphCaida = (GraphView) findViewById(R.id.graph);
        //graphFecha = (GraphView) findViewById(R.id.graphFecha);
        initGraphTipoCaida(graphCaida);
        //initGraphFecha(graphFecha);
    }

    public void initGraphTipoCaida(GraphView graph) {
        graph.removeAllSeries();
        BarGraphSeries<DataPoint> tipoCaida1 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(1, caidaTipo1),
        });
        tipoCaida1.setAnimated(true);
        graph.addSeries(tipoCaida1);

        BarGraphSeries<DataPoint> tipoCaida2 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(2, caidaTipo2),
        });
        tipoCaida2.setAnimated(true);
        graph.addSeries(tipoCaida2);

        BarGraphSeries<DataPoint> tipoCaida3 = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(3, caidaTipo3),
        });
        tipoCaida3.setAnimated(true);
        graph.addSeries(tipoCaida3);

        // styling
        tipoCaida1.setColor(Color.RED);
        tipoCaida2.setColor(Color.GREEN);
        tipoCaida3.setColor(Color.BLUE);

        tipoCaida1.setSpacing(5);
        tipoCaida2.setSpacing(5);
        tipoCaida3.setSpacing(5);
        // draw values on top
        tipoCaida1.setDrawValuesOnTop(true);
        tipoCaida2.setDrawValuesOnTop(true);
        tipoCaida3.setDrawValuesOnTop(true);

        tipoCaida1.setValuesOnTopColor(Color.BLACK);
        tipoCaida2.setValuesOnTopColor(Color.BLACK);
        tipoCaida3.setValuesOnTopColor(Color.BLACK);

        // legend
        tipoCaida1.setTitle("De frente");
        tipoCaida2.setTitle("De espalda");
        tipoCaida3.setTitle("De lado");

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(cantidadCaida + 1);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }

    public void initGraphFecha(GraphView graph) {
        graph.removeAllSeries();
        // generate Dates
        Calendar calendar = Calendar.getInstance();

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData());
        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(mNumLabels);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not nessecary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    private DataPoint[] generateData() {
        int count = listaFechaCaidas.size();
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            DataPoint v = new DataPoint(i, listaCantidadCaidas.get(i));
            Log.d("Cantidad caidas", "" + listaCantidadCaidas.get(i));
            values[i] = v;
        }
        return values;
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseCaidas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCaidas.clear();
                listaFechaCaidas.clear();
                caidaTipo1 = 0;
                caidaTipo2 = 0;
                caidaTipo3 = 0;

                for(DataSnapshot caidaSnapshot : dataSnapshot.getChildren()){
                    Caida caida = caidaSnapshot.getValue(Caida.class);
                    switch (caida.getTipo_caida()) {
                        case 1:
                            caidaTipo1++;
                            break;
                        case 2:
                            caidaTipo2++;
                            break;
                        case 3:
                            caidaTipo3++;
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
                cantidadCaida = Math.max(caidaTipo1, caidaTipo2);
                cantidadCaida = Math.max(cantidadCaida, caidaTipo3);
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
                ArrayAdapter adapter = new CaidaLista(Caidas.this, listaCaidas);
                listViewCaidas.setAdapter(adapter);

                initGraphTipoCaida(graphCaida);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
