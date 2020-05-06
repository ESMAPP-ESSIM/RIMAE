package com.example.rimae;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterRecycler extends RecyclerView.Adapter<MyAdapterRecycler.ViewHolder> {

    public class ViewHolder extends  RecyclerView.ViewHolder{

        protected TextView interviewTitle, interviewName,interviewTime;
        protected ImageView imgCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            interviewTitle = itemView.findViewById(R.id.titulo);
            interviewName= itemView.findViewById(R.id.nome);
            interviewTime= itemView.findViewById(R.id.time);
            imgCover = itemView.findViewById(R.id.pic);
        }
    }

    private ArrayList<Interview> myInterviewList;
    public MyAdapterRecycler(ArrayList<Interview> myInterviewList) {
        this.myInterviewList = myInterviewList;
    }


    @NonNull
    @Override
    public MyAdapterRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterRecycler.ViewHolder holder, int position) {
        Interview item = myInterviewList.get(position);
        holder.interviewName.setText(item.getInterviewName());
        holder.interviewTitle.setText(item.getInterviewTitle());
        holder.interviewTime.setText(item.getInterviewTime());
        holder.imgCover.setImageResource(item.getPictur());

    }

    @Override
    public int getItemCount() {
        return myInterviewList.size();
    }


}

