package com.example.rimae.models;

public class Bookmark {
    private String color,name;

    public Bookmark(){}

    public  Bookmark(String color,String name){
        this.color=color;
        this.name=name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
