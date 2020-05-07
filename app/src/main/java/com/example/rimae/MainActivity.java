package com.example.rimae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rInterview;
    private ArrayList<Interview> interviews;
    private MyAdapterRecycler interviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rInterview=findViewById(R.id.rEntrevistas);
        rInterview.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rInterview.setLayoutManager(llm);


        interviews = new ArrayList<Interview>();
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));
        interviews.add(
                new Interview("Master Of Puppets", "Metallica","23:52", R.drawable.picture1));

       interviewAdapter = new MyAdapterRecycler(interviews);
        rInterview.setAdapter(interviewAdapter);
    }
}
