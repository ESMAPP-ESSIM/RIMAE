package com.example.rimae.ui.my_interviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Training;
import com.example.rimae.recyclers.HomePageRecycler;
import com.example.rimae.recyclers.MyInterviewsRecycler;
import com.example.rimae.ui.profile.ProfileFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyInterviewsFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    MyInterviewsRecycler adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_my_interviews, container, false);

        // Buscar entrevistas onde o observed id ou o owner id é do utilizador autenticado
        Query query = db.collection("trainings");

        FirestoreRecyclerOptions<Training> options= new FirestoreRecyclerOptions.Builder<Training>()
                .setQuery(query, Training.class).build();

        adapter = new MyInterviewsRecycler(options);

        RecyclerView rInterview = root.findViewById(R.id.rInterviews);

        rInterview.setHasFixedSize(true);
        rInterview.setLayoutManager(new LinearLayoutManager(getContext()));
        rInterview.setAdapter(adapter);

        //Back Button
        Button back = root.findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        return root;
    }

    //Back to profile fragment
    public void goBack() {
        ProfileFragment fragment2 = new ProfileFragment();
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
