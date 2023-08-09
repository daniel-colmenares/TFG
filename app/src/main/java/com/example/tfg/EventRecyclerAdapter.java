package com.example.tfg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.tfg.remote.APIUtils;
import com.example.tfg.remote.Modelo;
import com.example.tfg.remote.PictogramService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    EditText editEvent, editVideo;
    Button editImagen;
    PictogramService pictogramService;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Context context;
    private View.OnClickListener onItemClickListener;
    ArrayList<Events> arrayList;
    Button buttonPicto, buttonGaleria;
    EditText filtroPicto;
    MainActivity activity;
    Boolean esAdmin;
    Uri uriImagen;

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
        SharedPreferences prefs1 = context.getSharedPreferences("CalendarioUsuario", 0);
        esAdmin = prefs1.getBoolean("esAdmin", false);
        Events events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.DateText.setText(events.getDATE());
        Glide.with(context).load(events.getIMAGEN()).into(holder.Imagen);
        holder.Video.setText(events.getVIDEO());
        if (!esAdmin){
            holder.Edit.setVisibility(View.GONE);
            holder.Borrar.setVisibility(View.GONE);
            holder.Confirm.setVisibility(View.GONE);
        }
        editEvent.setVisibility(View.GONE);
        editVideo.setVisibility(View.GONE);
        editImagen.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editEvent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });


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
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                holder.onEditButtonClick();
                editEvent.setText(events.EVENT);
                editVideo.setText(events.VIDEO);

            }
        });
        holder.Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Events eventToUpdate = arrayList.get(position);
                holder.onSaveButtonClick(eventToUpdate);
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
            editImagen = itemView.findViewById(R.id.editImagen);
            currentEventName = Event.getText().toString();
            currentVideoUrl = Video.getText().toString();
            activity = (MainActivity) context;
            /*pickMedia = activity.registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri!=null) {
                    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    context.getContentResolver().takePersistableUriPermission(uri,flag);
                    Glide.with(context).load(uri).into(Imagen);
                    Imagen.setVisibility(View.VISIBLE);
                    buttonPicto.setVisibility(View.GONE);
                    uriImagen = uri;
                }else {
                    Toast.makeText(activity, "Problema encontrado", Toast.LENGTH_SHORT).show();
                }
            });
            */


        }

        /*
        private void mostrarDialogoLista(Context context) {
            pictogramService = APIUtils.getPictoService();
            Call<List<Modelo>> call;
            if(filtroPicto.getText().toString().equals("")){
                call = pictogramService.getPictos();
            }
            else{
                call = pictogramService.getByFilter(filtroPicto.getText().toString());
            }
            call.enqueue(new Callback<List<Modelo>>() {
                @Override
                public void onResponse(Call<List<Modelo>> call, Response<List<Modelo>> response) {
                    if(response.isSuccessful()){
                        try {
                            int max = response.body().size()-1;
                            int min = 30;
                            int result = (int)(Math.random()*(max-min+1)+min);
                            List<Modelo> listaModelo = response.body().subList(result-20,result);
                            assert listaModelo != null;
                            List<String> listaCadenas = new ArrayList<>();
                            List<Integer> listaId = new ArrayList<>();

                            for (Modelo item : listaModelo) {
                                listaCadenas.add(item.getKeywords().get(0).getKeyword());
                                listaId.add(item.getId());
                            }

                            // Crear el diálogo
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Lista de pictogramas");
                            builder.setItems(listaCadenas.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int position) {
                                    Glide.with(context).load("https://api.arasaac.org/api/pictograms/"+listaId.get(position)).into(Imagen);
                                    uriImagen = Uri.parse("https://api.arasaac.org/api/pictograms/"+listaId.get(position));
                                    Imagen.setVisibility(View.VISIBLE);
                                    buttonGaleria.setVisibility(View.GONE);
                                }
                            });
                            // Mostrar el diálogo
                            builder.create().show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Modelo>> call, Throwable t) {
                    Log.d("fallo servidor", "error desconocido");
                }
            });
        }
        */

        /*private void showPhotoSelectionDialog(Context context) {
            final Dialog dialog1 = new Dialog(context);
            dialog1.setContentView(R.layout.dialog_foto);
            dialog1.setCancelable(true);
            filtroPicto = dialog1.findViewById(R.id.editTextPicto);
            buttonGaleria = dialog1.findViewById(R.id.buttonGaleria);
            buttonPicto = dialog1.findViewById(R.id.buttonPicto);
            buttonGaleria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());

                }
            });
            buttonPicto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Verificar la disponibilidad de conexión WiFi
                    ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkRequest networkRequest = new NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .build();

                    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            // Hay conexión WiFi, mostrar el diálogo de la lista
                            mostrarDialogoLista(view.getContext());
                        }

                        @Override
                        public void onLost(Network network) {
                            // No hay conexión WiFi, mostrar mensaje de error
                            Toast.makeText(view.getContext(), "Error: Para usar pictogramas, por favor conectate a una WiFi", Toast.LENGTH_SHORT).show();
                        }
                    };

                    connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
                }
            });
            dialog1.show();
        }
        */

        public void onEditButtonClick() {
            // Mostrar los valores actuales en los EditText
            editEvent.setText(currentEventName);
            editVideo.setText(currentVideoUrl);

            // Alternar las vistas para mostrar los EditText y ocultar los TextView
            Event.setVisibility(View.GONE);
            Video.setVisibility(View.GONE);
            Imagen.setVisibility(View.GONE);
            editEvent.setVisibility(View.VISIBLE);
            editVideo.setVisibility(View.VISIBLE);
            editImagen.setVisibility(View.VISIBLE);
            Edit.setVisibility(View.GONE);
            Borrar.setVisibility(View.GONE);
            //showPhotoSelectionDialog(context);
        }

        public void onSaveButtonClick(Events events) {
            // Obtener los nuevos valores ingresados por el usuario
            String newEventName = editEvent.getText().toString();
            String newVideoUrl = editVideo.getText().toString();

            if (newEventName.isEmpty()){
                newEventName = currentEventName;
            }
            if (newVideoUrl.isEmpty()){
                newVideoUrl = currentVideoUrl;
            }
            // Actualizar los TextView con los nuevos valores
            Event.setText(newEventName);
            Video.setText(newVideoUrl);

            // Guardar los nuevos valores en las variables de instancia
            currentEventName = newEventName;
            currentVideoUrl = newVideoUrl;

            DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
            // Borrar evento de la base de datos
            SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
            Integer eventId = dbOpenHelper.getEventId(events.getEVENT(), events.getDATE(), database);
            database.beginTransaction();
            try {
                dbOpenHelper.updateEvent(eventId,newEventName,newVideoUrl, database);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }
            database.close();
            // Alternar nuevamente la vista para ocultar los EditText
            editEvent.setVisibility(View.GONE);
            editVideo.setVisibility(View.GONE);
            editImagen.setVisibility(View.GONE);
            Event.setVisibility(View.VISIBLE);
            Video.setVisibility(View.VISIBLE);
            Imagen.setVisibility(View.VISIBLE);
            Edit.setVisibility(View.VISIBLE);
            Borrar.setVisibility(View.VISIBLE);

        }

    }
}
