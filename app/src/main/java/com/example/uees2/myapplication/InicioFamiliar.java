package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class InicioFamiliar extends AppCompatActivity {

    ImageView imageViewPerfil, imageViewReporte;
    Button buttonReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_familiar);

        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        imageViewReporte = findViewById(R.id.imageViewReporte);
        buttonReporte = findViewById(R.id.buttonReporte);


        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        imageViewReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReportePacientesActivity.class);
                startActivity(intent);
            }
        });


        buttonReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReportePacientesActivity.class);
                startActivity(intent);
            }
        });
    }
}
