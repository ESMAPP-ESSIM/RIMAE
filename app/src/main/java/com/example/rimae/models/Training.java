package com.example.rimae.models;

import com.google.firebase.firestore.SnapshotMetadata;

<<<<<<< HEAD:app/src/main/java/com/example/rimae/models/Training.java
public class Training {
    private String name,title,time,profile_pic;

    public Training(){}

    public Training(String name, String title, String time, String profile_pic ){
        this.name= name;
        this.title=title;
        this.time=time;
        this.profile_pic=profile_pic;
=======
public class Interview {
    private String name, title, time, profile_pic;

    public Interview() {}

    public Interview(String name, String title, String time, String profile_pic) {
        this.name = name;
        this.title = title;
        this.time = time;
        this.profile_pic = profile_pic;
>>>>>>> 2ee09a91a575a84fd909cd202a9eddd004abfc76:app/src/main/java/com/example/rimae/Interview.java
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String interviewTime) {
        this.time = interviewTime;
    }

    public void setTitle(String interviewTitle) {
        this.title = interviewTitle;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
