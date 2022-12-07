package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminoverviewActivity extends AppCompatActivity {
    Button createPoll;
    EditText pollTitle;
    Poll novo;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminoverview);
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
    }
}