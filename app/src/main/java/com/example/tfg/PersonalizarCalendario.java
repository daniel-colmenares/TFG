package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalizarCalendario extends AppCompatActivity {

    Button confirmarPersonalizar, letra_calendario, color_calendario, nombre_calendario;
    String cellColor, letraCal;
    AlertDialog alertDialog;
    String idCalendario;
    Boolean esAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizar_calendario);

        idCalendario = getIntent().getStringExtra("ID");
        confirmarPersonalizar = findViewById(R.id.confirmarPers);
        color_calendario = findViewById(R.id.color_calendario);
        letra_calendario = findViewById(R.id.letra_calendario);
        nombre_calendario = findViewById(R.id.nombre_calendario);
        SharedPreferences prefs1 = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        esAdmin = prefs1.getBoolean("esAdmin", false);
        color_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!esAdmin){
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                View addView = LayoutInflater.from(v.getContext()).inflate(R.layout.listacolores_calendario,null);
                Button azul = addView.findViewById(R.id.button_azul);
                Button rojo = addView.findViewById(R.id.button_rojo);
                Button amarillo = addView.findViewById(R.id.button_amarillo);
                Button morado = addView.findViewById(R.id.button_morado);
                Button rosa = addView.findViewById(R.id.button_rosa);
                Button verde = addView.findViewById(R.id.button_verde);
                Button blanco = addView.findViewById(R.id.button_blanco);

                blanco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#FFFFFF";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                azul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#8181F7";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
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
                if(!esAdmin){
                    return;
                }
                android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(view.getContext());
                builder2.setCancelable(true);
                View addView = LayoutInflater.from(view.getContext()).inflate(R.layout.listafuentes_calendario, null);
                Button monospace = addView.findViewById(R.id.monospace);
                Button serif = addView.findViewById(R.id.serif);
                Button cursivestandard = addView.findViewById(R.id.times);
                Button times = addView.findViewById(R.id.cursivestandard);
                Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/cursivestandard.ttf");
                Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/times.ttf");
                cursivestandard.setTypeface(typeface1);
                times.setTypeface(typeface2);

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
                cursivestandard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        letraCal = "cursivestandard";
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("letraCal", letraCal);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                times.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        letraCal = "times";
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
        nombre_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!esAdmin){
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Introduzca el nuevo nombre del calendario");

                // Agregar el campo de texto
                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Agregar los botones "Guardar" y "Cancelar"
                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCalendarName = input.getText().toString().trim();
                        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("name", newCalendarName);
                        editor.apply();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Mostrar el cuadro de diálogo
                builder.show();
            }
        });
        confirmarPersonalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}