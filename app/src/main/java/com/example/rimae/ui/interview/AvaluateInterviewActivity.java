package com.example.rimae.ui.interview;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.models.Bookmark;
import com.example.rimae.models.Question;
import com.example.rimae.recyclers.AvaluationRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AvaluateInterviewActivity extends AppCompatActivity {

    Integer currentIndex=0;
    ArrayList<Bookmark> bookmarksList= new ArrayList<Bookmark>();
    TextView pager;
    public static TextView bookmarkName;
    View divider;
    Button goBack,goNext;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    RecyclerView rQuestion;
    AvaluationRecycler adapter;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaluate_interview);
        rQuestion=findViewById(R.id.rQuestions);
        pager = findViewById(R.id.pager);
        bookmarkName=findViewById(R.id.bookmarkName);
        divider= findViewById(R.id.divider);
        goNext=findViewById(R.id.goNext);
        goBack=findViewById(R.id.goBack);
        db.collection("bookmarks_categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        bookmarksList.add(new Bookmark(document.getId(),document.get("name").toString(),document.get("color").toString()));
                    }
                    updateUI(bookmarksList.get(currentIndex).name,bookmarksList.get(currentIndex).color,currentIndex,bookmarksList.size());
                    startRecycler(bookmarksList.get(currentIndex).id);
                    goNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(currentIndex<bookmarksList.size()-1){
                              currentIndex++;
                              updateUI(bookmarksList.get(currentIndex).name,bookmarksList.get(currentIndex).color,currentIndex,bookmarksList.size());
                              update(bookmarksList.get(currentIndex).id);
                           }
                        }
                    });
                    goBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(currentIndex>0){
                                currentIndex--;
                                updateUI(bookmarksList.get(currentIndex).name,bookmarksList.get(currentIndex).color,currentIndex,bookmarksList.size());
                                update(bookmarksList.get(currentIndex).id);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     *
     * @param name
     * @param color
     * @param currentIndex
     * @param size
     */
    public void updateUI(String name,String color, int currentIndex,int size){
        bookmarkName.setText(bookmarksList.get(currentIndex).name);
        divider.setBackgroundColor(Color.parseColor(bookmarksList.get(currentIndex).color));
        pager.setText((currentIndex + 1) +" / "+size);
    }

    public void finish(View view){
        if (currentIndex.equals(bookmarksList.size()-1)){
            Map<String,Object> userAvaluation = new HashMap<>();
            userAvaluation.put("avaluated",true);
            db.collection("trainings").document(Globals.currentInterview).collection("users").document(mAuth.getUid()).set(userAvaluation);
            db.collection("users").document(Globals.avaliatedUserId).collection("avaluations").orderBy("category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document:task.getResult()){
                            db.collection("users").document(Globals.avaliatedUserId).update(document.get("category").toString(), FieldValue.increment(Integer.parseInt(document.get("value").toString())));
                            db.collection("users").document(Globals.avaliatedUserId).update("points",FieldValue.increment(Integer.parseInt(document.get("value").toString())));
                        }
                    }
                }
            });
            finish();
        }
    }

    /**
     *
     * @param view
     */
    public void review(View view){
        Intent intent = new Intent(this,VideoFilteredActivity.class);
        intent.putExtra("interviewId",Globals.currentInterview);
        intent.putExtra("category",bookmarkName.getText().toString());
        startActivity(intent);
    }
    public void startRecycler(String bookmarkCategory){
        Query query =db.collection("bookmarks_categories").document(bookmarkCategory).collection("questions");
        FirestoreRecyclerOptions<Question> options = new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query,Question.class).build();

        adapter=new AvaluationRecycler(options);
        rQuestion.setHasFixedSize(true);
        rQuestion.setLayoutManager(new LinearLayoutManager(this));
        rQuestion.setAdapter(adapter);
        adapter.startListening();
    }

    /**
     *
     * @param bookmarkCategory
     */
    public void update(String bookmarkCategory){
        Query query = db.collection("bookmarks_categories").document(bookmarkCategory).collection("questions");
        FirestoreRecyclerOptions<Question> newOptions = new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query,Question.class).build();
        adapter.updateOptions(newOptions);
    }

    /**
     *
     * @param view
     */
    public  void goBack(View view){
        finish();
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}
