package com.example.tfg;

import android.media.Image;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventList = new ArrayList<>();
    public static ArrayList<Event> eventsForDate(LocalDate date){
        ArrayList<Event> events = new ArrayList<>();
        for(Event event : eventList){
            if(event.getDate().equals(date))
                events.add(event);
        }
        return events;
    }
    private String name;
    private LocalDate date;
    private LocalTime time;

    private ImageView image;

    public Event(String name, LocalDate date, LocalTime time, ImageView image) {
        this.image=image;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
