package com.example.uees2.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    DatabaseReference databaseUsuario;
    DatabaseReference databaseNotificacion;

    EditText editTextEmail;
    Spinner spinnerRoles;
    Button buttonGuardar;
    Usuario usuario;
    List<Usuario> listaTrabajadores;
    public String listaAdmin = "";
    public String listaEnfermero = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        usuario = (Usuario) getIntent().getSerializableExtra(ListarUsuariosActivity.USUARIO);
        databaseUsuario = FirebaseDatabase.getInstance().getReference("Usuario");
        databaseNotificacion = FirebaseDatabase.getInstance().getReference("Notificacion");

        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerRoles = findViewById(R.id.spinnerRoles);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        editTextEmail.setText(usuario.getEmail());
        spinnerRoles.setSelection(getIndex(spinnerRoles, usuario.getRol()));


        listaTrabajadores = new ArrayList<>();

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rol = spinnerRoles.getSelectedItem().toString();
                actualizarUsuario(usuario, rol);

            }
        });

    }


    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }


    private boolean actualizarUsuario(Usuario usuario, String rol){

        usuario.setRol(rol);
        databaseUsuario.child(usuario.getUserId()).setValue(usuario);

        listaAdmin = getNotificacion();

        Log.d("notificacion",""+listaAdmin.length());
        Log.d("notificacion",listaAdmin);
        Log.d("notificacion",""+listaEnfermero.length());
        Log.d("notificacion",listaEnfermero);



        finish();
        Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_LONG).show();

        return true;
    }


    private boolean setNotificacion(String listaA ,String listaE){


        databaseNotificacion.child("Admin").setValue(listaA);
        databaseNotificacion.child("Enfermero").setValue(listaE);

        return true;
    }


    protected String getNotificacion() {

        databaseUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaTrabajadores.clear();
                for(DataSnapshot pacienteSnapshot : dataSnapshot.getChildren()){
                    Usuario usuario = pacienteSnapshot.getValue(Usuario.class);

                    //listaTrabajadores.add(usuario);
                    Log.d("notificacion",usuario.getRol());

                    if(usuario.getRol().equals("Admin")){
                        listaAdmin = listaAdmin+"\""+usuario.getPlayerId()+"\""+',';
                    }

                    if (usuario.getRol().equals("Enfermero")){
                        listaEnfermero = listaEnfermero+"\""+usuario.getPlayerId()+"\""+',';
                    }



                }

                //Toast.makeText(getApplicationContext(), listaAdmin , Toast.LENGTH_LONG).show();

                if(listaAdmin.length()>0){
                listaAdmin=listaAdmin.substring(0,listaAdmin.length()-1);
                }
                if(listaAdmin.length()>0) {
                    listaEnfermero = listaEnfermero.substring(0, listaEnfermero.length() - 1);
                }
                Log.d("notificacion",listaAdmin);
                setNotificacion(listaAdmin,listaEnfermero);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return listaAdmin;
    }
}
