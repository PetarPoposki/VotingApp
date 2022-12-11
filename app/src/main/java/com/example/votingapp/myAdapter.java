package com.example.votingapp;

import android.content.Context;
import android.location.Location;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private List<Question> myList;
    private int rowLayout;
    private Context mContext;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    List<String> odgovori = new ArrayList<String>();
    List<String> odgovorcinja = new ArrayList<>();



    // Референца на views за секој податок
// Комплексни податоци може да бараат повеќе views per item
// Пристап до сите views за податок се дефинира во view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView question;
        public Button potvrdi;

        //public TextView firstanswer;
        //public TextView secondanswer;
        public RadioGroup radiogrupa;
        public ViewHolder(View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance("https://votingapp-b03ae-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
            question = (TextView) itemView.findViewById(R.id.question);
            radiogrupa = (RadioGroup) itemView.findViewById(R.id.grupa);
            potvrdi = (Button) itemView.findViewById(R.id.confirmanswer);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();



            radiogrupa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup rGroup, int checkedId) {

                    int radioButtonID = radiogrupa.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) radiogrupa.findViewById(radioButtonID);

                    String selectedText = (String) radioButton.getText();

                    potvrdi.setOnClickListener( new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    // CountQuestions = (int) snapshot.getChildrenCount();
                                    for (DataSnapshot postSnapshot : snapshot.child("Results").getChildren()) {
                                        //values.add((Question) postSnapshot.child().getValue(Poll.class));
                                        List<Question> listaprasanja = new ArrayList<Question>();
                                        listaprasanja.clear();
                                        listaprasanja = postSnapshot.getValue(Poll.class).getQuestions();
                                        String title = postSnapshot.getValue(Poll.class).getTitle();
                                        String time = postSnapshot.getValue(Poll.class).getTime();
                                        for(Question prasanje: listaprasanja)
                                        {

                                            //values.add(prasanje);
                                            //mAdapter.notifyDataSetChanged();
                                            Integer i = listaprasanja.indexOf(prasanje);

                                            odgovorcinja = prasanje.getAnswers();
                                            odgovorcinja = funkcija(odgovorcinja, selectedText);
                                            if (odgovorcinja != null)
                                            {
                                                String iminja = snapshot.child("HasVoted").child(prasanje.getQuestion()).getValue(String.class);
                                                iminja = iminja + " " + mUser.getEmail();
                                                prasanje.setAnswers(odgovorcinja);
                                                listaprasanja.remove(i);
                                                listaprasanja.set(i, prasanje);
                                                Poll novo = new Poll(listaprasanja, time, title);
                                                mDatabase.child("Results").child(title).setValue(novo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Toast.makeText(mContext, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                mDatabase.child("HasVoted").child(prasanje.getQuestion()).setValue(iminja).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Toast.makeText(mContext, "DATA IS ADDED", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                myList.remove(getAdapterPosition());
                                                notifyItemRemoved(getAdapterPosition());
                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(mContext, "HELLO" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });




                        }
                    });


                }
            });

        }
    }
    // конструктор
    public myAdapter(List<Question> myList, int rowLayout, Context context) {
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

        Question pras = myList.get(i);
        viewHolder.question.setText(pras.getQuestion());
        List<String> odgovori = new ArrayList<>();
        odgovori.clear();
        odgovori = pras.getAnswers();
        for(String odgovor : odgovori)
        {
            RadioButton button = new RadioButton(mContext);
            button.setText(odgovor);
            viewHolder.radiogrupa.addView(button);
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

    public List<String> funkcija(List<String> odgovorig, String tekst) //work in progress
    {
        for(String x : odgovorig)
        {
            Integer i = odgovorig.indexOf(x);
            String[] parts = x.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            if (part1.equals(tekst))
            {

                Integer brglasovi = Integer.parseInt(part2);
                brglasovi = brglasovi + 1;
                String vrednost = part1 + "-" + brglasovi.toString();
                odgovorig.remove(i);
                odgovorig.set(i, vrednost);
                return odgovorig;
            }

        }
        return null;
    }

}