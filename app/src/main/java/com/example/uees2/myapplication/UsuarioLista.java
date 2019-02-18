package com.example.uees2.myapplication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UsuarioLista extends ArrayAdapter<Usuario> {

    private Activity context;
    private List<Usuario> usuarioList;


    public UsuarioLista(Activity context, List<Usuario> usuarioList){
        super(context, R.layout.lista_usuario, usuarioList);
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.lista_usuario, null, true);

        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewRol = (TextView) listViewItem.findViewById(R.id.textViewRol);

        Usuario usuario = usuarioList.get(position);
        textViewEmail.setText(usuario.getEmail());
        textViewRol.setText(usuario.getRol());


        return listViewItem;
    }
}
