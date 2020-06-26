package com.example.rimae.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rimae.BeforeLoginActivity;
import com.example.rimae.ui.my_interviews.MyInterviewsFragment;
import com.example.rimae.ui.profile.admin.MarkersFragment;
import com.example.rimae.ui.profile.definitions.DefinitionsActivity;
import com.example.rimae.R;
import com.example.rimae.ui.profile.stats.StatsFragment;
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

        // Ir buscar informação sobre o utilizador logged
        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //Atualizar o ImageView e o TextView com a informação correspondente do utilizador
                    String uri = task.getResult().get("profile_pic").toString();
                    String name = task.getResult().get("name").toString();

                    ImageView profilePhoto = root.findViewById(R.id.profilePhoto);
                    Picasso.get().load(uri).fit().centerCrop().into(profilePhoto);

                    TextView profileName = root.findViewById(R.id.profileName);
                    profileName.setText(name);
                } else {
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

        LinearLayout buttonMyInterviews = root.findViewById(R.id.buttonInterviews);
        buttonMyInterviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyInterviews();
            }
        });

        LinearLayout buttonManageMarkers= root.findViewById(R.id.buttonAdmin);
        buttonManageMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdmin();
            }
        });

        LinearLayout buttonStats=root.findViewById(R.id.buttonStats);
        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStats();
            }
        });

        return root;
    }

    public void goToMyInterviews(){
        MyInterviewsFragment fragment2 = new MyInterviewsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToSettings(){
        DefinitionsActivity fragment2 = new DefinitionsActivity();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToStats(){
        StatsFragment fragment2 = new StatsFragment();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToAdmin(){
        MarkersFragment fragment2 = new MarkersFragment();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getContext(), BeforeLoginActivity.class);

        startActivity(intent);
        getActivity().finish();
    }
}
