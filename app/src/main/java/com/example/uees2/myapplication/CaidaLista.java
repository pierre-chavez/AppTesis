package com.example.uees2.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CaidaLista extends ArrayAdapter<Caida> {

    private Activity context;
    private List<Caida> caidaList;

    public CaidaLista(Context context, int resource) {
        super(context, resource);
    }

    public CaidaLista(Activity context, List<Caida> caidaLista){
        super(context, R.layout.activity_lista_caida, caidaLista);
        this.context = context;
        this.caidaList = caidaLista;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_lista_caida, null, true);

        TextView textViewId = (TextView) listViewItem.findViewById(R.id.textViewIdCaida);
        TextView textViewFechaHora = (TextView) listViewItem.findViewById(R.id.textViewFechaHora);
        TextView textViewTipoCaida = (TextView) listViewItem.findViewById(R.id.textViewTipoCaida);

        Caida caida = caidaList.get(position);
        //Log.d("myTag", String.valueOf(caida.getFecha_hora()));
        int i = position +1;
        textViewId.setText(String.valueOf(i));
        textViewFechaHora.setText(caida.getFecha_hora());
        String tipoCaida = "";
        switch (caida.getTipo_caida()){
            case 1:
                tipoCaida = "De frente";
                break;
            case 2:
                tipoCaida = "De espalda";
                break;
            case 3:
                tipoCaida = "De lado";
                break;
            default:
                tipoCaida = "Indeterminado";
                break;
        }
        textViewTipoCaida.setText(tipoCaida);

        return listViewItem;
    }
}
