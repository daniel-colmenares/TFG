package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PersonalizarCalendario extends AppCompatActivity {

    Button confirmarPersonalizar, letra_calendario, color_calendario;
    String cellColor, letraCal;
    AlertDialog alertDialog;
    String idCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizar_calendario);

        idCalendario = getIntent().getStringExtra("ID");
        confirmarPersonalizar = findViewById(R.id.confirmarPers);
        color_calendario = findViewById(R.id.color_calendario);
        letra_calendario = findViewById(R.id.letra_calendario);
        color_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setCancelable(true);
                View addView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listacolores_calendario,null);
                Button azul = addView.findViewById(R.id.button_azul);
                Button rojo = addView.findViewById(R.id.button_rojo);
                Button amarillo = addView.findViewById(R.id.button_amarillo);
                Button morado = addView.findViewById(R.id.button_morado);
                Button rosa = addView.findViewById(R.id.button_rosa);
                Button verde = addView.findViewById(R.id.button_verde);

                azul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#8181F7";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        Toast.makeText(view.getContext(), "Color de calendario cambiado a azul", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                rojo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#FA5858";

                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        Toast.makeText(view.getContext(), "Color de calendario: rojo", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                verde.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#04B404";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        Toast.makeText(view.getContext(), "Color de calendario: verde", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                amarillo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#D7DF01";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        Toast.makeText(view.getContext(), "Color de calendario: amarillo", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                morado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#D358F7";

                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        Toast.makeText(view.getContext(), "Color de calendario: morado", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                rosa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#F5A9D0";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        Toast.makeText(view.getContext(), "Color de calendario: rosa", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        letra_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(view.getContext());
                builder2.setCancelable(true);
                View addView = LayoutInflater.from(view.getContext()).inflate(R.layout.listafuentes_calendario, null);
                Button monospace = addView.findViewById(R.id.monospace);
                Button serif = addView.findViewById(R.id.serif);
                Button curisva = addView.findViewById(R.id.cursive);
                Button casual = addView.findViewById(R.id.casual);

                monospace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        letraCal = "monospace";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("letraCal", letraCal);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                serif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        letraCal = "serif";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("letraCal", letraCal);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                curisva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        letraCal = "cursiva";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("letraCal", letraCal);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                casual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        letraCal = "casual";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("letraCal", letraCal);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                builder2.setView(addView);
                alertDialog = builder2.create();
                alertDialog.show();
            }
        });
        confirmarPersonalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
}