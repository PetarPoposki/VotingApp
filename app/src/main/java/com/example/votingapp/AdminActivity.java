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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText prasanje, izbor1, izbor2, vreme;
    Button addQuestion, addPoll;
    List<Question> values = new ArrayList<Question>();
    Integer k = 0;
    Integer CountPolls;
    String PollId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);



        vreme = findViewById(R.id.inputTime);
        prasanje = findViewById(R.id.inputquestion);
        izbor1 = findViewById(R.id.inputanswer1);
        izbor2 = findViewById(R.id.inputanswer2);
        addQuestion = findViewById(R.id.createquestion);
        addPoll = findViewById(R.id.addPoll);
        mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("Polls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CountPolls = (int) dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String question = prasanje.getText().toString();
                String question = "Are you ok?";
                String question2 = "Do you like pizza?";
                List<String> answers = new ArrayList<>();
                answers.add("Yes");
                answers.add("No");

                if (question.isEmpty())
                {
                    prasanje.setError("Enter question.");
                }

                else if (!question.isEmpty())
                {
                    String QuestionId = "Question" + k.toString();
                    PollId = "Poll" + CountPolls.toString();
                    k = k + 1;
                    Question prasance = new Question(question, answers);
                    Question prasanje2 = new Question(question2, answers);
                    Poll prasalnik = new Poll();


                    mDatabase.child("Polls").child(PollId).setValue(prasance).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //values.add(prasance);
                    prasanje.setText("");
                    izbor1.setText("");
                    izbor2.setText("");
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
                    String question = "Do you like music?";
                    String question2 = "Do you like art?";
                    List<String> answers = new ArrayList<>();
                    answers.add("Yes");
                    answers.add("No");

                    Question prasance = new Question(question, answers);
                    Question prasanje2 = new Question(question2, answers);
                    List<Question> prasanja = new ArrayList<>();
                    prasanja.add(prasance);
                    prasanja.add(prasanje2);
                    time = "07:02:03";
                    String title = "Vtor";
                    Poll prasalnik = new Poll(prasanja, time, title);
                    PollId = "Poll" + CountPolls.toString();

                    mDatabase.child("Polls").child(title).setValue(prasalnik).addOnSuccessListener(new OnSuccessListener<Void>() {
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