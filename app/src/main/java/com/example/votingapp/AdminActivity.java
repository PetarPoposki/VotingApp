package com.example.votingapp;

import static android.content.ContentValues.TAG;
import static java.time.chrono.JapaneseEra.values;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    String SERVER_KEY = "AAAAeDFaaos:APA91bEJr3EKrIkaR0o4LD1ADA_s1eRBKTB3WX7B8Z-3k21JjVkx5Vn9WxKooGvEeLFLYMvgjLQV9juu2ao_SmVomE5ZzEYNQpUw4Is8jPdpW0Isy2X5YFpufjOZ2EZFCx2IKWE2iLvf";

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
        Intent intent = getIntent();
        PollId = intent.getStringExtra("PollId");

        DatabaseReference lastRef = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

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
                    Poll prasalnik = new Poll(prasanja, time, PollId);

                    lastRef.child("Polls").child(PollId).setValue(prasalnik).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });

                    for(Question k: prasanja)
                    {
                        String d = k.getQuestion();
                        lastRef.child("HasVoted").child(d).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(AdminActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });

                        lastRef.child("TimeOut").child(d).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(AdminActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }




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
                            y = y + "-0";
                            rezultatiodgovori.remove(k);
                            rezultatiodgovori.set(k, y);
                        }
                        x.setAnswers(rezultatiodgovori);
                        rezultati.remove(i);
                        rezultati.set(i,x);
                    }
                    sendNotification();


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

    private void sendNotification() {
        // Create a new message
        JSONObject message = new JSONObject();
        try {
            message.put("to", "/topics/all");
            message.put("data", new JSONObject().put("message", PollId + " HAS STARTED!!"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send the message using the FCM API
        new SendMessageTask().execute(message);
    }

    private class SendMessageTask extends AsyncTask<JSONObject, Void, String> {
        @Override
        protected String doInBackground(JSONObject... params) {
            try {
                // Send the message to the server
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=AAAAeDFaaos:APA91bEJr3EKrIkaR0o4LD1ADA_s1eRBKTB3WX7B8Z-3k21JjVkx5Vn9WxKooGvEeLFLYMvgjLQV9juu2ao_SmVomE5ZzEYNQpUw4Is8jPdpW0Isy2X5YFpufjOZ2EZFCx2IKWE2iLvf");
                connection.setDoOutput(true);

                // Write the message to the request body
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(params[0].toString().getBytes());
                outputStream.flush();
                outputStream.close();

                // Read the response from the server
                InputStream inputStream = connection.getInputStream();
                String response = convertStreamToString(inputStream);
                inputStream.close();

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            // Handle the response from the server
            Log.d(TAG, "Response: " + response);
        }
    }

    private String convertStreamToString(InputStream inputStream) {
        // This method reads the response from the server and converts it to a string
        // You can customize this method to handle the response in any way you want
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }


}






