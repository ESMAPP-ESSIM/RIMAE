package com.example.rimae.recyclers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.models.Bookmark;
import com.example.rimae.models.Question;
import com.example.rimae.ui.interview.AvaluateInterviewActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AvaluationRecycler extends FirestoreRecyclerAdapter<Question, AvaluationRecycler.BookmarkHolder> {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public AvaluationRecycler(@NonNull FirestoreRecyclerOptions<Question> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final BookmarkHolder holder, final int position, @NonNull final Question model) {
        holder.questionName.setText(model.getQuestion());
        holder.good.setBackgroundResource(R.drawable.good_emoji);
        holder.medium.setBackgroundResource(R.drawable.medium_emoji);
        holder.bad.setBackgroundResource(R.drawable.bad_emoji);
        holder.good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.good.setBackgroundResource(R.drawable.good_emoji_sel);
                    holder.medium.setBackgroundResource(R.drawable.medium_emoji);
                    holder.bad.setBackgroundResource(R.drawable.bad_emoji);
                    db.collection("trainings").document(Globals.currentInterview).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Globals.avaliatedUserId=documentSnapshot.get("observed_uid").toString();
                            String observedId=documentSnapshot.get("observed_uid").toString();
                            String uid=mAuth.getUid();
                            Map<String,Object> avaluation = new HashMap<>();
                            avaluation.put("value","3");
                            avaluation.put("category",AvaluateInterviewActivity.bookmarkName.getText().toString());
                            db.collection("users").document(observedId).collection("avaluations").document(model.getQuestion()+uid).set(avaluation);
                        }
                    });
            }
        });
        holder.medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.good.setBackgroundResource(R.drawable.good_emoji);
                    holder.medium.setBackgroundResource(R.drawable.medium_emoji_sel);
                    holder.bad.setBackgroundResource(R.drawable.bad_emoji);
                db.collection("trainings").document(Globals.currentInterview).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Globals.avaliatedUserId=documentSnapshot.get("observed_uid").toString();
                        String observedId=documentSnapshot.get("observed_uid").toString();
                        String uid=mAuth.getUid();
                        Map<String,Object> avaluation = new HashMap<>();
                        avaluation.put("value","2");
                        avaluation.put("category",AvaluateInterviewActivity.bookmarkName.getText().toString());
                        db.collection("users").document(observedId).collection("avaluations").document(model.getQuestion()+uid).set(avaluation);
                    }
                });
            }
        });
        holder.bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.good.setBackgroundResource(R.drawable.good_emoji);
                    holder.medium.setBackgroundResource(R.drawable.medium_emoji);
                    holder.bad.setBackgroundResource(R.drawable.bad_emoji_sel);
                    db.collection("trainings").document(Globals.currentInterview).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Globals.avaliatedUserId=documentSnapshot.get("observed_uid").toString();
                        String observedId=documentSnapshot.get("observed_uid").toString();
                        String uid=mAuth.getUid();
                        Map<String,Object> avaluation = new HashMap<>();
                        avaluation.put("value","1");
                        avaluation.put("category",AvaluateInterviewActivity.bookmarkName.getText().toString());
                        db.collection("users").document(observedId).collection("avaluations").document(model.getQuestion()+uid).set(avaluation);
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public AvaluationRecycler.BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avaluation_cardview,parent,false);
        return new AvaluationRecycler.BookmarkHolder(view);
    }

    class BookmarkHolder extends RecyclerView.ViewHolder{

        Button bad,medium,good;
        TextView questionName;
        public BookmarkHolder(@NonNull View itemView) {
            super(itemView);
            bad=itemView.findViewById(R.id.badIcon);
            medium=itemView.findViewById(R.id.mediumIcon);
            good=itemView.findViewById(R.id.goodIcon);
            questionName=itemView.findViewById(R.id.questionName);
        }
    }
}
