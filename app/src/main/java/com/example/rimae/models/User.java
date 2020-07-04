package com.example.rimae.models;

public class User {
    String name, profile_pic;
    Long points;

    public User() {}

    public User(String name, String profile_pic,Long points){
        this.name = name;
        this.profile_pic = profile_pic;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Long getPoints() {
        return points;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
