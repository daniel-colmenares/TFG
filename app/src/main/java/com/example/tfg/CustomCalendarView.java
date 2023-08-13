package com.example.tfg;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.example.tfg.model.Pictograma;
import com.example.tfg.remote.APIUtils;
import com.example.tfg.remote.Modelo;
import com.example.tfg.remote.PictogramService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomCalendarView extends LinearLayout{
    PictogramService pictogramService;
    ArrayList<Calendars> arrayList;
    DBOpenHelper dbOpenHelper;
    TextView personalizarTV;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    Button nextBtn, prevBtn, elegirCalendario, color_calendario, pdf,
            letra_calendario, buttonPicto, buttonGaleria, personalizar,
            confirmarPersonalizar, buttonsettings, buttonImagen;
    TextView CurrentDate;

    Calendar currentDate;
    GridView gridView;
    private static final int MAX_CALENDARDAYS=42;
    Calendar calendar = Calendar.getInstance(Locale.forLanguageTag("es-ES"));
    Context context;
    String cellColor, letraCal;

    TextView lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    Boolean esAdmin;
    Calendars calendars;
    List<Date> dates= new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",Locale.forLanguageTag("es-ES"));
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.forLanguageTag("es-ES"));
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.forLanguageTag("es-ES"));
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.forLanguageTag("es-ES"));
    MyGridAdapter myGridAdapter;
    EditText filtroPicto;
    AlertDialog alertDialog;
    ImageView imageViewGal, imageViewPicto;

    RecyclerView recyclerView;

    Uri uriImagen;
    MainActivity activity;

    //Button changeColorButton = findViewById(R.id.color_calendario);
    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();

        activity = (MainActivity)context;
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = activity.registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri!=null) {
                int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                context.getContentResolver().takePersistableUriPermission(uri,flag);
                Glide.with(context).load(uri).into(imageViewGal);
                imageViewGal.setVisibility(View.VISIBLE);
                buttonPicto.setVisibility(View.GONE);
                buttonImagen.setVisibility(GONE);
                uriImagen = uri;
            }else {
                imageViewGal.setVisibility(GONE);
                Toast.makeText(activity, "Problema encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        Calendar dateCalendar = Calendar.getInstance();
        int currentDay = dateCalendar.get(Calendar.DAY_OF_WEEK);
        lunes = findViewById(R.id.lunes);
        martes = findViewById(R.id.martes);
        miercoles = findViewById(R.id.miercoles);
        jueves = findViewById(R.id.jueves);
        viernes = findViewById(R.id.viernes);
        sabado = findViewById(R.id.sabado);
        domingo = findViewById(R.id.domingo);

        if (currentDay==Calendar.MONDAY){
            lunes.setBackgroundColor(Color.parseColor("#8181F7"));

        }
        else if (currentDay==Calendar.TUESDAY){
            martes.setBackgroundColor(Color.parseColor("#8181F7"));

        }
        else if (currentDay==Calendar.WEDNESDAY){
            miercoles.setBackgroundColor(Color.parseColor("#8181F7"));

        }
        else if (currentDay==Calendar.THURSDAY){
            jueves.setBackgroundColor(Color.parseColor("#8181F7"));

        }
        else if (currentDay==Calendar.FRIDAY){
            viernes.setBackgroundColor(Color.parseColor("#8181F7"));

        }
        else if (currentDay==Calendar.SATURDAY){
            sabado.setBackgroundColor(Color.parseColor("#8181F7"));

        }
        else if (currentDay==Calendar.SUNDAY){
            domingo.setBackgroundColor(Color.parseColor("#8181F7"));

        }

        SharedPreferences prefs = activity.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        String nombre = prefs.getString("name","");
        TextView nombreCalView = findViewById(R.id.NombreCalView);
        //Calendar calendar = Calendar.getInstance();
        String email = prefs.getString("email", "");
        String fecha = prefs.getString("fechacreacion","");
        Integer idCal = prefs.getInt("ID", 99);
        cellColor = prefs.getString("cellColor", "#5FB404");
        letraCal = prefs.getString("letraCal", "");
        calendars = new Calendars(nombre, email,fecha, idCal, cellColor, letraCal);
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        dbOpenHelper.getCalendarsByID(calendars.getID(),database);
        nombreCalView.setText("CALENDARIO "+nombre);

        pictogramService = APIUtils.getPictoService();


        buttonsettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.settings_calendario);
                dialog.setCancelable(true);
                personalizarTV = dialog.findViewById(R.id.textView10);
                // Obtener referencias a las vistas dentro del diálogo
                elegirCalendario = dialog.findViewById(R.id.buttonCambiarCalendario);
                pdf = dialog.findViewById(R.id.pdf);
                personalizar = dialog.findViewById(R.id.personalizar);
                if (!esAdmin){
                    personalizarTV.setVisibility(GONE);
                }
                elegirCalendario.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, SelectCalendarActivity.class);

                        // Opcional: Puedes pasar datos adicionales a la nueva actividad usando putExtra()
                        SharedPreferences prefs = activity.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        String email = prefs.getString("email", "");
                        intent.putExtra("email", email);

                        // Iniciar la actividad SelectCalendarActivity
                        context.startActivity(intent);
                    }
                });
                pdf.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Environment.isExternalStorageManager()) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            activity.startActivity(intent);
                            return;
                        }
                        guardarComoPDF(activity.getWindow().getDecorView().getRootView());
                        Toast.makeText(context, "Guardado como pdf", Toast.LENGTH_SHORT).show();
                    }
                });
                if (!esAdmin){
                    personalizar.setVisibility(View.GONE);
                }
                personalizar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("esAdmin", esAdmin);
                        editor.apply();
                        if (esAdmin) {
                            Intent intent = new Intent(context, PersonalizarCalendario.class);
                            intent.putExtra("ID",calendars.getID());
                            context.startActivity(intent);
                        }
                    }
                });
                dialog.show();
            }
        });

        prevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,-1);
                SetUpCalendar();
            }
        });
        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalendar();
            }
        });
        SharedPreferences prefs1 = activity.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        esAdmin = prefs1.getBoolean("esAdmin", false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Calendars calendars = arrayList.get(position);
                if (!esAdmin){
                    return;
                }
                if (dates.get(position) == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
                EditText EventName = addView.findViewById(R.id.eventname);
                EditText EventVideo = addView.findViewById(R.id.eventvideo);
                buttonImagen = addView.findViewById(R.id.buttonFoto);
                imageViewGal = addView.findViewById(R.id.imageViewGaleria);
                imageViewPicto = addView.findViewById(R.id.imageViewPicto);
                Button AddEvent = addView.findViewById(R.id.addevent);
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                TextView fecha = addView.findViewById(R.id.mostrarfecha);
                fecha.setText(date);
                String date1 = eventDateFormat.format(dates.get(position));
                List<Events> events = CollectEventByDate(date1,idCal);
                if(events.size()==1 && events.get(0).getIMAGEN()!=null){
                    buttonImagen.setVisibility(View.GONE);
                }
                if(events.size()==2){
                    Toast.makeText(activity, "No se puede añadir mas de 2 eventos por dia", Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonImagen.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog1 = new Dialog(view.getContext());
                        dialog1.setContentView(R.layout.dialog_foto);
                        dialog1.setCancelable(true);
                        filtroPicto = dialog1.findViewById(R.id.editTextPicto);
                        buttonGaleria = dialog1.findViewById(R.id.buttonGaleria);
                        buttonPicto = dialog1.findViewById(R.id.buttonPicto);
                        buttonGaleria.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
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
                                    }

                                    @Override
                                    public void onLost(Network network) {
                                        // No hay conexión WiFi, mostrar mensaje de error
                                        Toast.makeText(view.getContext(), "Error: Para usar pictogramas, por favor conectate a una WiFi", Toast.LENGTH_SHORT).show();
                                    }
                                };
                                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });
                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (EventName.getText().toString().equals("") && uriImagen==null && EventVideo.getText().toString().equals("")){
                            Toast.makeText(activity, "Un evento no puede estar vacío", Toast.LENGTH_SHORT).show();
                        }else {
                            if (uriImagen==null){
                                SaveEvent(idCal,EventName.getText().toString(), null, date, month, year, EventVideo.getText().toString());
                            }else {
                                SaveEvent(idCal,EventName.getText().toString(), uriImagen, date, month, year, EventVideo.getText().toString());
                            }
                        }
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }

        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dates.get(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(),
                        CollectEventByDate(date, idCal));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();
                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

    }


    private void mostrarDialogoLista(Context context) {

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
                                Glide.with(context).load("https://api.arasaac.org/api/pictograms/"+listaId.get(position)).into(imageViewPicto);
                                uriImagen = Uri.parse("https://api.arasaac.org/api/pictograms/"+listaId.get(position));
                                imageViewPicto.setVisibility(View.VISIBLE);
                                buttonGaleria.setVisibility(View.GONE);
                                buttonImagen.setVisibility(GONE);
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




    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                guardarComoPDF(gridView);
            } else {
                Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void guardarComoPDF(View view) {
        // Captura la pantalla actual
        View rootView = view;
        //findViewById(android.R.id.content)
        Bitmap bitmap = getBitmapFromView(rootView);
        String nombreCalendario = calendars.getNAME();
        // Crea un archivo PDF y escribe la imagen en él
        try {
            File pdfFile = new File(Environment.getExternalStorageDirectory(),nombreCalendario + " calendario.pdf");
            FileOutputStream outputStream = new FileOutputStream(pdfFile);

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Image image = Image.getInstance(byteArray);
            image.scaleToFit(document.getPageSize());
            document.add(image);
            document.close();

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



    private ArrayList<Events> CollectEventByDate(String date, Integer id){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date,id,database);
        while ( cursor.moveToNext()){
             //Integer Id = cursor.getInt(cursor.getColumnIndex("ID")+0);
             String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT)+0);
             String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE)+0);
             String Month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH)+0);
             String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR)+0);
             //Uri Image = Uri.parse(cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0));
            String imageString = cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0);
            //Integer idCal = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID)+0);
            Uri Image = null;
            if (imageString != null) {
                Image = Uri.parse(imageString);
            }
             String Video = cursor.getString(cursor.getColumnIndex(DBStructure.VIDEO)+0);
            //Events events = new Events(event,Date,Month, Year, Image, Video,idCal);
            Events events = new Events(event,Date,Month, Year, Image, Video);
            arrayList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    private Events getEventById(String date,Integer idC, int eventId) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date,idC, database);

        Events event = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID")+0);
            if (id == eventId) {
                String eventStr = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT)+0);
                String dateStr = cursor.getString(cursor.getColumnIndex(DBStructure.DATE)+0);
                String monthStr = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH)+0);
                String yearStr = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR)+0);
                String imageStr = cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0);
                Uri imageUri = (imageStr == null) ? null : Uri.parse(imageStr);
                String videoStr = cursor.getString(cursor.getColumnIndex(DBStructure.VIDEO)+0);
                //Integer idCal = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID)+0);

                //event = new Events(eventStr, dateStr, monthStr, yearStr, imageUri, videoStr, idCal);
                event = new Events(eventStr, dateStr, monthStr, yearStr, imageUri, videoStr);
                break;
            }
        }

        cursor.close();
        dbOpenHelper.close();

        return event;
    }

    private void SaveEvent(Integer id, String event, Uri uri, String date, String month, String year, String video){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(id, event, uri, date, month, year, video, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Evento Guardado", Toast.LENGTH_SHORT).show();
    }

    private void UpdateCalendar(int id, String name, String color, String letra){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.updateCalendar(id, name, color, letra, database);
        dbOpenHelper.close();
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout,this);
        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn = view.findViewById(R.id.previousBtn);
        CurrentDate=view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridView);
        buttonsettings = view.findViewById(R.id.buttonsettings);
    }

    public void SetUpCalendar() {
        SharedPreferences prefs = activity.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        Integer id = prefs.getInt("ID", 99);
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

// Establecer el primer día de la semana en lunes (2)
        monthCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - monthCalendar.getFirstDayOfWeek();
        if (FirstDayOfMonth < 0) {
            // Si el primer día de la semana es domingo, ajustar para que sea lunes
            FirstDayOfMonth += 7;
        }
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);

        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()), id);

        while (dates.size() < MAX_CALENDARDAYS) {
            Date dateToAdd = monthCalendar.getTime();
            if (monthCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                // agregar la fecha a la lista solo si pertenece al mes actual
                dates.add(dateToAdd);
            } else {
                // agregar un valor nulo a la lista para las fechas que pertenecen a otros meses
                dates.add(null);
            }
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //SharedPreferences prefs = activity.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        String nombre = prefs.getString("name","");
        TextView nombreCalView = findViewById(R.id.NombreCalView);
        //Integer id = prefs.getInt("ID", 99);
        cellColor = prefs.getString("cellColor", "#5FB404");
        letraCal = prefs.getString("letraCal", "");
        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList, cellColor, letraCal);
        gridView.setAdapter(myGridAdapter);
        UpdateCalendar(id,nombre,cellColor, letraCal);
        nombreCalView.setText(nombre);
    }

    private void CollectEventsPerMonth ( String month, String year,Integer id) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsPerMonth(month, year,id, database);
        while (cursor.moveToNext()) {
            //Integer Id = cursor.getInt(cursor.getColumnIndex("ID")+0);
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT)+0);
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE)+0);
            String Month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH)+0);
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR)+0);
            Uri Image = null;
            String imagenString = cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0);
            if (imagenString != null) {
                Image = Uri.parse(imagenString);
            }
            //Uri Image = Uri.parse(cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0));
            String Video = cursor.getString(cursor.getColumnIndex(DBStructure.VIDEO)+0);
            //Integer idCal = cursor.getInt(cursor.getColumnIndex(DBStructure.CALENDAR_ID)+1);
            //Events events = new Events(event, date, Month, Year, Image, Video, idCal);
            Events events = new Events(event, date, Month, Year, Image, Video);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }
}
