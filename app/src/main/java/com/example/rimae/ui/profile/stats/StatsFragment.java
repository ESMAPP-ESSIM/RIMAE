package com.example.rimae.ui.profile.stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rimae.R;
import com.example.rimae.ui.profile.ProfileFragment;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StatsFragment extends Fragment {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    RadarChart RadarChart;
    RadarData radarData;
    RadarDataSet radarDataSet;
    ArrayList radarEntries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root= inflater.inflate(R.layout.fragment_chart,container,false);
        RadarChart=root.findViewById(R.id.RadarChart);
        radarData=new RadarData();
        db.collection("users").document(mAuth.getUid()).collection("avaluations").orderBy("category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (final QueryDocumentSnapshot document:task.getResult()){
                        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    if(!labels.contains(document.get("category").toString())) {
                                        radarEntries.add(new RadarEntry(Integer.parseInt(task.getResult().get(document.get("category").toString()).toString())));
                                        labels.add(document.get("category").toString());
                                        radarDataSet = new RadarDataSet(radarEntries, "");
                                        radarDataSet.setColor(Color.RED);
                                        radarDataSet.setValueTextColor(Color.BLACK);
                                        radarDataSet.setValueTextSize(18f);
                                        XAxis xAxis = RadarChart.getXAxis();
                                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                                        radarData.addDataSet(radarDataSet);
                                        RadarChart.setData(radarData);
                                        RadarChart.invalidate();
                                    }

                                }
                            }
                        });

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

        TextView goRankings=root.findViewById(R.id.rankings);
        goRankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingFragment fragment2 = new RankingFragment();
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return root;
    }

}
