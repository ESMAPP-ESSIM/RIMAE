package com.example.rimae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.rimae.models.Training;
import com.example.rimae.recyclers.HomePageRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView rInterview;
<<<<<<< HEAD
    HomePageRecycler adapter;
=======
    MyAdapterRecycler adapter;

>>>>>>> 2ee09a91a575a84fd909cd202a9eddd004abfc76
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        rInterview=findViewById(R.id.rEntrevistas);
        Query query= db.collection("trainings");

        Log.d("Query","Query"+query.toString());
        FirestoreRecyclerOptions<Training> options= new FirestoreRecyclerOptions.Builder<Training>()
                .setQuery(query, Training.class).build();
=======

        rInterview = findViewById(R.id.rEntrevistas);

        Query query = db.collection("trainings");

        Log.d("Query","Query" + query.toString());

        FirestoreRecyclerOptions<Interview> options = new FirestoreRecyclerOptions.Builder<Interview>()
                .setQuery(query,Interview.class).build();
>>>>>>> 2ee09a91a575a84fd909cd202a9eddd004abfc76

        adapter = new HomePageRecycler(options);

        rInterview.setHasFixedSize(true);
        rInterview.setLayoutManager(new LinearLayoutManager(this));
        rInterview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
