package com.example.votingapp;

import static java.time.chrono.JapaneseEra.values;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private  DatabaseReference lastRef;
    EditText prasanje, izbor1, vreme;
    Button addQuestion, addPoll;
    List<Question> prasanja;
    List<Question> rezultati;
    List<String> pomosna;
    String PollId;
    Poll poll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        prasanja = new ArrayList<Question>();
        rezultati = new ArrayList<Question>();
        pomosna = new ArrayList<String>();
        prasanja.clear();
        rezultati.clear();
        vreme = findViewById(R.id.inputTime);
        prasanje = findViewById(R.id.inputquestion);
        izbor1 = findViewById(R.id.inputanswer1);
        addQuestion = findViewById(R.id.createquestion);
        addPoll = findViewById(R.id.addPoll);
        DatabaseReference lastRef = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        Query lastQuery = lastRef.child("Polls").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {

                    String key = data.getKey(); // then it has the value "4:"
                    poll = new Poll();
                    poll.setTitle(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tekst = prasanje.getText().toString();
                Integer k = 0;
                //<String> answers = new ArrayList<>();
                //answers.add("Yes");
                //answers.add("No");

                if (tekst.isEmpty())
                {
                    prasanje.setError("Enter question.");
                }

                else if (!tekst.isEmpty())
                {

                    for (Question x: prasanja)
                    {
                        if(x.getQuestion().equals(tekst))
                        {
                            Integer i = prasanja.indexOf(x);
                            List<String> answers = x.getAnswers();
                            answers.add(izbor1.getText().toString());
                            x.setAnswers(answers);
                            prasanja.remove(i);
                            prasanja.set(i,x);
                            k = 1;
                            break;
                        }

                    }
                    if (k == 0)
                    {
                        Question prasance = new Question();
                        prasance.setQuestion(tekst);
                        List<String> answers = new ArrayList<>();
                        answers.add(izbor1.getText().toString());
                        prasance.setAnswers(answers);
                        prasanja.add(prasance);
                    }

                    izbor1.setText("");
                }
            }
        });

        addPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = vreme.getText().toString();
                if(time.isEmpty())
                {
                    vreme.setError("Enter duration.");
                }
                else
                {
                    PollId = poll.getTitle();
                    Poll prasalnik = new Poll(prasanja, time, PollId);

                    lastRef.child("Polls").child(PollId).setValue(prasalnik).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });


                    for(Question x : prasanja)
                    {
                        Question novo = new Question(x.getQuestion(),x.getAnswers());
                        rezultati.add(novo);
                    }

                    for(Question x: rezultati)
                    {
                        Integer i = rezultati.indexOf(x);
                        pomosna.clear();
                        List<String> rezultatiodgovori = x.getAnswers();
                        for(String y: rezultatiodgovori)
                        {
                            Integer k = rezultatiodgovori.indexOf(y);
                            y = y + " 0";
                            rezultatiodgovori.remove(k);
                            rezultatiodgovori.set(k, y);
                        }
                        x.setAnswers(rezultatiodgovori);
                        rezultati.remove(i);
                        rezultati.set(i,x);
                    }



                    Poll rezultat = new Poll(rezultati, time, PollId);

                    lastRef.child("Results").child(PollId).setValue(rezultat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });

                   Intent intent = new Intent(AdminActivity.this, AdminoverviewActivity.class);
                   startActivity(intent);
                }
            }
        });

    }
}