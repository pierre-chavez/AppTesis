package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListarUsuariosActivity extends AppCompatActivity {

    ListView listViewUsuarios;
    List<Usuario> listaUsuarios;
    public static final String USUARIO = "user";


    DatabaseReference databaseUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuario");

        listaUsuarios = new ArrayList<>();

        listViewUsuarios = findViewById(R.id.usuarios);

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuario = listaUsuarios.get(position);
                actualizarUsuario(usuario);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        databaseUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaUsuarios.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){

                    Usuario usuario = userSnapshot.getValue(Usuario.class);

                    listaUsuarios.add(usuario);
                }

                UsuarioLista adapter = new UsuarioLista(ListarUsuariosActivity.this,listaUsuarios);
                listViewUsuarios.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void actualizarUsuario(Usuario usuario){

        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra(USUARIO, usuario);
        startActivity(intent);

    }
}
