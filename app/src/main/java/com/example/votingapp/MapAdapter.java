package com.example.votingapp;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
    private List<List<LatLng>> myList;
    private  List<String> polltituli;
    private int rowLayout;
    private Context mContext;
    private DatabaseReference mDatabase;
    public List<LatLng> listalokacii;




    FirebaseAuth mAuth;
    FirebaseUser mUser;

    // Референца на views за секој податок
// Комплексни податоци може да бараат повеќе views per item
// Пристап до сите views за податок се дефинира во view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        TextView polltitle;
        GoogleMap mapCurrent;
        MapView map;

        public ViewHolder(View itemView) {
            super(itemView);
            //mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
            polltitle = (TextView) itemView.findViewById(R.id.polltitula);
            map = (MapView) itemView.findViewById(R.id.showmap);
            if (map != null)
            {
                map.onCreate(null);
                map.onResume();
                map.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MapsInitializer.initialize(mContext.getApplicationContext());
            mapCurrent = googleMap;
            LatLng posledna = new LatLng(41.998925, 21.403635);
            for(LatLng lokacija: listalokacii)
            {
                mapCurrent.addMarker(new MarkerOptions().position(lokacija).title("EVE SU!"));
            }
            mapCurrent.moveCamera(CameraUpdateFactory.newLatLng(posledna));

        }
    }
    // конструктор
    public MapAdapter(List<List<LatLng>> myList, int rowLayout, Context context, List<String> polltituli) {
        this.myList = myList;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.polltituli = polltituli;
    }
    // Креирање нови views (повикано од layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        listalokacii = myList.get(i);
        String titula = polltituli.get(i);
        viewHolder.polltitle.setText(titula);

    }

    @Override
    public void onViewRecycled(ViewHolder viewHolder)
    {
        // Cleanup MapView here?
        if (viewHolder.mapCurrent != null)
        {
            viewHolder.mapCurrent.clear();
            viewHolder.mapCurrent.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }
    // Пресметка на големината на податочното множество (повикано од
    // layout manager)
    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }

}
