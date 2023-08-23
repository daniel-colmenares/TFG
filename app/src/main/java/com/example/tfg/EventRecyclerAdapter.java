package com.example.tfg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tfg.remote.APIUtils;
import com.example.tfg.remote.Modelo;
import com.example.tfg.remote.PictogramService;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    PictogramService pictogramService;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Context context;
    private View.OnClickListener onItemClickListener;
    CustomCalendarView customCalendarView;
    ArrayList<Events> arrayList;
    Button buttonPicto, buttonGaleria;
    EditText filtroPicto;
    MainActivity activity;
    Boolean esAdmin;
    Uri uriImagen;

    CustomCalendarView activ;

    int posicion = -1;

    public EventRecyclerAdapter(Context context, ArrayList<Events> arrayList, ActivityResultLauncher<PickVisualMediaRequest> pickMedia, CustomCalendarView activ) {
        this.context = context;
        this.arrayList = arrayList;
        this.pickMedia = pickMedia;
        this.activ = activ;
    }

    public void manejarModificacionImagen(Uri uriImagen) {

        arrayList.get(posicion).setIMAGEN(uriImagen);
        this.uriImagen = uriImagen;
        notifyItemChanged(posicion, "imagen");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            this.onBindViewHolder(holder, position);
        } else {
            for (Object payload : payloads) {
                if (payload.equals("imagen")) {
                    holder.onEditButtonClick(position);
                    Events events = arrayList.get(position);
                    holder.editEvent.setText(events.EVENT);
                    holder.editVideo.setText(events.VIDEO);
                    Glide.with(context).load(uriImagen).into(holder.Imagen);
                }
            }
        }

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SharedPreferences prefs1 = context.getSharedPreferences("CalendarioUsuario", 0);
        esAdmin = prefs1.getBoolean("esAdmin", false);
        activity = (MainActivity) context;
        //customCalendarView = holder.itemView.findViewById(R.id.custom_calendar_view);
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
        holder.editEvent.setVisibility(View.GONE);
        holder.editVideo.setVisibility(View.GONE);
        holder.editImagen.setVisibility(View.GONE);
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
                        notifyDataSetChanged();
                        dialog.dismiss();
                        activ.SetUpCalendar();
                    }

                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                builder.setView(view);

            }
        });
        /*holder.editImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });*/
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
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        holder.editEvent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
        holder.editVideo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });


        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                holder.onEditButtonClick(position);
                holder.editEvent.setText(events.EVENT);
                holder.editVideo.setText(events.VIDEO);
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
        EditText editEvent, editVideo;
        Button Borrar, Edit, Confirm;
        Button editImagen;
        ImageView Imagen;
        private Uri currentImagenUri;
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
            currentImagenUri = uriImagen;




        }


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
                                    editEvent.setVisibility(View.VISIBLE);
                                    editVideo.setVisibility(View.VISIBLE);
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


        private void showPhotoSelectionDialog(Context context, int position) {
            editEvent.setVisibility(View.VISIBLE);
            editVideo.setVisibility(View.VISIBLE);
            final Dialog dialog1 = new Dialog(context);
            dialog1.setContentView(R.layout.dialog_foto);
            dialog1.setCancelable(true);
            filtroPicto = dialog1.findViewById(R.id.editTextPicto);
            buttonGaleria = dialog1.findViewById(R.id.buttonGaleria);
            buttonPicto = dialog1.findViewById(R.id.buttonPicto);
            buttonGaleria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    posicion = position;
                    pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                    //Imagen.setVisibility(View.VISIBLE);
                    dialog1.dismiss();
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
                            Toast.makeText(view.getContext(), "Cargando pictogramas, espere unos segundos...", Toast.LENGTH_LONG).show();
                            dialog1.dismiss();
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



        public void onEditButtonClick(int position) {
            // Mostrar los valores actuales en los EditText
            editEvent.setText(currentEventName);
            editVideo.setText(currentVideoUrl);
            //editImagen.setText(currentImagenUri.toString());
            // Alternar las vistas para mostrar los EditText y ocultar los TextView
            Event.setVisibility(View.GONE);
            Video.setVisibility(View.GONE);
            //Imagen.setVisibility(View.GONE);
            editEvent.setVisibility(View.VISIBLE);
            editVideo.setVisibility(View.VISIBLE);
            editImagen.setVisibility(View.VISIBLE);
            Edit.setVisibility(View.GONE);
            Borrar.setVisibility(View.GONE);
            editImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPhotoSelectionDialog(context, position);
                }
            });

        }

        public void onSaveButtonClick(Events events) {
            // Obtener los nuevos valores ingresados por el usuario
            String newEventName = editEvent.getText().toString();
            String newVideoUrl = editVideo.getText().toString();
            Uri newImagenUri = uriImagen;

            if (newEventName.isEmpty()){
                newEventName = currentEventName;
            }
            if (newVideoUrl.isEmpty()){
                newVideoUrl = currentVideoUrl;
            }
            /*if (newImagenUri.equals(Uri.EMPTY)){
                newImagenUri = currentImagenUri;
            }*/
            // Actualizar los TextView con los nuevos valores
            Event.setText(newEventName);
            Video.setText(newVideoUrl);
            Imagen.setImageURI(uriImagen);

            // Guardar los nuevos valores en las variables de instancia
            currentEventName = newEventName;
            currentVideoUrl = newVideoUrl;
            currentImagenUri = newImagenUri;

            DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
            // Borrar evento de la base de datos
            SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
            Integer eventId = dbOpenHelper.getEventId(events.getEVENT(), events.getDATE(), database);
            database.beginTransaction();
            try {
                dbOpenHelper.updateEvent(eventId,newEventName,newVideoUrl,newImagenUri, database);
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
