package com.example.uees2.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    DatabaseReference databaseUsuario;
    EditText editTextEmail;
    Spinner spinnerRoles;
    Button buttonGuardar;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        usuario = (Usuario) getIntent().getSerializableExtra(ListarUsuariosActivity.USUARIO);
        databaseUsuario = FirebaseDatabase.getInstance().getReference("Usuario");

        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerRoles = findViewById(R.id.spinnerRoles);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        editTextEmail.setText(usuario.getEmail());
        spinnerRoles.setSelection(getIndex(spinnerRoles, usuario.getRol()));




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

        finish();
        Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_LONG).show();

        return true;
    }
}
