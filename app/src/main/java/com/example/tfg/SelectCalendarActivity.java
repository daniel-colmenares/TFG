package com.example.tfg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.Observable;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.hardware.SensorListener;
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
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.os.Handler;

public class SelectCalendarActivity extends AppCompatActivity {


     //implements  SensorEventListener

    RecyclerView show_calendarlist;
    private FirebaseAuth mAuth;
    android.app.AlertDialog alertDialog;
    DBOpenHelper dbOpenHelper;
    Boolean esAdmin;
    TextView textViewAdmin, textViewAñadir;
    Calendars calendars;
    ArrayList<Calendars> arrayList;
    Button crearcalendario, cerrarsesion, cambiarRol, ajustesBotton, confirmarAjustes;
    String colorCal, letraCal;


    EditText editTextBuscar;
    Button buttonFiltrar;
    CalendarRecyclerAdapter calendarRecyclerAdapter;
    List<Calendars> listaMostrada;
    int cantidadMaximaCalendarios, indiceInicioMostrado;
    List<Calendars> listaCalendarios;
    ///////////
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    /////////////////

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);
        textViewAdmin = findViewById(R.id.textViewCambiarRol);
        textViewAñadir = findViewById(R.id.textView);
        ajustesBotton = findViewById(R.id.ajustesbutton);
        crearcalendario = findViewById(R.id.button_crearcalendario);
        show_calendarlist = findViewById(R.id.recycled_selectcalendar);
        cerrarsesion = findViewById(R.id.botoncerrarsesion);
        cambiarRol = findViewById(R.id.buttonCambiarRol);
        ///////////////
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //////////////
        //RxBleClient rxBleClient = RxBleClient.create(this);
        mAuth = FirebaseAuth.getInstance();
        //calendarRecyclerAdapter = new CalendarRecyclerAdapter(getApplicationContext(),arrayList,esAdmin);
        recogerDatos();
        if (esAdmin) {
            crearcalendario.setVisibility(View.VISIBLE);
            textViewAñadir.setVisibility(View.VISIBLE);
        } else {
            crearcalendario.setVisibility(View.INVISIBLE);
            textViewAñadir.setVisibility(View.INVISIBLE);
        }







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
                builder.setView(addView);
                android.app.AlertDialog alertDialogCrear = builder.create();
                alertDialogCrear.show();

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
                        //alertDialog.dismiss();
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
                        Button times = addView.findViewById(R.id.times);
                        Button comicsans = addView.findViewById(R.id.comicsans);
                        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/comicsans.ttf");
                        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/times.ttf");
                        comicsans.setTypeface(typeface1);
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
                        comicsans.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                letraCal = "comicsans";
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
                confirmarCal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String nombreCalendario = nombreCal.getText().toString();
                        /*android.app.AlertDialog.Builder builder3 = new android.app.AlertDialog.Builder(view.getContext());
                        builder3.setCancelable(true);
                        alertDialog = builder3.create();*/
                        String email = getIntent().getStringExtra("email");
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String currentDateString = dateFormat.format(currentDate);
                        // Haz algo con el nombre introducido por el usuario
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                        if (!nombreCalLayout.getText().toString().equals("")) {
                            String calendarName = nombreCalLayout.getText().toString();
                            dbOpenHelper.SaveCalendar(calendarName, email, currentDateString, colorCal, letraCal, database);
                            Toast.makeText(SelectCalendarActivity.this, "Calendario creado", Toast.LENGTH_SHORT).show();
                            alertDialogCrear.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "El nombre del calendario no puede ser nulo", Toast.LENGTH_SHORT).show();
                        }
                        arrayList = new ArrayList<>();
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database1 = dbOpenHelper.getReadableDatabase();
                        //Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("ID"), database1);
                        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database1);

                        while (cursor.moveToNext()) {
                            Integer Id = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID) + 0);
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
                        esAdmin = prefs.getBoolean("esAdmin", true);
                        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                        calendarRecyclerAdapter.setOnItemClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = (int) view.getTag();
                                Calendars calendars = arrayList.get(position);
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Borrar calendario " + calendars.getNAME());
                                builder.setMessage("Seguro?");

