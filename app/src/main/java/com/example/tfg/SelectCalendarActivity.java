package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectCalendarActivity extends AppCompatActivity {

    RecyclerView show_calendarlist;
    DBOpenHelper dbOpenHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);
        show_calendarlist = findViewById(R.id.recycled_selectcalendar);

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
    }


}