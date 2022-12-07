package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import javax.sql.ConnectionPoolDataSource;

public class UserActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
   // private DatabaseReference reference;
    RecyclerView mRecyclerView;
    myAdapter mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<Question> values;
    Context context;
    String Vreme;




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
        mAdapter = new myAdapter(values, R.layout.poll_items, UserActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        context = this;
        Vreme = new String();




       // reference = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Polls");
        mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        mDatabase.child("Polls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                // CountQuestions = (int) snapshot.getChildrenCount();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //values.add((Question) postSnapshot.child().getValue(Poll.class));
                    for(Question prasanje: postSnapshot.getValue(Poll.class).getQuestions())
                    {
                        values.add(prasanje);
                    }

                }
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "HELLO" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });



            //Toast.makeText(context, "Time of start is " + Vreme, Toast.LENGTH_SHORT).show();






    }





    }
