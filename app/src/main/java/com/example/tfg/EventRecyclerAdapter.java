package com.example.tfg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;
import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    private View.OnClickListener onItemClickListener;
    ArrayList<Events> arrayList;

    public EventRecyclerAdapter(Context context, ArrayList<Events> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Events events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.DateText.setText(events.getDATE());
        Glide.with(context).load(events.getIMAGEN()).into(holder.Imagen);
        holder.Video.setText(events.getVIDEO());


        holder.Borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Está seguro de que desea eliminar este evento?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Obtener objeto Events correspondiente
                        Events eventToDelete = arrayList.get(position);

                        // Obtener id del evento a borrar
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        // Borrar evento de la base de datos
                        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
                        Integer eventId = dbOpenHelper.getEventId(eventToDelete.getEVENT(), eventToDelete.getDATE(), database);
                        database.beginTransaction();
                        try {
                            dbOpenHelper.deleteEvent(eventId, database);
                            database.setTransactionSuccessful();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            database.endTransaction();
                        }
                        database.close();

// Borrar evento de la lista y actualizar la vista
                        arrayList.remove(position);

                    }

                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = events.VIDEO;
                try {
                    URL videoUrl = new URL(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                } catch (MalformedURLException e) {
                    // Mostrar un mensaje si la URL no es válida
                    Toast.makeText(context, "URL no válida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        holder.Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    /*public void setOnItemClickListener(View.OnClickListener listener) {
        this.onItemClickListener = listener;
    }*/

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView DateText, Event, Video;
        EditText editEvent, editVideo;
        Button Borrar, Edit, Confirm;
        ImageView Imagen;

        private String currentEventName;
        private String currentVideoUrl;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            DateText = itemView.findViewById(R.id.evendate);
            Event = itemView.findViewById(R.id.eventname);
            Imagen = itemView.findViewById(R.id.imagenEvento);
            Video = itemView.findViewById(R.id.videourl);
            Borrar = itemView.findViewById(R.id.borrarevento);
            Edit = itemView.findViewById(R.id.edit);
            Confirm = itemView.findViewById(R.id.confirm);
            editEvent = itemView.findViewById(R.id.editEventName);
            editVideo = itemView.findViewById(R.id.editVideoUrl);

            currentEventName = Event.getText().toString();
            currentVideoUrl = Video.getText().toString();

        }

        public void onEditButtonClick(View view) {
            ViewSwitcher nameViewSwitcher = view.findViewById(R.id.nameViewSwitcher);
            nameViewSwitcher.showNext();

            ViewSwitcher urlViewSwitcher = view.findViewById(R.id.urlViewSwitcher);
            urlViewSwitcher.showNext();

            editEvent.setText(currentEventName);
            editVideo.setText(currentVideoUrl);
        }

        public void onSaveButtonClick(View view) {
            // Obtener los nuevos valores ingresados por el usuario
            String newEventName = editEvent.getText().toString();
            String newVideoUrl = editVideo.getText().toString();

            // Actualizar los TextView con los nuevos valores
            Event.setText(newEventName);
            Video.setText(newVideoUrl);

            // Guardar los nuevos valores en las variables de instancia
            currentEventName = newEventName;
            currentVideoUrl = newVideoUrl;

            // Alternar nuevamente la vista para ocultar los EditText
            ViewSwitcher nameViewSwitcher = view.findViewById(R.id.nameViewSwitcher);
            nameViewSwitcher.showNext();

            ViewSwitcher urlViewSwitcher = view.findViewById(R.id.urlViewSwitcher);
            urlViewSwitcher.showNext();
        }

    }
}
