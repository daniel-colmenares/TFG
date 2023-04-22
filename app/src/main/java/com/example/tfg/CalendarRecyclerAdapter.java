package com.example.tfg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;
import java.util.ArrayList;

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Calendars> arrayList;
    DBOpenHelper dbOpenHelper;



    public CalendarRecyclerAdapter(Context context, ArrayList<Calendars> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_calendarlist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Calendars calendars = arrayList.get(position);
        holder.Calendar.setText(calendars.getNAME());
        holder.verCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("email",calendars.getEMAIL());
                intent.putExtra("ID",calendars.getID());
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
                        //DUDA
                        dbOpenHelper.getCalendarsByUser(calendars.getEMAIL(),database);
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
        TextView Calendar;
        Button verCalendario;
        Button eliminarCalendario;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            Calendar = itemView.findViewById(R.id.calendarname);
            verCalendario = itemView.findViewById(R.id.vercalendario);
            eliminarCalendario = itemView.findViewById(R.id.eliminarcalendario);
        }

    }
}
