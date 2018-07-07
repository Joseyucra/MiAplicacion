package com.example.ariel.loapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Pricipal extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricipal);

        Button pri = (Button) findViewById(R.id.registro_venta);
        pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), LlenadoDeFormulario.class);
                startActivityForResult(intent, 0);
            }

        });

        Button pu = (Button) findViewById(R.id.publicados);
        pu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), casasPuntos.class);
                startActivityForResult(intent, 0);
            }

        });

    }
}