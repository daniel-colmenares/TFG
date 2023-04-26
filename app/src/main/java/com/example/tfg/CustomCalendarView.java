package com.example.tfg;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
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

//import yuku.ambilwarna.AmbilWarnaDialog;
//import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

public class CustomCalendarView extends LinearLayout{
    DBOpenHelper dbOpenHelper;
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
        Integer id = prefs.getInt("ID", 0);
        cellColor = prefs.getString("cellColor", "#cccccc");
        calendars = new Calendars("", email, id);
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
                image = addView.findViewById(R.id.image);
                Button AddEvent = addView.findViewById(R.id.addevent);
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year= yearFormat.format(dates.get(position));
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                    }
                });
                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SaveEvent(EventName.getText().toString(),uriImagen,date,month,year);
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
                azul.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cellColor = "#0000ff";
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
                        cellColor = "#ff0000";
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
                //Abre un cuadro de diálogo que permite al usuario seleccionar un color
                //ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
                //colorPickerDialog.show(activity.getSupportFragmentManager(), "colorPicker");
                //openColorPicker();
            }
        });
        pdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarComoPDF(view);
            }
        });

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
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    /*public void setGridAdapter(List<Date> dayValueInCells, Calendar mCal, List<Events> mEvents, FragmentActivity activity) {
        MyGridAdapter myGridAdapter = new MyGridAdapter(getContext(), dayValueInCells, mCal, mEvents, activity);
        gridView.setAdapter(myGridAdapter);
    }*/
    //int defaultColor;

    /*private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
            defaultColor=color;
            }
        });
        colorPicker.show();
    }*/

    private ArrayList<Events> CollectEventByDate(String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date,database);
        while ( cursor.moveToNext()){
             String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT)+0);
             String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE)+0);
             String Month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH)+0);
             String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR)+0);
             Uri Image = Uri.parse(cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0));
            Events events = new Events(event,Date,Month, Year, Image);
            arrayList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    private void SaveEvent(String event, Uri uri, String date, String month, String year){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, uri, date, month, year, database);
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
        }
        myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList, cellColor);
        //CUIDADO ARRIBA
        gridView.setAdapter(myGridAdapter);
    }

    private void CollectEventsPerMonth ( String month, String year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsPerMonth(month, year, database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT)+0);
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE)+0);
            String Month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH)+0);
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR)+0);
            Uri Image = Uri.parse(cursor.getString(cursor.getColumnIndex(DBStructure.IMAGEN)+0));
            Events events = new Events(event, date, Month, Year, Image);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }
}
