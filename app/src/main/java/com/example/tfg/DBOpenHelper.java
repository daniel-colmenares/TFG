package com.example.tfg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private final static String CREATE_EVENTS_TABLE= "create table " +DBStructure.EVENT_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructure.EVENT+ " TEXT, " + DBStructure.DATE+" TEXT, "+DBStructure.MONTH+" TEXT, "+DBStructure.YEAR+" TEXT, "+DBStructure.IMAGEN+" TEXT)";

    private final static String CREATE_CALENDAR_TABLE= "create table " +DBStructure.CALENDAR_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBStructure.NAME+ " TEXT, " + DBStructure.EMAIL+" TEXT)";

    private static final String DROP_EVENTS_TABLE="DROP TABLE IF EXISTS "+DBStructure.EVENT_TABLE_NAME;

    private static final String DROP_CALENDAR_TABLE="DROP TABLE IF EXISTS "+DBStructure.CALENDAR_TABLE_NAME;
    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME,null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_CALENDAR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_EVENTS_TABLE);
        db.execSQL(DROP_CALENDAR_TABLE);
        onCreate(db);
    }

    public void SaveCalendar(String name, String email, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.NAME,name);
        contentValues.put(DBStructure.EMAIL,email);
        database.insert(DBStructure.CALENDAR_TABLE_NAME,null,contentValues);

    }
    public void SaveEvent(String event, Uri uri, String date, String month, String year, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT,event);
        contentValues.put(DBStructure.IMAGEN,uri.toString());
        contentValues.put(DBStructure.DATE,date);
        contentValues.put(DBStructure.MONTH,month);
        contentValues.put(DBStructure.YEAR,year);
        database.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues);

    }

    public Cursor ReadEvents (String date, SQLiteDatabase database ){
        String [] Projections = {DBStructure.EVENT,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR, DBStructure.IMAGEN};
        String Selection = DBStructure.DATE + "=?";
        String [] SelectionArgs  = {date};

        return database.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);


    }

    public Cursor ReadEventsPerMonth (String month,String year, SQLiteDatabase database ){
        String [] Projections = {DBStructure.EVENT,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR, DBStructure.IMAGEN};
        String Selection = DBStructure.MONTH + "=? and "+DBStructure.YEAR+"=?";
        String [] SelectionArgs  = {month,year};

        return database.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);


    }


}