package com.example.tfg;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    Calendars calendars;
    List<Events> events;
    String cellColor, fuenteLetra;
    DBOpenHelper dbOpenHelper;
    LayoutInflater inflater;
    TextView lunes, martes, miercoles, jueves, viernes, sabado, domingo, nombreCal;
    private FragmentActivity mActivity;



    /*public MyGridAdapter(@NonNull Context context,  List<Date> dates, Calendar currentDate, List<Events> events, FragmentActivity activity) {
        super(context, R.layout.single_cell_layout);
        this.dates=dates;
        this.currentDate=currentDate;
        this.events=events;
        this.mActivity = activity;
        inflater = LayoutInflater.from(context);
    }*/

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events, String cellColor, String fuenteLetra) {
        super(context, R.layout.single_cell_layout);
        this.dates=dates;
        this.currentDate=currentDate;
        this.events=events;
        this.cellColor=cellColor;
        this.fuenteLetra=fuenteLetra;
        inflater = LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);

        Calendar dateCalendar = Calendar.getInstance();
        if (monthDate != null) { // verifica si monthDate no es nulo
            dateCalendar.setTime(monthDate);
        }else {
            monthDate = new Date(0);
        }
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentDay = currentDate.get(Calendar.DAY_OF_WEEK);


        View view = convertView;
        if (view==null){
            view = inflater.inflate(R.layout.single_cell_layout,parent,false);
        }
        //view.setBackgroundColor(Color.parseColor(cellColor));
        //view.setBackgroundResource(R.drawable.cell_border);
        if (Objects.equals(cellColor, "#04B404")){
            view.setBackgroundResource(R.drawable.cell_border_verde);
        }
        if (Objects.equals(cellColor, "#8181F7")){
            view.setBackgroundResource(R.drawable.cell_border_azul);
        }
        if (Objects.equals(cellColor, "#D358F7")){
            view.setBackgroundResource(R.drawable.cell_border_morado);
        }
        if (Objects.equals(cellColor, "#FA5858")){
            view.setBackgroundResource(R.drawable.cell_border_rojo);
        }
        if (Objects.equals(cellColor, "#F5A9D0")){
            view.setBackgroundResource(R.drawable.cell_border_rosa);
        }
        if (Objects.equals(cellColor, "#D7DF01")){
            view.setBackgroundResource(R.drawable.cell_border_amarillo);
        }





        SharedPreferences prefs = view.getContext().getSharedPreferences("CalendarioUsuario", MODE_PRIVATE);
        Integer idCal = prefs.getInt("ID", 99);

        TextView Day_Number = view.findViewById(R.id.calendar_day);
        TextView Event1 = view.findViewById(R.id.events_id);
        //TextView Link1 = view.findViewById(R.id.link1);
        TextView Event2 = view.findViewById(R.id.evento2);
        TextView url1 = view.findViewById(R.id.textView13);
        TextView url2 = view.findViewById(R.id.textView14);
        //TextView Link2 = view.findViewById(R.id.link2);
        ImageView EventImage1 = view.findViewById(R.id.imagenEvento1);
        ImageView EventImage2 = view.findViewById(R.id.imagenEvento2);
        //TextView NombreCal = view.findViewById(R.id.NombreCalView);
        if (monthDate.getTime()==0){
            Day_Number.setText("-");
            view.setBackgroundColor(Color.parseColor("#9C9EA0"));
        }else {
            Day_Number.setText(String.valueOf(DayNo));
            if (fuenteLetra.equals("monospace")){
                Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                url1.setTypeface(typeface);
                url2.setTypeface(typeface);
               //NombreCal.setTypeface(typeface);
            }
            else if (fuenteLetra.equals("serif")){
                Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                url1.setTypeface(typeface);
                url2.setTypeface(typeface);
                //NombreCal.setTypeface(typeface);
            }
            else if (fuenteLetra.equals("times")){
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/times.ttf");
                //Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                url1.setTypeface(typeface);
                url2.setTypeface(typeface);
                //NombreCal.setTypeface(typeface);
            }
            else if (fuenteLetra.equals("comicsans")){
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/comicsans.ttf");
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                url1.setTypeface(typeface);
                url2.setTypeface(typeface);
                //NombreCal.setTypeface(typeface);
            }
        }
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for ( int i =0; i < events.size(); i++){
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));
            if (DayNo==eventCalendar.get(Calendar.DAY_OF_MONTH)&& displayMonth==eventCalendar.get(Calendar.MONTH)+1
            && displayYear == eventCalendar.get(Calendar.YEAR)) {
                arrayList.add(events.get(i).getEVENT());
                eventCalendar.set(displayYear, displayMonth - 1, DayNo); // Establecer la fecha del calendario
                ArrayList<Events> eventsForDate = CollectEventByDate(convertDateToString(eventCalendar.getTime()), idCal);

                if (eventsForDate.size() == 1) {
                    if (!(eventsForDate.get(0).getVIDEO().equals(""))) {
                        //String video = eventsForDate.get(0).getVIDEO();
                        /*String truncatedVideo = video.substring(0, Math.min(video.length(), 5)) + "...";
                        Link1.setText(truncatedVideo);*/
                        //url1.setVisibility(View.VISIBLE);
                        url1.setText("LINK");
                    }else {
                       // Link1.setText("");
                        //url1.setVisibility(View.GONE);
                        url1.setText("");
                    }
                    Event1.setText(eventsForDate.get(0).getEVENT());
                    if(eventsForDate.get(0).getIMAGEN()==null){
                        EventImage1.setVisibility(View.GONE);
                    }
                    EventImage1.setImageURI(eventsForDate.get(0).getIMAGEN());
                    Glide.with(getContext()).load(eventsForDate.get(0).getIMAGEN()).into(EventImage1);
                    Event2.setText("");
                    //Link2.setText("");
                    url2.setText("");
                    EventImage2.setVisibility(View.GONE);
                }else if(eventsForDate.size() == 2){
                    if(eventsForDate.get(0).getIMAGEN()!=null){
                        EventImage1.setImageURI(eventsForDate.get(0).getIMAGEN());
                        Glide.with(getContext()).load(eventsForDate.get(0).getIMAGEN()).into(EventImage1);
                        //EventImage2.setVisibility(View.GONE);
                    }else{
                        EventImage1.setVisibility(View.GONE);
                    }
                    if (eventsForDate.get(1).getIMAGEN()!=null){
                        EventImage2.setImageURI(eventsForDate.get(1).getIMAGEN());
                        Glide.with(getContext()).load(eventsForDate.get(1).getIMAGEN()).into(EventImage2);
                        //EventImage1.setVisibility(View.GONE);
                    }else {
                        EventImage2.setVisibility(View.GONE);
                    }
                    if (eventsForDate.get(0).getVIDEO().equals("")) {
                        url1.setText("");
                        //url1.setVisibility(View.GONE);
                    }else {
                        //String video = eventsForDate.get(0).getVIDEO();
                        /*String truncatedVideo = video.substring(0, Math.min(video.length(), 5)) + "...";
                        Link1.setText(truncatedVideo);*/
                        //url1.setVisibility(View.VISIBLE);
                        url1.setText("LINK");
                    }
                    if (eventsForDate.get(1).getVIDEO().equals("")) {
                        //Link2.setText("");
                        //url2.setVisibility(View.GONE);
                        url2.setText("");
                    }else {
                        String video = eventsForDate.get(1).getVIDEO();
                        /*String truncatedVideo = video.substring(0, Math.min(video.length(), 5)) + "...";
                        Link2.setText(truncatedVideo);
                        */
                        //url2.setVisibility(View.VISIBLE);
                        url2.setText("LINK");
                    }
                    Event1.setText(eventsForDate.get(0).getEVENT());
                    Event2.setText(eventsForDate.get(1).getEVENT());

                    if (eventsForDate.get(1).getIMAGEN()==null) {
                        EventImage2.setVisibility(View.GONE);
                    }
                    if (eventsForDate.get(0).getIMAGEN()==null) {
                        EventImage1.setVisibility(View.GONE);
                    }
                }
            }
        }
        return view;
    }

    private String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private ArrayList<Events> CollectEventByDate(String date, Integer id){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(getContext());
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


    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.forLanguageTag("es-ES"));
        Date date = null;
        try {
            date=format.parse(eventDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
