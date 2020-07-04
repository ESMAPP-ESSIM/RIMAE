package com.example.rimae.ui.interview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.rimae.Globals;
import com.example.rimae.Main2Activity;
import com.example.rimae.R;
import com.example.rimae.VideoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DescriptionInterview extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    // Get Ids to query for information
    String observerId;
    String participantID;
    String pinCode ="";
    String interviewId ="";
    EditText pin;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_details);
        interviewId = getIntent().getStringExtra("interviewId");

        Log.d("Entrevista",interviewId);

        //Get Inputs
        final TextView title = findViewById(R.id.interviewTitle);
        final TextView description = findViewById(R.id.interviewDesc);
        final ImageView participantPhoto = findViewById(R.id.participant_photo);
        final TextView observerName = findViewById(R.id.interviewOwner);
        final ImageView observerPhoto = findViewById(R.id.creater_photo);
        final TextView participantName = findViewById(R.id.interviewParticipant);

        pin = findViewById(R.id.pin);

        //Get Interview Details
        db.collection("trainings").document(interviewId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    title.setText(task.getResult().get("title").toString());
                    description.setText(task.getResult().get("description").toString());

                    String uri = task.getResult().get("profile_pic").toString();

                    Picasso.get().load(uri).fit().centerCrop().into(participantPhoto);

                    observerId = task.getResult().get("owner_uid").toString();
                    participantID=task.getResult().get("observed_uid").toString();
                    if (participantID.equals(mAuth.getUid())){
                        Button goVideo = findViewById(R.id.button);
                        goVideo.setVisibility(View.GONE);
                    }
                    Log.d("IDS","IDS: " + observerId + " " + participantID);
                    if (!task.getResult().get("public").toString().equals("true")) {
                        pin.setVisibility(View.VISIBLE);
                        pinCode = task.getResult().get("pin").toString();
                    }
                    db.collection("users").document(observerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            observerName.setText(task.getResult().get("name").toString());

                            String uri = task.getResult().get("profile_pic").toString();

                            Picasso.get().load(uri).fit().centerCrop().into(observerPhoto);
                        }
                    });
                    db.collection("users").document(participantID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            participantName.setText(task.getResult().get("name").toString());
                        }
                    });
                }
            }
        });

    }

    /**
     *
     * @param view
     */
    public void back(View view) {
        Intent intent = new Intent(this, Main2Activity.class);

        startActivity(intent);
        finish();
    }

    /**
     *
     * @param view
     */
    public void goVideo(View view) {
        if (pinCode.isEmpty()) {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("interviewId",interviewId);

            startActivity(intent);
            finish();
        } else {
            if (pin.getText().toString().equals(pinCode)) {
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra("interviewId",interviewId);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,"Pin Code doesnt match",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
