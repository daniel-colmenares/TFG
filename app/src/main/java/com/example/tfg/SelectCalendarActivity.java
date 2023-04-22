package com.example.tfg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectCalendarActivity extends AppCompatActivity {

    RecyclerView show_calendarlist;
    private FirebaseAuth mAuth;
    DBOpenHelper dbOpenHelper;
    Button crearcalendario, cerrarsesion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);
        crearcalendario = findViewById(R.id.button_crearcalendario);
        show_calendarlist = findViewById(R.id.recycled_selectcalendar);
        cerrarsesion = findViewById(R.id.botoncerrarsesion);
        mAuth = FirebaseAuth.getInstance();

        ArrayList<Calendars> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.getCalendarsByUser(getIntent().getStringExtra("email"),database);
        while ( cursor.moveToNext()){
            String Name = cursor.getString(cursor.getColumnIndex(DBStructure.NAME)+0);
            String Email = cursor.getString(cursor.getColumnIndex(DBStructure.EMAIL)+0);
            Integer Id = cursor.getInt(cursor.getColumnIndex("ID")+0);
            Calendars calendar = new Calendars(Name,Email,Id);
            arrayList.add(calendar);

        }
        cursor.close();
        dbOpenHelper.close();
        show_calendarlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CalendarRecyclerAdapter calendarRecyclerAdapter = new CalendarRecyclerAdapter(this,arrayList);
        show_calendarlist.setAdapter(calendarRecyclerAdapter);

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
                        // Haz algo con el nombre introducido por el usuario
                        dbOpenHelper = new DBOpenHelper(view.getContext());
                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                        dbOpenHelper.SaveCalendar(nombreCalendario, email, database);
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


}