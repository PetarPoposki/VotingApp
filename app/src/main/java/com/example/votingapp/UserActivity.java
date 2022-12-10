package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView,kRecyclerView;
    myAdapter mAdapter,kAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<Question> values;
    List<String> iminja;
    Context context;
    List<String> vreminja;
    TextView naslov;
    List<Poll> polls;
    Location location = null;
    public static final int REQUEST_LOCATION_PERMISSION = 1;
    long MIN_TIME_INTERVAL = 1000; // 1 second
    float MIN_DISTANCE = 100; // 100 meters





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        values = new ArrayList<Question>();
        iminja = new ArrayList<String>();
        vreminja = new ArrayList<String>();
        polls = new ArrayList<Poll>();
        naslov = findViewById(R.id.textView);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Handle the updated location
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
            }
        };

        mRecyclerView = (RecyclerView) findViewById(R.id.lista);
        kRecyclerView = (RecyclerView) findViewById(R.id.lista);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new myAdapter(values, R.layout.poll_items, UserActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        context = this;



        // Check if the app has permission to access the device's location
        if (ActivityCompat.checkSelfPermission((Activity) UserActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission((Activity) UserActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If the app doesn't have permission, request permission
            ActivityCompat.requestPermissions((Activity) UserActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // If the location provider is not enabled, prompt the user to enable it
                Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(enableLocationIntent);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_INTERVAL, MIN_DISTANCE, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location == null) {
            try {
                throw new Exception("Please allow location so we can submit your vote");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }






        mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                vreminja.clear();
                // CountQuestions = (int) snapshot.getChildrenCount();

                for (DataSnapshot postSnapshot : snapshot.child("Polls").getChildren()) {
                    String mailce = mUser.getEmail();
                    String[] mailparts = mailce.split("[.]");
                    mDatabase.child("Locations").child(postSnapshot.getValue(Poll.class).getTitle()).child(mailparts[0]).setValue(location).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Toast.makeText(mContext, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });


                    vreminja.add(postSnapshot.getValue(Poll.class).getTime());
                    for(Question prasanje: postSnapshot.getValue(Poll.class).getQuestions())
                    {
                        String d = snapshot.child("HasVoted").child(prasanje.getQuestion()).getValue(String.class);
                        String[] parts = d.split(" ");

                        String d2 = snapshot.child("TimeOut").child(prasanje.getQuestion()).getValue(String.class);
                        String[] parts2 = d2.split(" ");

                        Integer proveri = 0;
                        String mail = mUser.getEmail();
                        for (String part: parts)
                        {
                            if(part.equals(mail))
                            {
                                proveri = 1;
                                break;
                            }
                        }
                        for (String part2: parts2)
                        {
                            if(part2.equals(mail))
                            {
                                proveri = 1;
                                break;
                            }
                        }
                        if (proveri == 0)
                        {
                            values.add(prasanje);

                        }
                    }

                }
                mRecyclerView = (RecyclerView) findViewById(R.id.lista);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(UserActivity.this));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new myAdapter(values, R.layout.poll_items, UserActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                for(String tro : vreminja) {
                    Integer kla = vreminja.indexOf(tro);

                    String[] deloj = tro.split(":");
                    Integer sekundi = Integer.parseInt(deloj[0]) * 3600 + Integer.parseInt(deloj[1]) * 60 + Integer.parseInt(deloj[2]);
                    if (!values.isEmpty()){
                        new CountDownTimer(sekundi * 1000 + 1000, 1000) {
                            String vnatre = tro;
                            Integer ks = 0;

                            public void onTick(long millisUntilFinished) {
                                int seconds = (int) (millisUntilFinished / 1000);
                                int minutes = seconds / 60;
                                seconds = seconds % 60;
                                String pom = naslov.getText().toString();
                                String[] delojni = pom.split("\n");
                                if (delojni[0].equals("TIME: 00:00")) {
                                    ks = ks + 1;
                                }
                                if (kla == ks) {
                                    pom = "TIME: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
                                } else {
                                    pom = pom + "\n" + "TIME: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
                                }
                                naslov.setText(pom);
                            }

                            public void onFinish() {

                                for (DataSnapshot snap : snapshot.child("Polls").getChildren()) {
                                    polls.add(snap.getValue(Poll.class));
                                }
                                for (Poll pe : polls) {

                                    if (vnatre.equals(pe.getTime())) {
                                        List<Question> pres = pe.getQuestions();
                                        for (int hj = 0; hj < pres.size(); hj++) {
                                            for (int kj = 0; kj < values.size(); kj++) {
                                                if (values.get(kj).getQuestion().equals(pres.get(hj).getQuestion())) {
                                                    Question prasanule = values.get(kj);

                                                    String iminja2 = snapshot.child("TimeOut").child(prasanule.getQuestion()).getValue(String.class);
                                                    iminja2 = iminja2 + " " + mUser.getEmail();

                                                    mDatabase.child("TimeOut").child(prasanule.getQuestion()).setValue(iminja2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            //Toast.makeText(mContext, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                    values.remove(kj);
                                                    kj--;
                                                }
                                            }
                                        }
                                    }
                                }
                                kRecyclerView = (RecyclerView) findViewById(R.id.lista);
                                kRecyclerView.setHasFixedSize(true);
                                kRecyclerView.setLayoutManager(new LinearLayoutManager(UserActivity.this));
                                kRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                kAdapter = new myAdapter(values, R.layout.poll_items, UserActivity.this);
                                kRecyclerView.setAdapter(mAdapter);
                                // naslov.setText("POLL FINISHED \n");
                            }
                        }.start();
                }
                    else
                    {
                        values.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "HELLO" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    }
