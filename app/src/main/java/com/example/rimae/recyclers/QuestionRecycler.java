package com.example.rimae.recyclers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.models.Question;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class QuestionRecycler extends FirestoreRecyclerAdapter<Question, QuestionRecycler.QuestionHolder> {
    public QuestionRecycler(@NonNull FirestoreRecyclerOptions<Question> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final QuestionRecycler.QuestionHolder holder, int position, @NonNull Question model) {
        holder.question.setText(model.getQuestion());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                String id = snapshot.getId();
                db.collection("bookmarks_categories").document(Globals.currentMarker).collection("questions").document(id).delete();
            }
        });
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_cardview,parent,false);

        return  new QuestionHolder(view);
    }

    class QuestionHolder extends RecyclerView.ViewHolder{
        TextView question;
        Button delete;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            question=itemView.findViewById(R.id.questionText);
            delete=itemView.findViewById(R.id.delQuestion);
        }
    }
}
