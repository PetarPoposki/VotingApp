package com.example.votingapp;

import com.google.firebase.database.PropertyName;

public class Question {
    @PropertyName("Question")
    private String question;
    @PropertyName("Answer1")
    private String answer1;
    @PropertyName("Answer2")
    private String answer2;

    public Question(String question, String answer1, String answer2) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public Question() {

    }
}
