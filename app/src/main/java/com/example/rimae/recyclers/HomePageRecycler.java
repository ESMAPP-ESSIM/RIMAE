package com.example.rimae.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Main2Activity;
import com.example.rimae.R;
import com.example.rimae.VideoActivity;
import com.example.rimae.models.Training;
import com.example.rimae.ui.interview.DescriptionInterview;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomePageRecycler extends  FirestoreRecyclerAdapter<Training, HomePageRecycler.InterviewHolder>
{

    public HomePageRecycler(@NonNull FirestoreRecyclerOptions<Training> options) {
        super(options);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onBindViewHolder(@NonNull final InterviewHolder holder, int position, @NonNull Training model) {
        holder.txtTitle.setText(model.getTitle());
        holder.txtName.setText(model.getName());
        Long time = Long.parseLong(model.getTime());
        holder.txtTime.setText(new SimpleDateFormat("mm:ss").format(new Date(time)));
        Picasso.get().load(model.getProfile_pic()).fit().centerCrop().into(holder.imgCover);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                String id=snapshot.getId();
                Context ctx= v.getContext();
                Intent intent = new Intent(ctx, DescriptionInterview.class);
                intent.putExtra("interviewId",id);
                ctx.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public InterviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new InterviewHolder(view);
    }

    class InterviewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtName,txtTime;
        ImageView imgCover;

        public InterviewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.titulo);
            txtName = itemView.findViewById(R.id.nome);
            txtTime = itemView.findViewById(R.id.time);
            imgCover = itemView.findViewById(R.id.pic);


        }
    }
}
