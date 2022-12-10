package com.example.votingapp;

import android.content.Context;
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

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private List<Question> myList;
    private int rowLayout;
    private Context mContext;
    private DatabaseReference mDatabase;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    List<String> odgovori = new ArrayList<String>();


    // Референца на views за секој податок
// Комплексни податоци може да бараат повеќе views per item
// Пристап до сите views за податок се дефинира во view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView question;
        public TextView prasanja;


        public ViewHolder(View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
            question = (TextView) itemView.findViewById(R.id.question);
            prasanja = (TextView) itemView.findViewById(R.id.grupa2);


            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

        }
    }
    // конструктор
    public AdminAdapter(List<Question> myList, int rowLayout, Context context) {
        this.myList = myList;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }
    // Креирање нови views (повикано од layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Question prasanje = myList.get(i);
        viewHolder.question.setText(prasanje.getQuestion());
        List<String> odgovori = prasanje.getAnswers();
        for(String odgovorce: odgovori)
        {
            String pom = viewHolder.prasanja.getText().toString();
            Integer j = odgovori.indexOf(odgovorce);
            if(j != 0)
            {
                pom = pom + "\n" + odgovorce;
            }
            else
            {
                pom = odgovorce;
            }
            viewHolder.prasanja.setText(pom);
        }
    }
    // Пресметка на големината на податочното множество (повикано од
    // layout manager)
    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }

    public List<Question> getList() {
        return myList;
    }

    public List<String> funkcija(List<String> odgovori, String tekst) //work in progress
    {
        for(String x : odgovori)
        {
            Integer i = odgovori.indexOf(x);
            String[] parts = x.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            if (part1.equals(tekst))
            {
                //String intValue = part2.replaceAll("[^0-9]", "");
                Integer brglasovi = Integer.parseInt(part2);
                brglasovi = brglasovi + 1;
                String vrednost = part1 + "-" + brglasovi.toString();
                odgovori.remove(i);
                odgovori.set(i, vrednost);
                return odgovori;
            }

        }
        return null;
    }


}
