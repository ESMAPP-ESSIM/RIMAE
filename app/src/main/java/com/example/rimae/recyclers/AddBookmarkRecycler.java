package com.example.rimae.recyclers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.VideoActivity;
import com.example.rimae.models.Bookmark;
import com.example.rimae.ui.interview.BookmarksInterview;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBookmarkRecycler extends FirestoreRecyclerAdapter<Bookmark, AddBookmarkRecycler.BookmarkHolder> {
    int index=-1;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public AddBookmarkRecycler(@NonNull FirestoreRecyclerOptions<Bookmark> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final BookmarkHolder holder, final int position, @NonNull final Bookmark model) {
        DocumentSnapshot snapshot=getSnapshots().getSnapshot(holder.getAdapterPosition());
        Log.d("Bookmark",snapshot.get("name").toString());
        holder.bookmarkName.setText(model.getName());
        holder.card_view.setCardBackgroundColor(Color.parseColor(model.getColor()));
        String colorAlpha= model.getColor();
        String finalColor="#26"+colorAlpha.substring(1);
        Log.d("Bookmark",finalColor);
        holder.linearLayout.getBackground().setColorFilter(Color.parseColor(finalColor), PorterDuff.Mode.SRC_ATOP);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=position;
                notifyDataSetChanged();
            }
        });
        if (index==position){
            holder.emojis.setVisibility(View.VISIBLE);
        }else{
            holder.emojis.setVisibility(View.GONE);
        }

        holder.bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addBookmark(model.getName(),"bad",holder.itemView.getContext(),model.getColor());
               updateUI(holder.itemView.getContext());
            }
        });

        holder.medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(model.getName(),"medium",holder.itemView.getContext(),model.getColor());
                updateUI(holder.itemView.getContext());
            }
        });

        holder.good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmark(model.getName(),"good",holder.itemView.getContext(),model.getColor());
                updateUI(holder.itemView.getContext());

            }
        });
    }

    /**
     * Mudar de fragmento
     */
    private void updateUI(Context context){
        BookmarksInterview fragment2=new BookmarksInterview();
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    /*
    * Função para acrescentar bookmark no video
    *
    * Recebe a categoria onde vai acrescentar, o tipo(bom,medio,mau) e o tempo
    *
     */
    private void addBookmark(String category, String type, final Context mContext,String color) {
        long timeInMillisec = VideoActivity.player.getCurrentPosition();
        String timeMS=String.valueOf(timeInMillisec);
        Map<String,Object> bookmark = new HashMap<>();
        bookmark.put("type",type);
        bookmark.put("category",category);
        bookmark.put("time",timeMS);
        bookmark.put("color",color);
        bookmark.put("userId", FirebaseAuth.getInstance().getUid());
        db.collection("trainings").document(Globals.currentInterview).collection("bookmarks").document().set(bookmark).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext,"Bookmark adicionado com sucesso",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,"Erro ao acrescentar Bookmark",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marker_cardview, parent, false);
        return new BookmarkHolder(view);
    }

    class BookmarkHolder extends RecyclerView.ViewHolder{
        TextView bookmarkName;
        CardView card_view;
        LinearLayout linearLayout;
        LinearLayout emojis;
        ImageButton bad,medium,good;
        public BookmarkHolder(View itemView) {
            super(itemView);
            bookmarkName = itemView.findViewById(R.id.markerName);
            card_view= (CardView) itemView.findViewById(R.id.cardView);
            linearLayout= itemView.findViewById(R.id.markerInnerColor);
            emojis=itemView.findViewById(R.id.emojis);
            bad=itemView.findViewById(R.id.badIcon);
            medium=itemView.findViewById(R.id.mediumIcon);
            good=itemView.findViewById(R.id.goodIcon);
        }
    }
}