// Botón "OK"
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbOpenHelper = new DBOpenHelper(view.getContext());
                                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                                        Integer Id = calendars.getID();
                                        dbOpenHelper.deleteCalendar(Id,database);

                                        arrayList = new ArrayList<>();
                                        dbOpenHelper = new DBOpenHelper(view.getContext());
                                        SQLiteDatabase database1 = dbOpenHelper.getReadableDatabase();
                                        //Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("ID"), database1);
                                        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database1);

                                        while (cursor.moveToNext()) {
                                            Integer Id1 = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID) + 0);
                                            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME) + 0);
                                            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL) + 0);
                                            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION) + 0);
                                            String Color = cursor.getString(cursor.getColumnIndex(DBStructure.COLOR) + 0);
                                            String Letra = cursor.getString(cursor.getColumnIndex(DBStructure.LETRA) + 0);
                                            Calendars calendar = new Calendars(Name, Email, Fecha, Id1, Color, Letra);
                                            arrayList.add(calendar);

                                        }
                                        cursor.close();
                                        dbOpenHelper.close();
                                        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                        esAdmin = prefs.getBoolean("esAdmin", true);
                                        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                                        calendarRecyclerAdapter.setOnItemClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int position = (int) view.getTag();
                                                Calendars calendars = arrayList.get(position);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                                builder.setTitle("Borrar calendario " + calendars.getNAME());
                                                builder.setMessage("¿Estás Seguro?");

// Botón "OK"
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dbOpenHelper = new DBOpenHelper(view.getContext());
                                                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                                                        Integer Id = calendars.getID();
                                                        dbOpenHelper.deleteCalendar(Id,database);

                                                        arrayList = new ArrayList<>();
                                                        dbOpenHelper = new DBOpenHelper(view.getContext());
                                                        SQLiteDatabase database1 = dbOpenHelper.getReadableDatabase();
                                                        //Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("ID"), database1);
                                                        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database1);

                                                        while (cursor.moveToNext()) {
                                                            Integer Id1 = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID) + 0);
                                                            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME) + 0);
                                                            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL) + 0);
                                                            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION) + 0);
                                                            String Color = cursor.getString(cursor.getColumnIndex(DBStructure.COLOR) + 0);
                                                            String Letra = cursor.getString(cursor.getColumnIndex(DBStructure.LETRA) + 0);
                                                            Calendars calendar = new Calendars(Name, Email, Fecha, Id1, Color, Letra);
                                                            arrayList.add(calendar);

                                                        }
                                                        cursor.close();
                                                        dbOpenHelper.close();
                                                        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                                                        esAdmin = prefs.getBoolean("esAdmin", true);
                                                        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                                        calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                                                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                                                        //calendarRecyclerAdapter.notifyDataSetChanged();
                                                        //dbOpenHelper.getCalendarsByUser(calendars.getEMAIL(),database);

// Acciones a realizar al hacer clic en el botón "OK"
                                                    }
                                                });
// Botón "Cancelar"
                                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
// Acciones a realizar al hacer clic en el botón "Cancelar"
                                                    }
                                                });

// Crear y mostrar el AlertDialog
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        });
                                        //calendarRecyclerAdapter.notifyDataSetChanged();
                                        //dbOpenHelper.getCalendarsByUser(calendars.getEMAIL(),database);

// Acciones a realizar al hacer clic en el botón "OK"
                                    }
                                });
// Botón "Cancelar"
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
// Acciones a realizar al hacer clic en el botón "Cancelar"
                                    }
                                });

