package com.example.tfg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FiltrarCalendario extends AppCompatActivity {
    Button  cambiarRol, confirmarAjustes;
    TextView textViewAdmin;
    ArrayList<Calendars> arrayList;
    Boolean esAdmin;

    RecyclerView show_calendarlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_calendario);
        textViewAdmin = findViewById(R.id.textViewCambiarRol);
        cambiarRol = findViewById(R.id.cambiarRol);
        Button nameFilterButton = findViewById(R.id.nameFilterButton);
        Button dateFilterButton = findViewById(R.id.dateFilterButton);
        Button resetButton = findViewById(R.id.resetButton);
        confirmarAjustes = findViewById(R.id.buttonConfirmarAjustes);

        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        esAdmin = prefs.getBoolean("esAdmin", false);
        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(this, arrayList, esAdmin);
        show_calendarlist.setAdapter(calendarRecyclerAdapter);

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
}