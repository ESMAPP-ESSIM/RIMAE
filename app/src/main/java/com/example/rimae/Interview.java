package com.example.rimae;

public class Interview {
    private String interviewName,interviewTitle,interviewTime;
    private  int pictur;

    public Interview(String interviewName, String interviewTitle,String interviewTime, int pictur){
        this.interviewName=interviewName;
        this.interviewTitle=interviewTitle;
        this.interviewTime=interviewTime;
        this.pictur=pictur;
    }

    public int getPictur() {
        return pictur;
    }

    public String getInterviewName() {
        return interviewName;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public String getInterviewTitle() {
        return interviewTitle;
    }

    public void setInterviewName(String interviewName) {
        this.interviewName = interviewName;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public void setInterviewTitle(String interviewTitle) {
        this.interviewTitle = interviewTitle;
    }

    public void setPictur(int pictur) {
        this.pictur = pictur;
    }
}
