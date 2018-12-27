package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Registro extends AppCompatActivity {

    Date fechaRegistro;
    EditText editTextCedula, editTextNombres, editTextApellidos, editTextNombreContato, editTextCelularContacto;
    Spinner spinnerGenero,spinnerHabitacion;
    Button buttonRegistrar;
    Button buttonPacientes;

    DatabaseReference databasePacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");

        editTextCedula = findViewById(R.id.editTextCedula);
        editTextNombres = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextNombreContato = findViewById(R.id.editTextNombreContacto);
        editTextCelularContacto = findViewById(R.id.editTextCelularContacto);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerHabitacion = findViewById(R.id.spinnerHabitacion);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonPacientes = findViewById(R.id.buttonPacientes);


        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegistrarPaciente();
            }
        });
        buttonPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Pacientes.class);
                startActivity(intent);
            }
        });


    }


    private void RegistrarPaciente(){

        String cedula = editTextCedula.getText().toString().trim();
        String nombres = editTextNombres.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        String nombreContacto = editTextNombreContato.getText().toString();
        String celularContacto = editTextCelularContacto.getText().toString();
        String genero = spinnerGenero.getSelectedItem().toString();
        String habitacion = spinnerHabitacion.getSelectedItem().toString();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/mm/yyyy");
        String strDate = mdformat.format(calendar.getTime());

        if (cedula.isEmpty()){
            editTextCedula.setError("Debe ingresar cedula de paciente");
            editTextCedula.requestFocus();
            return;
        }

        if (nombres.isEmpty()){
            editTextNombres.setError("Debe ingresar los nombres");
            editTextNombres.requestFocus();
            return;
        }

        if (apellidos.isEmpty()){
            editTextApellidos.setError("Debe ingresar los apellidos");
            editTextApellidos.requestFocus();
            return;
        }

        if (nombreContacto.isEmpty()){
            editTextNombreContato.setError("Debe ingresar nombre de persona de contacto");
            editTextNombreContato.requestFocus();
            return;
        }

        if (celularContacto.isEmpty()){
            editTextCelularContacto.setError("Debe ingresar celular de persona de contacto");
            editTextCelularContacto.requestFocus();
            return;
        }



        String id = cedula;
        int nHabitacion = Integer.parseInt(habitacion);

        Paciente paciente = new Paciente(cedula,nombres,apellidos,strDate,genero,nHabitacion,nombreContacto,celularContacto);

        databasePacientes.child(id).setValue(paciente);

        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();


    }


}
