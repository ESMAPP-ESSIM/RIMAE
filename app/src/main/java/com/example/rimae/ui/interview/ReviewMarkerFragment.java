package com.example.rimae.ui.interview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.models.VideoBookmark;
import com.example.rimae.recyclers.ListBookmarksRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ReviewMarkerFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListBookmarksRecycler adapter;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_review_markers, container, false);

        String interviewId = getActivity().getIntent().getStringExtra("interviewId");
        String bookmarkCategory=getActivity().getIntent().getStringExtra("category");
        Globals.currentInterview = interviewId;
        Button close= root.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        Query query = db.collection("trainings").document(interviewId).collection("bookmarks").whereEqualTo("category",bookmarkCategory);
        FirestoreRecyclerOptions<VideoBookmark> options = new FirestoreRecyclerOptions.Builder<VideoBookmark>()
                .setQuery(query,VideoBookmark.class).build();

        adapter = new ListBookmarksRecycler(options);
        RecyclerView rBookmarks = root.findViewById(R.id.rBookmarks);

        rBookmarks.setHasFixedSize(true);
        rBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        rBookmarks.setAdapter(adapter);

        return root;
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /**
     *
     */
    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }
}
