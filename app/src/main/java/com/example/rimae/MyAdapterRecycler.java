package com.example.rimae;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MyAdapterRecycler extends  FirestoreRecyclerAdapter<Interview,MyAdapterRecycler.InterviewHolder>
{

    public MyAdapterRecycler(@NonNull FirestoreRecyclerOptions<Interview> options) {
        super(options);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onBindViewHolder(@NonNull InterviewHolder holder, int position, @NonNull Interview model) {


        DocumentSnapshot doc= getSnapshots().getSnapshot(position);
        String id= doc.getId();
        Log.d("Query","ID:"+id);
        if(id.equals("hIO7P9KnDkqAYerOjOg5")){
            Log.d("Query", "Este não mostra");
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }else{
            holder.txtTitle.setText(model.getTitle());
            holder.txtName.setText(model.getName());
            holder.txtTime.setText(model.getTime());
            Picasso.get().load(model.getProfile_pic()).fit().centerCrop().into(holder.imgCover);
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

