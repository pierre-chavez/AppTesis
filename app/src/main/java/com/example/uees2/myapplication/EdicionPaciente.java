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

import java.util.Date;

public class EdicionPaciente extends AppCompatActivity {

    Paciente paciente;

    Date fechaRegistro;
    EditText editTextCedula, editTextNombres, editTextApellidos, editTextNombreContato, editTextCelularContacto;
    EditText editTextHabitacion;
    Spinner spinnerGenero,spinnerHabitacion;
    Button buttonActualizar;

    DatabaseReference databasePacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_paciente);

        paciente = (Paciente) getIntent().getSerializableExtra(EditarPaciente.PACIENTE);
        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");


        editTextCedula = findViewById(R.id.editTextCedula);
        editTextNombres = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextNombreContato = findViewById(R.id.editTextNombreContacto);
        editTextCelularContacto = findViewById(R.id.editTextCelularContacto);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        //spinnerHabitacion = findViewById(R.id.spinnerHabitacion);
        editTextHabitacion = findViewById(R.id.editTextHabitacion);
        buttonActualizar = findViewById(R.id.buttonActualizar);

        editTextCedula.setText(paciente.getCedula());
        editTextNombres.setText(paciente.getNombres());
        editTextApellidos.setText(paciente.getApellidos());
        editTextNombreContato.setText(paciente.getNombreContacto());
        editTextCelularContacto.setText(paciente.getNumeroContacto());
        spinnerGenero.setSelection(getIndex(spinnerGenero, paciente.getGenero()));
        //spinnerHabitacion.setSelection(getIndex(spinnerHabitacion, Integer.toString(paciente.getHabitcion())));
        editTextHabitacion.setText(paciente.getHabitcion());


        buttonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cedula = editTextCedula.getText().toString().trim();
                String nombres = editTextNombres.getText().toString().trim();
                String apellidos = editTextApellidos.getText().toString().trim();
                String nombreContacto = editTextNombreContato.getText().toString();
                String celularContacto = editTextCelularContacto.getText().toString();
                String genero = spinnerGenero.getSelectedItem().toString();
                String habitacion = editTextHabitacion.getText().toString().trim();
                String fecha = paciente.fechaRegistro;

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
                String familiarIds = paciente.getFamiliaIds();
                if (familiarIds.equals("playerIds")) {
                    familiarIds = "";
                }
                ActualizarPaciente(cedula, nombres, apellidos, fecha, genero, habitacion, nombreContacto, celularContacto, familiarIds);

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


    private boolean ActualizarPaciente(String cedula, String nombres, String apellidos, String fechaRegistro, String genero, String habitacion, String nombreContacto, String celular, String familiarIds) {


        String id= cedula;
        Paciente paciente = new Paciente(cedula, nombres, apellidos, fechaRegistro, genero, habitacion, nombreContacto, celular, familiarIds);


        databasePacientes.child(id).setValue(paciente);
        finish();
        Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_LONG).show();



        return true;



    }
}
