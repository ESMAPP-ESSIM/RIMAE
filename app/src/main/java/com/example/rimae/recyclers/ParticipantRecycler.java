package com.example.rimae.recyclers;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.models.Participant;
import com.example.rimae.models.Training;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ParticipantRecycler extends FirestoreRecyclerAdapter<Participant, ParticipantRecycler.ParticipantHolder>
{
    int index=-1;
    public ParticipantRecycler(@NonNull FirestoreRecyclerOptions<Participant> options) {
        super(options);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onBindViewHolder(@NonNull final ParticipantHolder holder, final int position, @NonNull Participant model) {
        holder.nameParti.setText(model.getName());
        Picasso.get().load(model.getProfile_pic()).fit().centerCrop().into(holder.photoParti);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                String id=snapshot.getId();
                index=position;
                Globals.participantId=id;
                notifyDataSetChanged();
            }
        });

        if(index==position){
            holder.itemView.setBackgroundColor(Color.parseColor("#7E7E7E"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @NonNull
    @Override
    public ParticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_cardview, parent, false);
        Log.w("Participants",view.toString());
        return new ParticipantHolder(view);
    }

    class ParticipantHolder extends RecyclerView.ViewHolder {
        ImageView photoParti;
        TextView nameParti;

        public ParticipantHolder(@NonNull View itemView) {
            super(itemView);
            photoParti=itemView.findViewById(R.id.photoParti);
            nameParti=itemView.findViewById(R.id.nameParti);
        }
    }
}
