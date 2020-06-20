package com.example.rimae.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoBookmark {
    private String category,time,type,color;

    public VideoBookmark() {}

    public VideoBookmark(String category, String time, String type, String color) {
        this.category = category;
        this.time = time;
        this.type = type;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
