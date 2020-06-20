package com.example.rimae.ui.interview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Bookmark;
import com.example.rimae.recyclers.AddBookmarkRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AddBookmarInterview extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AddBookmarkRecycler adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_interview_add_bookmark, container, false);

        //Voltar atrás
        ImageButton back=root.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });

        //Iniciar Recycler View
        Query query = db.collection("bookmarks_categories");
        FirestoreRecyclerOptions<Bookmark> options= new FirestoreRecyclerOptions.Builder<Bookmark>()
                .setQuery(query, Bookmark.class).build();

        adapter = new AddBookmarkRecycler(options);
        RecyclerView rBookmarks = root.findViewById(R.id.rBookmarkList);

        rBookmarks.setHasFixedSize(true);
        rBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        rBookmarks.setAdapter(adapter);
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
    public void updateUI(){
        BookmarksInterview fragment2 = new BookmarksInterview();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
