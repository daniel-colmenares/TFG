package com.example.tfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
    List<Events> events;
    String cellColor;
    LayoutInflater inflater;
    TextView lunes, martes, miercoles, jueves, viernes, sabado, domingo;
    private FragmentActivity mActivity;





    /*public MyGridAdapter(@NonNull Context context,  List<Date> dates, Calendar currentDate, List<Events> events, FragmentActivity activity) {
        super(context, R.layout.single_cell_layout);
        this.dates=dates;
        this.currentDate=currentDate;
        this.events=events;
        this.mActivity = activity;
        inflater = LayoutInflater.from(context);
    }*/

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events, String cellColor) {
        super(context, R.layout.single_cell_layout);
        this.dates=dates;
        this.currentDate=currentDate;
        this.events=events;
        this.cellColor=cellColor;
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
        TextView EventNumber = view.findViewById(R.id.events_id);
        if (monthDate.getTime()==0){
            Day_Number.setText("-");
        }else {
            Day_Number.setText(String.valueOf(DayNo));
        }
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for ( int i =0; i < events.size(); i++){
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDATE()));
            if (DayNo==eventCalendar.get(Calendar.DAY_OF_MONTH)&& displayMonth==eventCalendar.get(Calendar.MONTH)+1
            && displayYear == eventCalendar.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT());
                if (arrayList.size()==1){
                    EventNumber.setText(events.get(i).getEVENT());
                }else {
                    EventNumber.setText(arrayList.size() + "Evtos.");
                }
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
