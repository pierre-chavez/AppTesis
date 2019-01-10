package com.example.uees2.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Informacion extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_informacion);

        addSlide(AppIntroFragment.newInstance("Paciente", "En esta opción la enfermera registra el paciente ingresando datos esenciales.",
                R.drawable.enfermera, ContextCompat.getColor(getApplicationContext(), R.color.Intro1)));
        addSlide(AppIntroFragment.newInstance("Enlazar", "En esta opción se enlaza una pulsera con un paciente, por el identificador de la pulsera con el paciente por el número de cédula.",
                R.drawable.pulsera, ContextCompat.getColor(getApplicationContext(), R.color.Intro2)));
        addSlide(AppIntroFragment.newInstance("Conectar", "En esta opción se configura las credenciales WiFi SSID (Nombre de la red WiFi) y su contraseña para que la pulserae se pueda conectar a la red.",
                R.drawable.wifi, ContextCompat.getColor(getApplicationContext(), R.color.Intro3)));
        addSlide(AppIntroFragment.newInstance("Reporte de caídas", "En esta opción se listan los pacientes y las caidas de cada uno, indicando el tipo de caída y cuando se detecto.",
                R.drawable.monitor, ContextCompat.getColor(getApplicationContext(), R.color.Intro4)));
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }
}
