package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener {

    CustomCalendarView customCalendarView;
    DBOpenHelper dbOpenHelper;
    //FragmentManager color;

    MyGridAdapter myGridAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name",getIntent().getExtras().getString("name"));
        editor.putString("email", getIntent().getExtras().getString("email"));
        editor.putInt("ID", getIntent().getExtras().getInt("ID"));
        editor.apply();
        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar_view);
        //glide
    }

    @Override
    public void onColorSelected(int color) {
        // Save the selected color to SharedPreferences
        SharedPreferences prefs = getSharedPreferences("CalendarioUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("color_calendario", "#" + Integer.toHexString(color));
        editor.apply();

        // Update the grid cells with the selected color
        myGridAdapter.notifyDataSetChanged();
    }

}
