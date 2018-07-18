package com.example.tc.todolist;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity
public class TodoItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String title;
    private String description;
    private String date;
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeInEpochs() { return timeInEpochs; }

    public void setTimeInEpochs(long timeInEpochs) {
        this.timeInEpochs = timeInEpochs;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInEpochs);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        ++month;

        this.date = day + "/" + month + "/" + year;

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        this.time = hour + ":" + min;
    }

    private long timeInEpochs;

    TodoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
