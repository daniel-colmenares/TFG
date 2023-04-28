package com.example.tfg;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import android.os.Build;
import android.view.Window;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CustomCalendarView extends LinearLayout{
    DBOpenHelper dbOpenHelper;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    Events events;
    Button nextBtn, prevBtn, elegirCalendario, color_calendario, pdf;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDARDAYS=42;
    Calendar calendar = Calendar.getInstance(Locale.forLanguageTag("es-ES"));
    Context context;
    String cellColor;
    Calendars calendars;
    List<Date> dates= new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",Locale.forLanguageTag("es-ES"));
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",Locale.forLanguageTag("es-ES"));
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",Locale.forLanguageTag("es-ES"));
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.forLanguageTag("es-ES"));
    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    ImageView image;

    Uri uriImagen;

    //Button changeColorButton = findViewById(R.id.color_calendario);
    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();

        MainActivity activity = (MainActivity)context;
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = activity.registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri!=null) {
                Glide.with(context).load(uri).into(image);
                uriImagen = uri;
            }else {
                Toast.makeText(activity, "Problema encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        //DUDAAAAAAAAAAAA

        SharedPreferences prefs = activity.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String fecha = prefs.getString("fechacreacion","");
        Integer id = prefs.getInt("ID", 0);
        cellColor = prefs.getString("cellColor", "#cccccc");
        calendars = new Calendars("", email,fecha, id);
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        dbOpenHelper.getCalendarsByID(calendars.getID(),database);




        elegirCalendario.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout,null);
                EditText EventName = addView.findViewById(R.id.eventname);//eventsid
                EditText EventVideo = addView.findViewById(R.id.eventvideo);
                image = addView.findViewById(R.id.image);
                Button AddEvent = addView.findViewById(R.id.addevent);
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year= yearFormat.format(dates.get(position));
                TextView fecha = addView.findViewById(R.id.mostrarfecha);
                fecha.setText(date);
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                    }
                });
                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SaveEvent(EventName.getText().toString(),uriImagen,date,month,year,EventVideo.getText().toString());
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
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout,null);
                View borrarView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout,null);
                Button borrarevento = borrarView.findViewById(R.id.borrarevento);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(),
                        CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();

                borrarevento.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("¿Está seguro de que desea eliminar este evento?");
                        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Obtener objeto Events correspondiente
                                Events eventToDelete = eventsList.get(which);

                                // Obtener id del evento a borrar
                                int eventId = eventToDelete.getID();

                                // Borrar evento de la base de datos
                                SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
                                dbOpenHelper.deleteEvent(eventId, database);
                                database.close();

                                // Borrar evento de la lista y actualizar la vista
                                eventsList.remove(which);
                                //notifyItemRemoved(position);
                                //notifyItemRangeChanged(position, eventsList.size());
                            }

                        });
                        builder.setNegativeButton("No", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });


                return true;
            }
        });
        color_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(context).inflate(R.layout.listacolores_calendario,null);
                Button azul = addView.findViewById(R.id.button_azul);
                Button rojo = addView.findViewById(R.id.button_rojo);
                Button amarillo = addView.findViewById(R.id.button_amarillo);
                Button morado = addView.findViewById(R.id.button_morado);
                Button rosa = addView.findViewById(R.id.button_rosa);
                Button verde = addView.findViewById(R.id.button_verde);

                azul.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#8181F7";
                        SetUpCalendar();
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                rojo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#FA5858";
                        SetUpCalendar();
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                verde.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#58FA58";
                        SetUpCalendar();
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                amarillo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#F4FA58";
                        SetUpCalendar();
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                morado.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#D358F7";
                        SetUpCalendar();
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                rosa.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#F5A9D0";
                        SetUpCalendar();
                        SharedPreferences prefs = context.getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("cellColor", cellColor);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        pdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    } else {
                        //guardarComoPDF(gridView);
                    }
                }
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
        View rootView = findViewById(android.R.id.content);
        Bitmap bitmap = getBitmapFromView(rootView);

        // Crea un archivo PDF y escribe la imagen en él
        try {
            File pdfFile = new File(Environment.getExternalStorageDirectory(), "captura.pdf");
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
        view = gridView;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



    private ArrayList<Events> CollectEventByDate(String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date,database);
        while ( cursor.moveToNext()){
             //Integer Id = cursor.getInt(cursor.getColumnIndex("ID")+0);
             String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT)+0);
             String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE)+0);
             String Month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH)+0);
             String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR)+0);
             //Uri Image = Uri.parse(cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0));
            String imageString = cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0);
            Uri Image = null;
            if (imageString != null) {
                Image = Uri.parse(imageString);
            }
             String Video = cursor.getString(cursor.getColumnIndex(DBStructure.VIDEO)+0);
            Events events = new Events(event,Date,Month, Year, Image, Video);
            arrayList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    private Events getEventById(String date, int eventId) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);

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

                event = new Events(eventStr, dateStr, monthStr, yearStr, imageUri, videoStr);
                break;
            }
        }

        cursor.close();
        dbOpenHelper.close();

        return event;
    }

    private void SaveEvent(String event, Uri uri, String date, String month, String year, String video){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, uri, date, month, year, video, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Evento Guardado", Toast.LENGTH_SHORT).show();
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
        elegirCalendario = view.findViewById(R.id.buttonCambiarCalendario);
        color_calendario = view.findViewById(R.id.color_calendario);
        pdf = view.findViewById(R.id.pdf);
        //defaultColor = ContextCompat.getColor(context, droidninja.filepicker.R.color.colorPrimary);
    }



    private void SetUpCalendar(){
        String currentDate= dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while(dates.size() < MAX_CALENDARDAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
            myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList, cellColor);
        }

        //CUIDADO ARRIBA
        gridView.setAdapter(myGridAdapter);
    }


    /*
    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();

        // Obtener el primer día del mes actual y el número total de días del mes
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Obtener el día de la semana en que comienza el mes actual
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        // Añadir fechas del mes anterior si el primer día del mes no es domingo
        if (firstDayOfMonth > 0) {
            monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
            for (int i = 0; i < firstDayOfMonth; i++) {
                dates.add(null); // Agregar valores nulos
            }
        }

        // Añadir fechas del mes actual
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));
        for (int i = 1; i <= daysInMonth; i++) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Añadir fechas del mes siguiente si el último día del mes no es sábado
        int lastDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK);
        if (lastDayOfMonth != 1) {
            for (int i = lastDayOfMonth; i <= 7; i++) {
                dates.add(null); // Agregar valores nulos
            }
        }

        // Asegurar que el número total de elementos en la lista sea igual a MAX_CALENDAR_DAYS
        while (dates.size() < MAX_CALENDARDAYS) {
            dates.add(null); // Agregar valores nulos
        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList, cellColor);
        gridView.setAdapter(myGridAdapter);
    }*/

    private void CollectEventsPerMonth ( String month, String year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsPerMonth(month, year, database);
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
            Events events = new Events(event, date, Month, Year, Image, Video);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }
}
