package com.example.votingapp;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Question {
    @PropertyName("Question")
    private String question;
    @PropertyName("Answers")
    private List<String> answers;


    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    @PropertyName("question")
    public String getQuestion() {
        return question;
    }

    @PropertyName("answers")
    public List<String> getAnswers() {
        return answers;
    }


    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }


    public Question() {

    }
}
