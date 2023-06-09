package com.example.tfg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.Observable;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectCalendarActivity extends AppCompatActivity {

    RecyclerView show_calendarlist;
    private FirebaseAuth mAuth;
    android.app.AlertDialog alertDialog;
    DBOpenHelper dbOpenHelper;
    Boolean esAdmin;
    TextView textViewAdmin;
    ArrayList<Calendars> arrayList;
    Button crearcalendario, cerrarsesion, cambiarRol, ajustesBotton, confirmarAjustes;
    String colorCal, letraCal;


    EditText editTextBuscar;
    Button buttonFiltrar;
    CalendarRecyclerAdapter calendarRecyclerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);
        //cambiarRol = findViewById(R.id.cambiarRol);
        //textViewAdmin = findViewById(R.id.textView_Admin);
        ajustesBotton = findViewById(R.id.ajustesbutton);
        crearcalendario = findViewById(R.id.button_crearcalendario);
        show_calendarlist = findViewById(R.id.recycled_selectcalendar);
        cerrarsesion = findViewById(R.id.botoncerrarsesion);
        mAuth = FirebaseAuth.getInstance();


        //editTextBuscar = findViewById(R.id.editText_buscar);
        //buttonFiltrar = findViewById(R.id.buttonfiltrar);

        arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database);
        while (cursor.moveToNext()) {
            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME) + 0);
            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL) + 0);
            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION) + 0);
            Integer Id = cursor.getInt(cursor.getColumnIndex("ID") + 0);
            String Color = cursor.getString(cursor.getColumnIndex(DBStructure.COLOR)+0);
            String Letra = cursor.getString(cursor.getColumnIndex(DBStructure.LETRA)+0);
            Calendars calendar = new Calendars(Name, Email, Fecha, Id, Color, Letra);
            arrayList.add(calendar);

        }
        cursor.close();
        dbOpenHelper.close();
        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        esAdmin = prefs.getBoolean("esAdmin", false);
        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(this, arrayList, esAdmin);
        show_calendarlist.setAdapter(calendarRecyclerAdapter);


        crearcalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                //android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
                //builder.setCancelable(true);
                View addView = LayoutInflater.from(view.getContext()).inflate(R.layout.crear_calendario, null);
                Button selec_color = addView.findViewById(R.id.buttonColor);
                Button selec_fuente = addView.findViewById(R.id.buttonLetra);
                EditText nombreCalLayout = addView.findViewById(R.id.editTextNombreCalendario);
                Button confirmarCal = addView.findViewById(R.id.button_crearcalendario);
                Button buttonCancelar = addView.findViewById(R.id.buttonCancelar);

                selec_color.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(view.getContext());
                        builder1.setCancelable(true);
                        View addView = LayoutInflater.from(view.getContext()).inflate(R.layout.listacolores_calendario, null);
                        Button azul = addView.findViewById(R.id.button_azul);
                        Button rojo = addView.findViewById(R.id.button_rojo);
                        Button amarillo = addView.findViewById(R.id.button_amarillo);
                        Button morado = addView.findViewById(R.id.button_morado);
                        Button rosa = addView.findViewById(R.id.button_rosa);
                        Button verde = addView.findViewById(R.id.button_verde);

                        azul.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colorCal = "#8181F7";
                                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cellColor", colorCal);
                                editor.apply();
                                alertDialog.dismiss();
                            }
                        });
                        rojo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colorCal = "#FA5858";
                                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cellColor", colorCal);
                                editor.apply();
                                alertDialog.dismiss();
                            }
                        });
                        verde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colorCal = "#04B404";
                                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cellColor", colorCal);
                                editor.apply();
                                alertDialog.dismiss();
                            }
                        });
                        amarillo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colorCal = "#D7DF01";
                                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cellColor", colorCal);
                                editor.apply();
                                alertDialog.dismiss();
                            }
                        });
                        morado.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colorCal = "#D358F7";
                                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cellColor", colorCal);
                                editor.apply();
                                alertDialog.dismiss();
                            }
                        });
                        rosa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                colorCal = "#F5A9D0";
                                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cellColor", colorCal);
                                editor.apply();
                                alertDialog.dismiss();
                            }
                        });
                        builder1.setView(addView);
                        alertDialog = builder1.create();
                        alertDialog.show();
                    }
                });
                selec_fuente.setOnClickListener(new View.OnClickListener() {
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
                confirmarCal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String nombreCalendario = nombreCal.getText().toString();
                        String email = getIntent().getStringExtra("email");
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDateString = dateFormat.format(currentDate);
                        // Haz algo con el nombre introducido por el usuario
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                        if (!nombreCalLayout.getText().toString().equals("")) {
                            String calendarName = nombreCalLayout.getText().toString();
                            dbOpenHelper.SaveCalendar(calendarName, email, currentDateString, colorCal, letraCal, database);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "El nombre del calendario no puede ser nulo", Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();

                        arrayList = new ArrayList<>();
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database1 = dbOpenHelper.getReadableDatabase();
                        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database1);
                        while (cursor.moveToNext()) {
                            Integer Id = cursor.getInt(cursor.getColumnIndex("ID") + 0);
                            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME) + 0);
                            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL) + 0);
                            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION) + 0);
                            String Color = cursor.getString(cursor.getColumnIndex(DBStructure.COLOR) + 0);
                            String Letra = cursor.getString(cursor.getColumnIndex(DBStructure.LETRA) + 0);
                            Calendars calendar = new Calendars(Name, Email, Fecha, Id, Color, Letra);
                            arrayList.add(calendar);

                        }
                        cursor.close();
                        dbOpenHelper.close();
                        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        esAdmin = prefs.getBoolean("esAdmin", false);
                        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        ajustesBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.filtrar_calendar_settings);
                textViewAdmin = findViewById(R.id.textViewCambiarRol);
                cambiarRol = findViewById(R.id.cambiarRol);
                Button nameFilterButton = findViewById(R.id.nameFilterButton);
                Button dateFilterButton = findViewById(R.id.dateFilterButton);
                Button resetButton = findViewById(R.id.resetButton);
                confirmarAjustes = findViewById(R.id.buttonConfirmarAjustes);
                //View addView = LayoutInflater.from(view.getContext()).inflate(R.layout.listacolores_calendario, null);

                nameFilterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Crear el cuadro de diálogo
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Buscar calendario por nombre");

                        // Agregar el campo de texto
                        final EditText input = new EditText(view.getContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        // Agregar los botones "Buscar" y "Cancelar"
                        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String searchText = input.getText().toString().trim();
                                ArrayList<Calendars> filteredList = new ArrayList<>();
                                for (Calendars calendar : arrayList) {
                                    if (calendar.getNAME().toLowerCase().contains(searchText.toLowerCase())) {
                                        filteredList.add(calendar);
                                    }
                                }
                                calendarRecyclerAdapter.filterList(filteredList);
                                Toast.makeText(view.getContext(), "Aplicado filtro de nombre", Toast.LENGTH_SHORT).show();
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

                dateFilterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Obtener la fecha actual para inicializar el diálogo
                        Calendar calendar = Calendar.getInstance();
                        int initialYear = calendar.get(Calendar.YEAR);
                        int initialMonth = calendar.get(Calendar.MONTH);

                        // Mostrar el diálogo de selección de mes y año
                        MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                        pd.setListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                // Filtrar los calendarios por mes y año de creación
                                ArrayList<Calendars> filteredList = new ArrayList<>();
                                for (Calendars calendar : arrayList) {
                                    try {
                                        Date creationDate = dateFormat.parse(calendar.getFECHA());
                                        Calendar calendarCreationDate = Calendar.getInstance();
                                        calendarCreationDate.setTime(creationDate);
                                        if (calendarCreationDate.get(Calendar.YEAR) == selectedYear && calendarCreationDate.get(Calendar.MONTH) + 1 == selectedMonth) {
                                            filteredList.add(calendar);
                                        }
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                // Actualizar el RecyclerView con los calendarios filtrados
                                calendarRecyclerAdapter.filterList(filteredList);
                                Toast.makeText(view.getContext(), "Aplicado filtro de fecha", Toast.LENGTH_SHORT).show();
                            }
                        });
                        pd.show(getSupportFragmentManager(), "MonthYearPickerDialog");
                    }
                });
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Restablecer la lista de calendarios
                        calendarRecyclerAdapter.filterList(arrayList);
                    }
                });
                cambiarRol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        esAdmin = !esAdmin;
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("esAdmin", esAdmin);
                        if (esAdmin) {
                            Toast.makeText(SelectCalendarActivity.this, "Eres Admin", Toast.LENGTH_SHORT).show();
                            crearcalendario.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(SelectCalendarActivity.this, "Eres Usuario", Toast.LENGTH_SHORT).show();
                            crearcalendario.setVisibility(View.INVISIBLE);
                        }
                        editor.apply();
                        if (esAdmin) {
                            textViewAdmin.setText("ADMININSTRADOR");

                        } else {
                            textViewAdmin.setText("USUARIO");
                        }
                        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                    }
                });
                confirmarAjustes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
        });



                cerrarsesion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Realiza las acciones necesarias para cerrar sesión
                        // Ejemplo: cierra la sesión del usuario actual y elimina información de sesión
                        mAuth.signOut();

                        // Redirige al usuario a la pantalla de inicio de sesión
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }

    @Override
    protected void onResume() {
        if (esAdmin) {
            Toast.makeText(SelectCalendarActivity.this, "Eres Admin", Toast.LENGTH_SHORT).show();
            crearcalendario.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(SelectCalendarActivity.this, "Eres Usuario", Toast.LENGTH_SHORT).show();
            crearcalendario.setVisibility(View.INVISIBLE);
        }
        super.onResume();
    }
}
