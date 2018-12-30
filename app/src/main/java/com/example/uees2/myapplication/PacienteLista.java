package com.example.uees2.myapplication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PacienteLista extends ArrayAdapter<Paciente> {
    private Activity context;
    private List<Paciente> pacienteList;

    public PacienteLista(Activity context, List<Paciente> pacienteLista){
        super(context, R.layout.activity_lista_paciente, pacienteLista);
        this.context = context;
        this.pacienteList = pacienteLista;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_lista_paciente, null, true);

        TextView textViewNombres = (TextView) listViewItem.findViewById(R.id.textViewNombresCompletos);
        TextView textViewCedula = (TextView) listViewItem.findViewById(R.id.textViewCedula);
        TextView textViewHabitacion = (TextView) listViewItem.findViewById(R.id.textViewHabitacion);
        Paciente paciente = pacienteList.get(position);
        Log.d("myTag", String.valueOf(paciente.getHabitcion()));
        textViewCedula.setText(paciente.getCedula());
        textViewNombres.setText(paciente.getNombres()+" "+paciente.getApellidos());
        textViewHabitacion.setText(""+paciente.getHabitcion());

        return listViewItem;
    }
}
