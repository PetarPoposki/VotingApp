package com.example.votingapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Userresults extends Fragment {

    Poll novo;
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView,kRecyclerView;
    AdminAdapter mAdapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<Question> values;
    List<String> prasanja;
    Context context;

    public Userresults() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        values = new ArrayList<Question>();
        prasanja = new ArrayList<String>();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lista3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdminAdapter(values, R.layout.result_items, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        context = getActivity();

        mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                values.clear();
                for(DataSnapshot snoopy: snapshot.child("HasVoted").getChildren())
                {
                    prasanja.add(snoopy.getKey());

                }
                for(DataSnapshot snappy: snapshot.child("Results").getChildren())
                {
                    List<Question> pomosna = snappy.getValue(Poll.class).getQuestions();
                    for(Question prasanje: pomosna)
                    {
                        String d = snapshot.child("HasVoted").child(prasanje.getQuestion()).getValue(String.class);
                        String[] parts = d.split(" ");

                        String mail = mUser.getEmail();
                        for (String part: parts)
                        {
                            if(part.equals(mail))
                            {
                                values.add(prasanje);
                                break;
                            }
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "HELLO" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_userresults, container, false);
    }
}