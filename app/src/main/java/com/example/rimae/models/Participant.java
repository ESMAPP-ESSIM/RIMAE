package com.example.rimae.models;

public class Participant {

    private String name, profile_pic;

    public Participant() {}

    public Participant(String name, String profile_pic) {
        this.name = name;
        this.profile_pic = profile_pic;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
