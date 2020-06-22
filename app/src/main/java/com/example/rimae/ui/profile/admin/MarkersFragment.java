package com.example.rimae.ui.profile.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.rimae.recyclers.ManageBookmarksRecycler;
import com.example.rimae.ui.interview.BookmarksInterview;
import com.example.rimae.ui.profile.ProfileFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MarkersFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ManageBookmarksRecycler adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_manage_markers, container, false);

        //Voltar atrás
        Button back=root.findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });

        //Iniciar Recycler View
        Query query=db.collection("bookmarks_categories");
        FirestoreRecyclerOptions<Bookmark> options = new FirestoreRecyclerOptions.Builder<Bookmark>()
                .setQuery(query,Bookmark.class).build();
        adapter=new ManageBookmarksRecycler(options);
        RecyclerView rBookmarks= root.findViewById(R.id.rBookmarks);
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
        adapter.stopListening();
    }
    public void updateUI(){
        ProfileFragment fragment2 = new ProfileFragment();
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