// Crear y mostrar el AlertDialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        //calendarRecyclerAdapter.filterList(arrayList);
                    }
                });
                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Confirmación");
                        builder.setMessage("¿Estás seguro de que quieres volver?");

                        // Agregar el botón "Sí"
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialogCrear.dismiss(); // Cierra el diálogo original si el usuario confirma
                            }
                        });

                        // Agregar el botón "No"
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // No se hace nada, el diálogo original permanece abierto
                                dialog.dismiss();
                            }
                        });

                        // Mostrar el diálogo de confirmación
                        builder.show();
                    }
                });
            }
        });

        cambiarRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esAdmin = !esAdmin;
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("esAdmin", esAdmin);
                editor.apply();
                if (esAdmin) {
                    crearcalendario.setVisibility(View.VISIBLE);
                    textViewAñadir.setVisibility(View.VISIBLE);
                } else {
                    crearcalendario.setVisibility(View.INVISIBLE);
                    textViewAñadir.setVisibility(View.INVISIBLE);
                }
                if (esAdmin) {
                    textViewAdmin.setText("CAMBIAR A VISUALIZACIÓN");

                } else {
                    textViewAdmin.setText("CAMBIAR A EDICIÓN");
                }
                calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                show_calendarlist.setAdapter(calendarRecyclerAdapter);
                onResume();
            }
        });
        ajustesBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("FILTROS");
                final String[] opciones = {"Filtrar calendarios por nombre", "Filtrar calendarios por fecha",
                        "Eliminar filtro de calendarios"};

                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acciones a realizar cuando se selecciona una opción
                        String opcionSeleccionada = opciones[which];
                        switch (opcionSeleccionada) {
                            case "Filtrar calendarios por nombre":
                                AlertDialog.Builder searchBuilder = new AlertDialog.Builder(view.getContext());
                                searchBuilder.setTitle("Buscar calendario por nombre");

                                // Agregar el campo de texto
                                final EditText input = new EditText(view.getContext());
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                searchBuilder.setView(input);

                                // Agregar los botones "Buscar" y "Cancelar"
                                searchBuilder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
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
                                searchBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                // Mostrar el cuadro de diálogo de búsqueda
                                searchBuilder.show();
                                break;
                            case "Filtrar calendarios por fecha":
                                Calendar calendar = Calendar.getInstance();
                                //int initialYear = calendar.get(Calendar.YEAR);
                                //int initialMonth = calendar.get(Calendar.MONTH);

                                // Mostrar el diálogo de selección de mes y año
                                MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
                                pd.setListener(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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
                                break;
                            case "Eliminar filtro de calendarios":
                                calendarRecyclerAdapter.filterList(arrayList);
                                break;
                        }
                        // Aquí puedes realizar las acciones correspondientes a la opción seleccionada
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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


    /*@Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listeners when the activity is in the foreground
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener((SensorEventListener) this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listeners when the activity is paused or stopped
        sensorManager.unregisterListener((SensorEventListener) this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // Handle magnetometer data
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Implement your logic to handle gesture events based on magnetometer data
            if (isShakeGesture(x, y, z)) {
                // Handle shake gesture (e.g., perform an action)
                cerrarsesion.performClick();
                Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show();
            } else if (isTiltRightGesture(x, y, z)) {
                cambiarRol.performClick();
                Toast.makeText(this, "HEY", Toast.LENGTH_SHORT).show();
                // Handle tilt to the right gesture (e.g., perform an action)
            } else if (isTiltLeftGesture(x, y, z)) {
                crearcalendario.performClick();
                Toast.makeText(this, "HA", Toast.LENGTH_SHORT).show();
                // Handle tilt to the left gesture (e.g., perform an action)
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private boolean isShakeGesture(float x, float y, float z) {
        float thresholdX = 1.0f;  // Adjust as needed
        float thresholdY = 1.0f;  // Adjust as needed
        float thresholdZ = 1.0f;
        // Implement your shake gesture logic
        // For example, check if the change in magnetic field values exceeds a threshold
        return (Math.abs(x) > thresholdX || Math.abs(y) > thresholdY || Math.abs(z) > thresholdZ);
    }

    private boolean isTiltRightGesture(float x, float y, float z) {
        // Implement your tilt to the right gesture logic
        // For example, check if the x value is above a threshold and y value is close to 0
        float thresholdTilt = 1.0f;
        float thresholdY = 1.0f;
        return (x > thresholdTilt && Math.abs(y) < thresholdY);
    }

    private boolean isTiltLeftGesture(float x, float y, float z) {
        float thresholdTilt = 1.0f;
        float thresholdY = 1.0f;
        // Implement your tilt to the left gesture logic
        // For example, check if the x value is below a negative threshold and y value is close to 0
        return (x < -thresholdTilt && Math.abs(y) < thresholdY);
    }
    */

    private void recogerDatos() {
        arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database);
        while (cursor.moveToNext()) {
            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME) + 0);
            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL) + 0);
            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION) + 0);
            Integer Id = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID) + 0);
            String Color = cursor.getString(cursor.getColumnIndex(DBStructure.COLOR)+0);
            String Letra = cursor.getString(cursor.getColumnIndex(DBStructure.LETRA)+0);
            Calendars calendar = new Calendars(Name, Email, Fecha, Id, Color, Letra);
            arrayList.add(calendar);

        }
        cursor.close();
        dbOpenHelper.close();
        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        esAdmin = prefs.getBoolean("esAdmin", true);
        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        calendarRecyclerAdapter = new CalendarRecyclerAdapter(this, arrayList, esAdmin);
        show_calendarlist.setAdapter(calendarRecyclerAdapter);
        if (esAdmin) {
            textViewAdmin.setText("CAMBIAR A VISUALIZACIÓN");

        } else {
            textViewAdmin.setText("CAMBIAR A EDICIÓN");
        }
        calendarRecyclerAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                Calendars calendars = arrayList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Borrar calendario " + calendars.getNAME());
                builder.setMessage("Seguro?");

