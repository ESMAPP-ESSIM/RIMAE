package com.example.rimae.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Training;
import com.example.rimae.ui.my_interviews.EditInterview;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyInterviewsRecycler extends  FirestoreRecyclerAdapter<Training, MyInterviewsRecycler.InterviewHolder>
{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MyInterviewsRecycler(@NonNull FirestoreRecyclerOptions<Training> options) {
        super(options);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onBindViewHolder(@NonNull final InterviewHolder holder, int position, @NonNull Training model) {
        final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id=snapshot.getId();
        Log.d("MY","OBSERVERUID: "+snapshot.get("observed_uid").toString() +"OWNER: " + snapshot.get("owner_uid").toString());
        if(snapshot.get("observed_uid").toString().equals(mAuth.getUid()) || snapshot.get("owner_uid").toString().equals(mAuth.getUid())){
            holder.txtTitle.setText(model.getTitle());
            holder.txtName.setText(model.getName());
            Long time = Long.parseLong(model.getTime());
            holder.txtTime.setText(new SimpleDateFormat("mm:ss").format(new Date(time)));
            Picasso.get().load(model.getProfile_pic()).fit().centerCrop().into(holder.imgCover);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context ctx= v.getContext();
                    Intent intent = new Intent(ctx, EditInterview.class);
                    intent.putExtra("interviewId",id);
                    ctx.startActivity(intent);
                }
            });
        }else {
            holder.itemView.setVisibility(View.GONE);
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
            param.height=0;
            param.width=0;
            holder.itemView.setLayoutParams(param);
        }
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
