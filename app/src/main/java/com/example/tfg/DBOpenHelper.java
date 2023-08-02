package com.example.tfg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private final static String CREATE_EVENTS_TABLE = "CREATE TABLE " + DBStructure.EVENT_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBStructure.EVENT + " TEXT, " + DBStructure.DATE + " TEXT, " + DBStructure.MONTH + " TEXT, "
            + DBStructure.YEAR + " TEXT, " + DBStructure.IMAGEN + " TEXT, " + DBStructure.VIDEO + " TEXT, "
            + DBStructure.CALENDAR_ID + " INTEGER)";
    private final static String CREATE_CALENDAR_TABLE = "create table " + DBStructure.CALENDAR_TABLE_NAME + "(IDCAL INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBStructure.NAME + " TEXT, " + DBStructure.EMAIL + " TEXT, " + DBStructure.FECHA_CREACION + " TEXT, "
            + DBStructure.COLOR + " TEXT, " + DBStructure.LETRA + " TEXT)";

    private final static String USER_CALENDARS =  "SELECT * FROM " + DBStructure.CALENDAR_TABLE_NAME + "WHERE " + DBStructure.EMAIL + " = ?";


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

    public void SaveEvent(Integer id, String event, Uri uri, String date, String month, String year,String video, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.CALENDAR_ID, id);
        contentValues.put(DBStructure.EVENT,event);
        if (uri != null) {
            contentValues.put(DBStructure.IMAGEN,uri.toString());
        }
        contentValues.put(DBStructure.DATE,date);
        contentValues.put(DBStructure.MONTH,month);
        contentValues.put(DBStructure.YEAR,year);
        contentValues.put(DBStructure.VIDEO,video);
        database.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues);

    }

    public void updateEvent(Integer id, String name, String video, Uri uri, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT, name);
        contentValues.put(DBStructure.VIDEO, video);
        contentValues.put(DBStructure.IMAGEN, uri.toString());
        String selection =  "ID = ?";
        String[] selectionArgs = { String.valueOf(id) };

        database.update(DBStructure.CALENDAR_TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void updateEvent(Integer id, String name, String video, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENT, name);
        contentValues.put(DBStructure.VIDEO, video);
        String selection =  "ID = ?";
        String[] selectionArgs = { String.valueOf(id) };

        database.update(DBStructure.EVENT_TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public int getEventId(String eventName, String date, SQLiteDatabase database) {
        int eventId = -1;
        Cursor cursor = database.rawQuery("SELECT ID FROM " + DBStructure.EVENT_TABLE_NAME + " WHERE " + DBStructure.EVENT + " = ? AND " + DBStructure.DATE + " = ?", new String[]{eventName, date});
        if (cursor.moveToFirst()) {
            eventId = cursor.getInt(cursor.getColumnIndex("ID")+0);
        }
        cursor.close();
        return eventId;
    }

    public Cursor ReadEvents (String date, Integer id, SQLiteDatabase database ){
        String [] Projections = {DBStructure.EVENT,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR, DBStructure.IMAGEN, DBStructure.VIDEO, DBStructure.CALENDAR_ID};
        String Selection = DBStructure.DATE + "=? and "+DBStructure.CALENDAR_ID+"=?";
        String [] SelectionArgs  = {date, String.valueOf(id)};

        return database.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);


    }

    public Cursor ReadEventsPerMonth (String month, String year, Integer id, @NonNull SQLiteDatabase database ){
        String [] Projections = {DBStructure.EVENT,DBStructure.DATE,DBStructure.MONTH,DBStructure.YEAR, DBStructure.IMAGEN, DBStructure.VIDEO};
        String Selection = DBStructure.MONTH + "=? and "+DBStructure.YEAR+"=? and "+DBStructure.CALENDAR_ID+"=?";
        String [] SelectionArgs  = {month,year, String.valueOf(id)};

        return database.query(DBStructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);

    }

    public void deleteEvent(Integer id, SQLiteDatabase database) {
        String selection =  "ID = ?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DBStructure.EVENT_TABLE_NAME, selection, selectionArgs);
    }



    public void SaveCalendar(String name, String email,String currentDate, String color, String letra,  SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.NAME,name);
        contentValues.put(DBStructure.EMAIL,email);
        contentValues.put(DBStructure.FECHA_CREACION,currentDate);
        contentValues.put(DBStructure.COLOR,color);
        contentValues.put(DBStructure.LETRA,letra);
        database.insert(DBStructure.CALENDAR_TABLE_NAME,null,contentValues);

    }

    public void updateCalendar(int id, String name, String color, String letra, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.NAME, name);
        contentValues.put(DBStructure.COLOR, color);
        contentValues.put(DBStructure.LETRA, letra);

        String selection =  DBStructure.CALENDAR_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        database.update(DBStructure.CALENDAR_TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void deleteCalendar(int id, SQLiteDatabase database) {
        String selection =  DBStructure.CALENDAR_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DBStructure.CALENDAR_TABLE_NAME, selection, selectionArgs);
    }
    public Cursor getCalendarsByUser(String email, SQLiteDatabase database) {
        String[] Projections = {
                DBStructure.NAME,
                DBStructure.EMAIL,
                DBStructure.FECHA_CREACION,
                DBStructure.COLOR,
                DBStructure.LETRA,
                DBStructure.CALENDAR_ID
        };
        String Selection = DBStructure.EMAIL + " = ?";
        String[] SelectionArgs = { email };

        return database.query(DBStructure.CALENDAR_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);

    }
    public Cursor getCalendarsByID(int id, SQLiteDatabase database) {
        String[] Projections = {
                DBStructure.CALENDAR_ID
        };
        String Selection = DBStructure.CALENDAR_ID +" = ?";
        String[] selectionArgs = { String.valueOf(id) };

        return database.query(DBStructure.CALENDAR_TABLE_NAME,Projections,Selection,selectionArgs,null,null,null);

    }







}
