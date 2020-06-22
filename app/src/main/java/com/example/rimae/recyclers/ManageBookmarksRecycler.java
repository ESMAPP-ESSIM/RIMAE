package com.example.rimae.recyclers;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Bookmark;
import com.example.rimae.models.VideoBookmark;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ManageBookmarksRecycler  extends FirestoreRecyclerAdapter<Bookmark,ManageBookmarksRecycler.BookmarkHolder>{
    int index = -1;

    public ManageBookmarksRecycler(@NonNull FirestoreRecyclerOptions<Bookmark> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageBookmarksRecycler.BookmarkHolder holder,final int position, @NonNull Bookmark model) {
        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        holder.bookmarkName.setText(model.getName());
        holder.card_view.setCardBackgroundColor(Color.parseColor(model.getColor()));
        String colorAlpha = model.getColor();
        String finalColor = "#26" + colorAlpha.substring(1);
        holder.linearLayout.getBackground().setColorFilter(Color.parseColor(finalColor), PorterDuff.Mode.SRC_ATOP);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
            }
        });

        if (index==position){
            holder.manageBtn.setVisibility(View.VISIBLE);
            holder.linearLayout.setVisibility(View.GONE);
        }else{
            holder.manageBtn.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public ManageBookmarksRecycler.BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_marker_cardview,parent,false);

        return  new ManageBookmarksRecycler.BookmarkHolder(view);
    }

    class BookmarkHolder extends RecyclerView.ViewHolder{
        TextView bookmarkName;
        CardView card_view;
        LinearLayout linearLayout;
        LinearLayout manageBtn;
        Button editBtn,deleteBtn;
        public BookmarkHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkName = itemView.findViewById(R.id.markerName);
            card_view = (CardView) itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.markerInnerColor);
            manageBtn=itemView.findViewById(R.id.manageBtn);
            editBtn=itemView.findViewById(R.id.editBtn);
            deleteBtn=itemView.findViewById(R.id.deleteBtn);
        }
    }
}
