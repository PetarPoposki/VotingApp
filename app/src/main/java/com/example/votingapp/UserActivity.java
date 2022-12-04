package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    myAdapter mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<Question> values;
    Integer CountPolls;
    String PollId;
    String prasa, odg1, odg2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        values = new ArrayList<Question>();
        mRecyclerView = (RecyclerView) findViewById(R.id.lista);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Polls").child("Poll1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    values.add((Question) postSnapshot.getValue(Question.class));

                }
                mAdapter = new myAdapter(values, R.layout.poll_items, UserActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "HELLO" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
       // PollId = "Poll" + CountPolls.toString();
        //values.add(new Question("aren si?", "ne", "da"));




       // Question prasanje = new Question(mUser.getEmail().split("@")[0], mUser.getEmail().split("@")[0],mUser.getEmail().split("@")[0]);
        //values.add(prasanje);

    }





       // Question prasanje = new Question(mUser.getEmail().split("@")[0], mUser.getEmail().split("@")[0], mUser.getEmail().split("@")[0] );
       // values.add(prasanje);

//сетирање на RecyclerView контејнерот


    }
