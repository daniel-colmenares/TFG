package com.example.tfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Calendars> arrayList;
    DBOpenHelper dbOpenHelper;
    private ArrayList<Calendars> arrayListFull;

    Boolean esAdmin;

    private CalendarRecyclerAdapter calendarRecyclerAdapter;



    public CalendarRecyclerAdapter(Context context, ArrayList<Calendars> arrayList, Boolean esAdmin) {
        this.context = context;
        this.arrayList = arrayList;
        this.esAdmin = esAdmin;
        this.arrayListFull = new ArrayList<>(arrayList); // Copia la lista original
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_calendarlist,parent,false);
        return new MyViewHolder(view);
    }

    public void filterList(ArrayList<Calendars> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Calendars calendars = arrayList.get(position);
        holder.Calendar.setText(calendars.getNAME());
        holder.fechaCalendario.setText(calendars.getFECHA().toString());
        if (!esAdmin){
            holder.eliminarCalendario.setVisibility(View.INVISIBLE);
        }
        //VISIBLE
        holder.verCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("name",calendars.getNAME());
                editor.putString("email", calendars.getEMAIL());
                editor.putInt("ID", calendars.getID());
                editor.putString("cellColor",calendars.getCOLOR());
                editor.putString("letraCal",calendars.getLETRA());
                editor.apply();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("name",calendars.getNAME());
                intent.putExtra("email",calendars.getEMAIL());
                intent.putExtra("ID",calendars.getID());
                intent.putExtra("color",calendars.getCOLOR());
                intent.putExtra("letraCal",calendars.getLETRA());
                context.startActivity(intent);

            }
        });
        holder.eliminarCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Borrar calendario " + calendars.getNAME());
                builder.setMessage("Seguro?");

// Botón "OK"
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbOpenHelper = new DBOpenHelper(context);
                        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
                        Integer Id = calendars.getID();
                        dbOpenHelper.deleteCalendar(Id,database);

                        /*arrayList = new ArrayList<>();
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
                        dbOpenHelper.close();*/
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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Calendar, fechaCalendario;
        Button verCalendario;
        Button eliminarCalendario;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            Calendar = itemView.findViewById(R.id.calendarname);
            fechaCalendario = itemView.findViewById(R.id.fechacalendario);
            verCalendario = itemView.findViewById(R.id.vercalendario);
            eliminarCalendario = itemView.findViewById(R.id.eliminarcalendario);
        }

    }
}
