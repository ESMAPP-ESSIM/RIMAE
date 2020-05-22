package com.example.rimae.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Training;
import com.example.rimae.recyclers.HomePageRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    HomePageRecycler adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Query query= db.collection("trainings");

        Log.d("Query","Query"+query.toString());
        FirestoreRecyclerOptions<Training> options= new FirestoreRecyclerOptions.Builder<Training>()
                .setQuery(query, Training.class).build();

        adapter = new HomePageRecycler(options);
        RecyclerView rInterview=root.findViewById(R.id.rEntrevistas);

        rInterview.setHasFixedSize(true);
        rInterview.setLayoutManager(new LinearLayoutManager(getContext()));
        rInterview.setAdapter(adapter);
        return root;
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
