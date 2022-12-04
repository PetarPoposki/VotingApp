package com.example.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private List<Question> myList;
    private int rowLayout;
    private Context mContext;
    // Референца на views за секој податок
// Комплексни податоци може да бараат повеќе views per item
// Пристап до сите views за податок се дефинира во view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public TextView firstanswer;
        public TextView secondanswer;
        public ViewHolder(View itemView) {
            super(itemView);

            question = (TextView) itemView.findViewById(R.id.question);
            firstanswer = (TextView) itemView.findViewById(R.id.choice1);
            secondanswer = (TextView) itemView.findViewById(R.id.choice2);
           // myName = (TextView) itemView.findViewById(R.id.Name);
           // Pic = (ImageView) itemView.findViewById(R.id.picture);
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
        Question entry = myList.get(i);
        viewHolder.question.setText(entry.getQuestion());
        viewHolder.firstanswer.setText(entry.getAnswer1());
        viewHolder.secondanswer.setText(entry.getAnswer2());
       // viewHolder.myName.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View v) {
            //    TextView tv = (TextView) v;
             //   Toast.makeText(mContext, tv.getText(), Toast.LENGTH_SHORT).show();
          //  }
      //  });
       // viewHolder.Pic.setImageResource(R.drawable.phone);
    }
    // Пресметка на големината на податочното множество (повикано од
    // layout manager)
    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }
}
