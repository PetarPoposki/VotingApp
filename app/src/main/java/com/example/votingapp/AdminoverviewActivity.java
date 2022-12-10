package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminoverviewActivity extends AppCompatActivity {
    Button createPoll;
    EditText pollTitle;
    Poll novo;
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView,kRecyclerView;
    AdminAdapter mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<Question> values;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminoverview);
        values = new ArrayList<Question>();

        mRecyclerView = (RecyclerView) findViewById(R.id.lista2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdminAdapter(values, R.layout.result_items, AdminoverviewActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        context = this;


        mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        pollTitle = findViewById(R.id.inputTitle);

        createPoll = findViewById(R.id.createquestion);
        createPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pollTitle.getText().toString().isEmpty()) {
                    novo = new Poll();
                    novo.setTitle(pollTitle.getText().toString());
                    mDatabase.child("Polls").child(pollTitle.getText().toString()).setValue(novo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminoverviewActivity.this, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(AdminoverviewActivity.this, AdminActivity.class));
                }
                else
                {
                    pollTitle.setError("Enter title");
                }
            }
        });

        mDatabase.child("Results").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                for(DataSnapshot snappy: snapshot.getChildren())
                {
                    List<Question> pomosna = snappy.getValue(Poll.class).getQuestions();
                    for(Question prasanje: pomosna)
                    {
                        values.add(prasanje);
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminoverviewActivity.this, "HELLO" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }
}