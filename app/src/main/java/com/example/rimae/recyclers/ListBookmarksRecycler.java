package com.example.rimae.recyclers;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.Bookmark;
import com.example.rimae.models.VideoBookmark;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListBookmarksRecycler extends FirestoreRecyclerAdapter<VideoBookmark, ListBookmarksRecycler.BookmarkHolder> {

    public ListBookmarksRecycler(@NonNull FirestoreRecyclerOptions<VideoBookmark> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListBookmarksRecycler.BookmarkHolder holder, int position, @NonNull VideoBookmark model) {
        Long time = Long.parseLong(model.getTime());
        holder.time.setText(new SimpleDateFormat("mm:ss").format(new Date(time)));
        holder.category.setText(model.getCategory());
        if (model.getType().equals("bad")){
            Picasso.get().load(R.drawable.bad_emoji).fit().centerCrop().into(holder.icon);
        }else if(model.getType().equals("medium")){
            Picasso.get().load(R.drawable.medium_emoji).fit().centerCrop().into(holder.icon);
        }else{
            Picasso.get().load(R.drawable.good_emoji).fit().centerCrop().into(holder.icon);
        }

        String colorAlpha= model.getColor();
        String finalColor="#26"+colorAlpha.substring(1);
        holder.linearLayout.getBackground().setColorFilter(Color.parseColor(finalColor), PorterDuff.Mode.SRC_ATOP);
    }

    @NonNull
    @Override
    public ListBookmarksRecycler.BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_icon_time_cardview, parent, false);
        return new BookmarkHolder(view);
    }

    class BookmarkHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView category;
        ImageView icon;
        LinearLayout linearLayout;
        public BookmarkHolder(View itemView) {
            super(itemView);
            time= itemView.findViewById(R.id.timeView);
            category=itemView.findViewById(R.id.category);
            linearLayout= itemView.findViewById(R.id.markerInnerColor);
            icon=itemView.findViewById(R.id.iconView);
        }
    }
}
