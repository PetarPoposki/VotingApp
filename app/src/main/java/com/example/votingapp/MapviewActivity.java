package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapviewActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    MapAdapter mAdapter;
    List<List<LatLng>> listalokacii;
    List<LatLng> listice;
    List<String> polltituli;
    LatLng lokacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        listalokacii = new ArrayList<List<LatLng>>();
        listice = new ArrayList<LatLng>();
        polltituli = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listalokacii.clear();
                polltituli.clear();
                for(DataSnapshot snappy: snapshot.child("Locations").getChildren())
                {
                    polltituli.add(snappy.getKey().toString());
                    listice.clear();
                    for(DataSnapshot snapp1: snappy.getChildren())
                    {
                        lokacija = new LatLng((Double) snapp1.child("latitude").getValue(),(Double) snapp1.child("longitude").getValue());
                        listice.add(lokacija);
                    }
                    listalokacii.add(listice);
                }
                mRecyclerView = (RecyclerView) findViewById(R.id.lista3);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MapviewActivity.this));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new MapAdapter(listalokacii, R.layout.activity_maps, MapviewActivity.this, polltituli);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}