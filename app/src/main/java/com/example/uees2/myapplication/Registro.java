package com.example.uees2.myapplication;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    Date fechaRegistro;
    EditText editTextCedula, editTextNombres, editTextApellidos, editTextNombreContato, editTextCelularContacto;
    EditText editTextHabitacion;
    Spinner spinnerGenero,spinnerFamiliar;
    Button buttonRegistrar;
    List<Usuario> listaUsuario;


    DatabaseReference databasePacientes;
    DatabaseReference databaseFamiliar;
    public static final String REGEX_LETRAS = "^[a-zA-ZáÁéÉíÍóÓúÚñÑüÜ\\s]+$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        databasePacientes = FirebaseDatabase.getInstance().getReference("Persona");
        databaseFamiliar = FirebaseDatabase.getInstance().getReference("Usuario");

        editTextCedula = findViewById(R.id.editTextCedula);
        editTextNombres = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        //editTextNombreContato = findViewById(R.id.editTextNombreContacto);
        editTextCelularContacto = findViewById(R.id.editTextCelularContacto);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        //spinnerHabitacion = findViewById(R.id.spinnerHabitacion);
        spinnerFamiliar = findViewById(R.id.spinnerFamiliar);
        editTextHabitacion = findViewById(R.id.editTextHabitacion);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        listaUsuario = new ArrayList<>();


        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarPaciente();
            }
        });
        /*buttonPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Pacientes.class);
                startActivity(intent);
            }
        });
        buttonEnlazador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, Enlazador.class);
                startActivity(intent);
            }
        });*/

    }


    private void RegistrarPaciente(){


        String cedula = editTextCedula.getText().toString().trim();
        String nombres = editTextNombres.getText().toString().trim();
        String apellidos = editTextApellidos.getText().toString().trim();
        //String nombreContacto = editTextNombreContato.getText().toString();
        String emailContacto = spinnerFamiliar.getSelectedItem().toString();
        String celularContacto = editTextCelularContacto.getText().toString();
        String genero = spinnerGenero.getSelectedItem().toString();
        //String habitacion = spinnerHabitacion.getSelectedItem().toString();
        String habitacion = editTextHabitacion.getText().toString().trim();


        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("dd/mm/yyyy");
        String strDate = mdformat.format(calendar.getTime());

        if (cedula.isEmpty()){
            editTextCedula.setError("Debe ingresar cedula de paciente");
            editTextCedula.requestFocus();
            return;
        }

        if (cedula.length() > 10){
            editTextCedula.setError("La cedula debe ser de 10 digitos");
            editTextCedula.requestFocus();
            return;
        }

        if (cedula.length() < 10){
            editTextCedula.setError("La cedula debe ser de 10 digitos");
            editTextCedula.requestFocus();
            return;
        }


        if (nombres.isEmpty()){
            editTextNombres.setError("Debe ingresar los nombres");
            editTextNombres.requestFocus();
            return;
        }

        if (validarCampo(nombres)){
            editTextNombres.setError("no debe contener numeros");
            editTextNombres.requestFocus();
            return;
        }

        if (apellidos.isEmpty()){
            editTextApellidos.setError("Debe ingresar los apellidos");
            editTextApellidos.requestFocus();
            return;
        }

        if (validarCampo(apellidos)){
            editTextApellidos.setError("no debe contener numeros");
            editTextApellidos.requestFocus();
            return;
        }

        if (emailContacto.isEmpty()){
            editTextNombreContato.setError("Debe seleccionar correo de contacto");
            editTextNombreContato.requestFocus();
            return;
        }

        if(!celularContacto.isEmpty()){

            if (celularContacto.length() > 10){
                editTextCelularContacto.setError("celular debe ser de 10 digitos");
                editTextCelularContacto.requestFocus();
                return;
            }

            if (celularContacto.length() < 10 ){
                editTextCelularContacto.setError("celular debe ser de 10 digitos");
                editTextCelularContacto.requestFocus();
                return;
            }

            if(!validarCampo(celularContacto)){
                editTextCelularContacto.setError("ceular no debe contener letras");
                editTextCelularContacto.requestFocus();
                return;
            }
        }




        Usuario familiar = buscarUsuario(emailContacto);

        String id = cedula;
        //int nHabitacion = Integer.parseInt(habitacion);
        String familiarIds = familiar.getPlayerId();
        String playerId = "\""+ familiarIds+"\"";
        Paciente paciente = new Paciente(cedula, nombres, apellidos, strDate, genero, habitacion, emailContacto, celularContacto, playerId);

        databasePacientes.child(id).setValue(paciente);
        finish();
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();


    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseFamiliar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaUsuario.clear();
                final List<String> emails = new ArrayList<>();


                for(DataSnapshot usuarioSnapshot : dataSnapshot.getChildren() ){

                    Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                    listaUsuario.add(usuario);
                    emails.add(usuario.getEmail());

                }


                ArrayAdapter<String> usuarioAdapter = new ArrayAdapter<>(Registro.this, android.R.layout.simple_spinner_item, emails);
                usuarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFamiliar.setAdapter(usuarioAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    Usuario buscarUsuario(String email){
        for(Usuario usuario : listaUsuario) {
            if(usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    boolean validarCampo(String texto){
        Pattern patron = Pattern.compile(REGEX_LETRAS);

        if(patron.matcher(texto).matches()){
            return false;
        }else{
            return true;
        }
    }
}
