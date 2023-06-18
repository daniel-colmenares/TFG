package com.example.tfg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
        //int currentDay = currentDate.get(Calendar.DAY_OF_WEEK);


        View view = convertView;
        if (view==null){
            view = inflater.inflate(R.layout.single_cell_layout,parent,false);
        }
        view.setBackgroundColor(Color.parseColor(cellColor));
        //nombreCal = parent.findViewById(R.id.NombreCalView);



        //view.setBackgroundColor(getContext().getResources().getColor(R.color.green));
        /*lunes = view.findViewById(R.id.lunes);
        martes = view.findViewById(R.id.martes);
        miercoles = view.findViewById(R.id.miercoles);
        jueves = view.findViewById(R.id.jueves);
        viernes = view.findViewById(R.id.viernes);
        sabado = view.findViewById(R.id.sabado);
        domingo = view.findViewById(R.id.domingo);

        if (currentDay==Calendar.MONDAY){
            lunes.setTypeface(null, Typeface.BOLD);

        }
        else if (currentDay==Calendar.TUESDAY){
            martes.setTypeface(null, Typeface.BOLD);

        }
        if (currentDay==Calendar.WEDNESDAY){
            miercoles.setTypeface(null, Typeface.BOLD);

        }
        if (currentDay==Calendar.THURSDAY){
            jueves.setTypeface(null, Typeface.BOLD);

        }
        if (currentDay==Calendar.FRIDAY){
            viernes.setTypeface(null, Typeface.BOLD);

        }
        if (currentDay==Calendar.SATURDAY){
            sabado.setTypeface(null, Typeface.BOLD);

        }
        if (currentDay==Calendar.SUNDAY){
            domingo.setTypeface(null, Typeface.BOLD);

        }
        */

        TextView Day_Number = view.findViewById(R.id.calendar_day);
        TextView Event1 = view.findViewById(R.id.events_id);
        TextView Link1 = view.findViewById(R.id.link1);
        TextView Event2 = view.findViewById(R.id.evento2);
        TextView Link2 = view.findViewById(R.id.link2);

        ImageView EventImage = view.findViewById(R.id.imagenEvento);
        if (monthDate.getTime()==0){
            Day_Number.setText("-");
        }else {
            Day_Number.setText(String.valueOf(DayNo));
            if (fuenteLetra.equals("monospace")){
                Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                Link1.setTypeface(typeface);
                Link2.setTypeface(typeface);
            }
            else if (fuenteLetra.equals("serif")){
                Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                Link1.setTypeface(typeface);
                Link2.setTypeface(typeface);
            }
            else if (fuenteLetra.equals("times")){
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/times.ttf");
                //Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                Link1.setTypeface(typeface);
                Link2.setTypeface(typeface);
            }
            else if (fuenteLetra.equals("cursivestandard")){
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/cursivestandard.ttf");
                Day_Number.setTypeface(typeface);
                Event1.setTypeface(typeface);
                Event2.setTypeface(typeface);
                Link1.setTypeface(typeface);
                Link2.setTypeface(typeface);
            }
        }
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for ( int i =0; i < events.size(); i++){
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));
            if (DayNo==eventCalendar.get(Calendar.DAY_OF_MONTH)&& displayMonth==eventCalendar.get(Calendar.MONTH)+1
            && displayYear == eventCalendar.get(Calendar.YEAR)) {
                arrayList.add(events.get(i).getEVENT());
                if (arrayList.size() == 1) {
                    if(events.get(i).getVIDEO().equals("")){
                        Link1.setVisibility(View.INVISIBLE);
                    }else {
                        String video = events.get(i).getVIDEO();
                        String truncatedVideo = video.substring(0, Math.min(video.length(), 5)) + "...";
                        Link1.setText(truncatedVideo);
                    }
                    if(events.get(i).getEVENT()==null){
                        Event1.setVisibility(View.INVISIBLE);
                    } else {
                        Event1.setText(events.get(i).getEVENT());
                    }
                EventImage.setImageURI(events.get(i).getIMAGEN());
                Glide.with(getContext()).load(events.get(i).getIMAGEN()).into(EventImage);
                Event2.setVisibility(View.GONE);
                Link2.setVisibility(View.GONE);
               }else if(arrayList.size() == 2){
                    if(events.get(0).getVIDEO().equals("")){
                        Link1.setVisibility(View.INVISIBLE);
                    }else {
                        String video = events.get(i).getVIDEO();
                        String truncatedVideo = video.substring(0, Math.min(video.length(), 5)) + "...";
                        Link1.setText(truncatedVideo);
                    }
                    if(events.get(1).getVIDEO().equals("")){
                        Link2.setVisibility(View.INVISIBLE);
                    }else {
                        String video2 = events.get(i).getVIDEO();
                        String truncatedVideo2 = video2.substring(0, Math.min(video2.length(), 5)) + "...";
                        Link2.setText(truncatedVideo2);
                    }
                    if(events.get(0).getEVENT().equals("")){
                        Event1.setVisibility(View.INVISIBLE);
                    }else{
                        Event1.setText(events.get(i).getEVENT());
                    }
                    if(events.get(1).getEVENT().equals("")){
                        Event2.setVisibility(View.INVISIBLE);
                    }else{
                        Event2.setText(events.get(i).getEVENT());
                    }
                    EventImage.setImageURI(events.get(i).getIMAGEN());
                    Glide.with(getContext()).load(events.get(i).getIMAGEN()).into(EventImage);
                }
                        /*String video1 = events.get(i).getVIDEO();
                        String truncatedVideo1 = video1.substring(0, Math.min(video1.length(), 5)) + "...";
                        Link1.setText(truncatedVideo1);
                        Event1.setText(events.get(i).getEVENT());
                        EventImage.setImageURI(events.get(i).getIMAGEN());
                        Glide.with(getContext()).load(events.get(i).getIMAGEN()).into(EventImage);
                        String video2 = events.get(i).getVIDEO();
                        String truncatedVideo2 = video2.substring(0, Math.min(video2.length(), 5)) + "...";
                        Link1.setText(truncatedVideo2);
                        Event2.setText(events.get(i).getEVENT());
                        */
            }
        }
        return view;
    }



    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("es-ES"));
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