// Botón "OK"
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                        Integer Id = calendars.getID();
                        dbOpenHelper.deleteCalendar(Id,database);
                        //arrayList.remove(position);
                        //calendarRecyclerAdapter.notifyItemRemoved(position);
                        //calendarRecyclerAdapter.notifyItemRangeChanged(position, arrayList.size());

                        arrayList = new ArrayList<>();
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database1 = dbOpenHelper.getReadableDatabase();
                        //Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("ID"), database1);
                        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"), database1);

                        while (cursor.moveToNext()) {
                            Integer Id1 = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID) + 0);
                            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME) + 0);
                            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL) + 0);
                            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION) + 0);
                            String Color = cursor.getString(cursor.getColumnIndex(DBStructure.COLOR) + 0);
                            String Letra = cursor.getString(cursor.getColumnIndex(DBStructure.LETRA) + 0);
                            Calendars calendar = new Calendars(Name, Email, Fecha, Id1, Color, Letra);
                            arrayList.add(calendar);

                        }
                        cursor.close();
                        dbOpenHelper.close();
                        calendarRecyclerAdapter.notifyDataSetChanged();
                        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        esAdmin = prefs.getBoolean("esAdmin", true);
                        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(), arrayList, esAdmin);
                        //calendarRecyclerAdapter.updateDataset(arrayList);
                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                        //dbOpenHelper.getCalendarsByUser(calendars.getEMAIL(),database);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
// Acciones a realizar al hacer clic en el botón "Cancelar"
                    }
                });

// Crear y mostrar el AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /*private void actualizarListaMostrada() {
        listaMostrada.clear();
        int indiceFinMostrado = Math.min(indiceInicioMostrado + cantidadMaximaCalendarios, listaCalendarios.size());
        listaMostrada.addAll(listaCalendarios.subList(indiceInicioMostrado, indiceFinMostrado));
        calendarRecyclerAdapter.notifyDataSetChanged();
    }*/

    /*@Override
    protected void onResume() {
        recogerDatos();
        super.onResume();
    }*/
}