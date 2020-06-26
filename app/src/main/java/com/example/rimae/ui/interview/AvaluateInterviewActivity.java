package com.example.rimae.ui.interview;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rimae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class AvaluateInterviewActivity extends AppCompatActivity {

    Integer currentIndex=0;
    ArrayList<Bookmark> bookmarksList= new ArrayList<Bookmark>();
    TextView pager,bookmarkName;
    View divider;
    Button goBack,goNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaluate_interview);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
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
                    goNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(currentIndex<bookmarksList.size()){
                              currentIndex++;
                              updateUI(bookmarksList.get(currentIndex).name,bookmarksList.get(currentIndex).color,currentIndex,bookmarksList.size());
                           }
                        }
                    });
                    goBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(currentIndex>0){
                                currentIndex--;
                                updateUI(bookmarksList.get(currentIndex).name,bookmarksList.get(currentIndex).color,currentIndex,bookmarksList.size());
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateUI(String name,String color, int currentIndex,int size){
        bookmarkName.setText(bookmarksList.get(currentIndex).name);
        divider.setBackgroundColor(Color.parseColor(bookmarksList.get(currentIndex).color));
        pager.setText(String.valueOf(currentIndex+1)+" / "+size);
    }
    public  void goBack(View view){
        finish();
    }



    class Bookmark{

        String id,name,color;

        public Bookmark(String id, String name, String color) {
            this.id=id;
            this.name=name;
            this.color=color;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
