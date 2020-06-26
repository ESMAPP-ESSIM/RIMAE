package com.example.rimae.recyclers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.Main2Activity;
import com.example.rimae.R;
import com.example.rimae.models.Bookmark;
import com.example.rimae.models.VideoBookmark;
import com.example.rimae.ui.profile.admin.EditMarkerFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageBookmarksRecycler  extends FirestoreRecyclerAdapter<Bookmark,ManageBookmarksRecycler.BookmarkHolder>{
    int index = -1;
    Context context;
    public ManageBookmarksRecycler(@NonNull FirestoreRecyclerOptions<Bookmark> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ManageBookmarksRecycler.BookmarkHolder holder, final int position, @NonNull Bookmark model) {
        final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

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

            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globals.currentMarker=snapshot.getId();
                    Log.d("Marker","Marker atual:" + Globals.currentMarker);
                    EditMarkerFragment fragment2= new EditMarkerFragment();
                    FragmentManager manager=((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=manager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("bookmarks_categories").document(snapshot.getId()).delete();
                }
            });
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
            card_view = itemView.findViewById(R.id.cardView);
            linearLayout = itemView.findViewById(R.id.markerInnerColor);
            manageBtn=itemView.findViewById(R.id.manageBtn);
            editBtn=itemView.findViewById(R.id.editBtn);
            deleteBtn=itemView.findViewById(R.id.removeBtn);
            context=itemView.getContext();
        }
    }
}
