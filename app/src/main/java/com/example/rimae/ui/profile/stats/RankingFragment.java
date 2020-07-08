package com.example.rimae.ui.profile.stats;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.R;
import com.example.rimae.models.User;
import com.example.rimae.recyclers.UsersRecycler;
import com.example.rimae.ui.profile.ProfileFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingFragment extends Fragment {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    UsersRecycler rAdapter;
    RecyclerView rUsers;
    Boolean spinnerInitialized=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root= inflater.inflate(R.layout.fragment_rankings,container,false);
        final TextView place = root.findViewById(R.id.place);
        final TextView points=root.findViewById(R.id.points);
        final CircleImageView profilePhoto=root.findViewById(R.id.profilePhoto);
        db.collection("users").orderBy("points", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int index=1;
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        if (documentSnapshot.getId().equals(mAuth.getUid())){
                            place.setText(String.valueOf(index)+"º");
                            points.setText(documentSnapshot.get("points").toString());
                            Picasso.get().load(documentSnapshot.get("profile_pic").toString()).fit().centerCrop().into(profilePhoto);
                        }
                        index++;
                    }
                }
            }
        });
        Button backButton=root.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment2 = new ProfileFragment();
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        TextView goProfile=root.findViewById(R.id.perfil);
        goProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatsFragment fragment2 = new StatsFragment();
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        rUsers=root.findViewById(R.id.rUsers);
        startRecycler();
        final Spinner spinner= root.findViewById(R.id.tiersSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.tiers_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("RANKING", "onItemSelected: "+spinner.getSelectedItem().toString());
                if(!spinnerInitialized){
                    spinnerInitialized=true;
                    return;
                }
                search(spinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return root;
    }

    private void search(String tier) {
        Query query;
        if (tier.equals("Bronze")){
            Log.d("RANKING", "tier: "+tier);
            query = db.collection("users").orderBy("points", Query.Direction.DESCENDING).whereLessThan("points",150);
        }else if (tier.equals("Silver")){
            Log.d("RANKING", "tier: "+tier);
            query = db.collection("users").orderBy("points", Query.Direction.DESCENDING).whereGreaterThanOrEqualTo("points",150).whereLessThan("points",250);
        }else{
            Log.d("RANKING", "tier: "+tier);
            query = db.collection("users").orderBy("points", Query.Direction.DESCENDING).whereGreaterThanOrEqualTo("points",250);
        }
        FirestoreRecyclerOptions<User> newOptions= new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class).build();
        rAdapter.updateOptions(newOptions);
    }
    public void startRecycler(){
        Query query = db.collection("users").orderBy("points", Query.Direction.DESCENDING).whereGreaterThanOrEqualTo("points",250);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class).build();
        rAdapter=new UsersRecycler(options);
        rUsers.setHasFixedSize(true);
        rUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rUsers.setAdapter(rAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        rAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        rAdapter.stopListening();
    }
}
