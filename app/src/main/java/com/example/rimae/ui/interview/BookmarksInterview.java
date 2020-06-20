package com.example.rimae.ui.interview;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
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

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.VideoActivity;
import com.example.rimae.models.VideoBookmark;
import com.example.rimae.recyclers.ListBookmarksRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BookmarksInterview extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListBookmarksRecycler adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_video_markers_times, container, false);
        String interviewId = getActivity().getIntent().getStringExtra("interviewId");
        Globals.currentInterview = interviewId;

        Log.d("Interview","Fragment " + interviewId);

        // Botão para adicionar marcador
        Button markbtn = root.findViewById(R.id.markBtn);

        markbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleExoPlayer player = VideoActivity.player;
                player.setPlayWhenReady(false);

                // open add bookmark
                AddBookmarInterview fragment2 = new AddBookmarInterview();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container,fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
             }
        });

        //Botão para terminar entrevista



        //Iniciar RecyclerView
        Query query = db.collection("trainings").document(interviewId).collection("bookmarks").whereEqualTo("userId", FirebaseAuth.getInstance().getUid());
        FirestoreRecyclerOptions<VideoBookmark> options = new FirestoreRecyclerOptions.Builder<VideoBookmark>()
                .setQuery(query,VideoBookmark.class).build();

        adapter = new ListBookmarksRecycler(options);

        RecyclerView rBookmarks = root.findViewById(R.id.rBookmarks);

        rBookmarks.setHasFixedSize(true);
        rBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        rBookmarks.setAdapter(adapter);

        return  root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }
}
