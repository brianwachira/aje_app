package com.example.ex_contactapp.models;

public class ModelCalls {

    private String name,duration,date;

    public ModelCalls(String name, String duration, String date) {
        this.name = name;
        this.duration = duration;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
