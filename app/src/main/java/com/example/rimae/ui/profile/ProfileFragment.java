package com.example.rimae.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rimae.BeforeLoginActivity;
import com.example.rimae.DefinitionsActivity;
import com.example.rimae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getUid();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //Ir buscar informação sobre o utilizador logged
        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //Atualizar o ImageView e o TextView com a informação correspondente do utilizador
                    String uri=task.getResult().get("profile_pic").toString();
                    String name = task.getResult().get("name").toString();
                    ImageView profilePhoto=root.findViewById(R.id.profilePhoto);
                    Picasso.get().load(uri).fit().centerCrop().into(profilePhoto);
                    TextView profileName = root.findViewById(R.id.profileName);
                    profileName.setText(name);
                }else{
                    Log.w("Document",task.getException());
                }
            }
        });

        //Add ClickListeners

        LinearLayout buttonLogout = root.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        LinearLayout buttonSettings = root.findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });



    return root;
    }

    public void goToMyInterviews(){

    }

    public void goToSettings(){
        Intent  intent = new Intent(getContext(), DefinitionsActivity.class);
        startActivity(intent);
    }

    public void goToStats(){}

    public void goToAdmin(){}

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), BeforeLoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
