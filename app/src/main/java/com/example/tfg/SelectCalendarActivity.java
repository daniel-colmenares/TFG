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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectCalendarActivity extends AppCompatActivity  {

    RecyclerView show_calendarlist;
    private FirebaseAuth mAuth;
    DBOpenHelper dbOpenHelper;
    Boolean esAdmin;
    TextView textViewAdmin;
    ArrayList<Calendars> arrayList;
    Button crearcalendario, cerrarsesion, cambiarRol;


    EditText editTextBuscar;
    Button buttonFiltrar;
    CalendarRecyclerAdapter calendarRecyclerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);
        cambiarRol = findViewById(R.id.cambiarRol);
        textViewAdmin = findViewById(R.id.textView_Admin);
        crearcalendario = findViewById(R.id.button_crearcalendario);
        show_calendarlist = findViewById(R.id.recycled_selectcalendar);
        cerrarsesion = findViewById(R.id.botoncerrarsesion);
        mAuth = FirebaseAuth.getInstance();


        //editTextBuscar = findViewById(R.id.editText_buscar);
        //buttonFiltrar = findViewById(R.id.buttonfiltrar);

        arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"),database);
        while ( cursor.moveToNext()){
            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME)+0);
            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL)+0);
            String Fecha= cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION)+0);
            Integer Id = cursor.getInt(cursor.getColumnIndex("ID")+0);
            Calendars calendar = new Calendars(Name,Email,Fecha,Id);
            arrayList.add(calendar);

        }
        cursor.close();
        dbOpenHelper.close();
        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        esAdmin  = prefs.getBoolean("esAdmin", false);
        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(this,arrayList, esAdmin);
        show_calendarlist.setAdapter(calendarRecyclerAdapter);

        if(esAdmin) {
            textViewAdmin.setText("TERAPEUTA");

        }else {
            textViewAdmin.setText("USUARIO");
        }
        crearcalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Crea un AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Introduce el nombre del calendario");


// Crea un EditText y lo agrega al AlertDialog
                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Agrega los botones "OK" y "Cancelar"
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombreCalendario = input.getText().toString();
                        String email = getIntent().getStringExtra("email");
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String currentDateString = dateFormat.format(currentDate);
                        // Haz algo con el nombre introducido por el usuario
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                        dbOpenHelper.SaveCalendar(nombreCalendario, email,currentDateString, database);

                        arrayList = new ArrayList<>();
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database1 = dbOpenHelper.getReadableDatabase();
                        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"),database1);
                        while ( cursor.moveToNext()){
                            Integer Id = cursor.getInt(cursor.getColumnIndex("ID")+0);
                            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME)+0);
                            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL)+0);
                            String Fecha = cursor.getString(cursor.getColumnIndex(DBStructure.FECHA_CREACION)+0);
                            Calendars calendar = new Calendars(Name,Email,Fecha,Id);
                            arrayList.add(calendar);

                        }
                        cursor.close();
                        dbOpenHelper.close();
                        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        esAdmin  = prefs.getBoolean("esAdmin", false);
                        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(),arrayList, esAdmin);
                        show_calendarlist.setAdapter(calendarRecyclerAdapter);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

// Crea y muestra el AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
/*
        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene el texto ingresado por el usuario
                String searchText = editTextBuscar.getText().toString().trim();

                // Filtra los calendarios que coincidan con el texto ingresado
                ArrayList<Calendars> filteredList = new ArrayList<>();
                for (Calendars calendar : arrayList) {
                    if (calendar.getNAME().toLowerCase().contains(searchText.toLowerCase())) {
                        filteredList.add(calendar);
                    }
                }

                // Actualiza el RecyclerView con los calendarios filtrados
                calendarRecyclerAdapter.filterList(filteredList);
            }
        });

*/
        Button nameFilterButton = findViewById(R.id.nameFilterButton);
        Button dateFilterButton = findViewById(R.id.dateFilterButton);

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
                MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.YEAR));
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
                                if (calendarCreationDate.get(Calendar.YEAR) == selectedYear && calendarCreationDate.get(Calendar.MONTH)+1 == selectedMonth) {
                                    filteredList.add(calendar);
                                }
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        // Actualizar el RecyclerView con los calendarios filtrados
                        calendarRecyclerAdapter.filterList(filteredList);
                    }
                });
                pd.show(getSupportFragmentManager(), "MonthYearPickerDialog");
            }
        });


        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restablecer la lista de calendarios
                calendarRecyclerAdapter.filterList(arrayList);
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
                }else {
                    Toast.makeText(SelectCalendarActivity.this, "Eres Usuario", Toast.LENGTH_SHORT).show();
                    crearcalendario.setVisibility(View.INVISIBLE);
                }
                editor.apply();
                CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(view.getContext(),arrayList, esAdmin);
                show_calendarlist.setAdapter(calendarRecyclerAdapter);
            }
        });
    }


}