package com.example.votingapp;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Poll {
    @PropertyName("Questions")
    private List<Question> questions;
    @PropertyName("Time")
    private String time;
    @PropertyName("Title")
    private String title;

    public Poll(List<Question> questions, String time, String title) {
        this.questions = questions;
        this.time = time;
        this.title = title;
    }

    @PropertyName("questions")
    public List<Question> getQuestions() {
        return questions;
    }

    @PropertyName("time")
    public String getTime() {
        return time;
    }

    @PropertyName("title")
    public String getTitle() {return  title;}


    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {this.title = title;}

    public Poll() {

    }
}
