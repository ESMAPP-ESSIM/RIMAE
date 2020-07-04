package com.example.rimae.recyclers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecycler extends FirestoreRecyclerAdapter<User, UsersRecycler.UserHolder> {
    public UsersRecycler(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersRecycler.UserHolder holder, int position, @NonNull User model) {
        holder.userPoints.setText(String.valueOf(model.getPoints()));
        holder.userName.setText(model.getName());
        holder.position.setText(String.valueOf(position + 1));

        Picasso.get().load(model.getProfile_pic()).fit().centerCrop().into(holder.userProfilePic);
    }

    @NonNull
    @Override
    public UsersRecycler.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_cardview, parent,false);
        return new UserHolder(view);
    }

    class UserHolder extends RecyclerView.ViewHolder{
        TextView position, userName, userPoints;
        CircleImageView userProfilePic;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            userName = itemView.findViewById(R.id.userName);
            userPoints = itemView.findViewById(R.id.userPoints);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
        }
    }
}
