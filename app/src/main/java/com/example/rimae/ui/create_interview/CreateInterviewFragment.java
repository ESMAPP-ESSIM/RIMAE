package com.example.rimae.ui.create_interview;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Participant;
import com.example.rimae.models.Training;
import com.example.rimae.recyclers.HomePageRecycler;
import com.example.rimae.recyclers.ParticipantRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CreateInterviewFragment extends Fragment {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ParticipantRecycler adapter;
    EditText participantName;
    RecyclerView rParticipant;
    String searchFilter="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create_interview, container, false);
        rParticipant= root.findViewById(R.id.rParticipant);
        participantName=root.findViewById(R.id.interParti);
        startRecycler();
        participantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter=participantName.getText().toString();
                search(searchFilter);
            }
        });
        return root;
    }

    private void startRecycler() {
        Query query= db.collection("users");

        FirestoreRecyclerOptions<Participant> options= new FirestoreRecyclerOptions.Builder<Participant>()
                .setQuery(query, Participant.class).build();

        adapter = new ParticipantRecycler(options);


        rParticipant.setHasFixedSize(true);
        rParticipant.setLayoutManager(new LinearLayoutManager(getContext()));
        rParticipant.setAdapter(adapter);
    }

    public void search(String search) {
        Query newQuery = db.collection("users").orderBy("name").startAt(search).endAt(search + "\uf8ff");

        FirestoreRecyclerOptions<Participant> newOptions = new FirestoreRecyclerOptions.Builder<Participant>().setQuery(newQuery, Participant.class).build();

        adapter.updateOptions(newOptions);
        Log.d("Participants",search);
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
