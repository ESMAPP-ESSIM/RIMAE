package com.example.rimae.ui.create_interview;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.Main2Activity;
import com.example.rimae.R;
import com.example.rimae.email.JavaMailAPI;
import com.example.rimae.models.Participant;
import com.example.rimae.recyclers.ParticipantRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CreateInterviewFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ParticipantRecycler adapter;
    EditText participantName;
    RecyclerView rParticipant;
    String searchFilter = "";
    String active = "public";
    String pin = "";
    String participantId = "";
    EditText interviewTitle, interviewDesc;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_interview, container, false);

        // Get the user inputs
        interviewTitle = root.findViewById(R.id.interTitle);
        interviewDesc = root.findViewById(R.id.interDesc);
        rParticipant = root.findViewById(R.id.rParticipant);
        participantName = root.findViewById(R.id.interParti);

        startRecycler();

        // Check for filter changes
        participantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter=participantName.getText().toString();
                search(searchFilter);
            }
        });

        final Button privateBtn = root.findViewById(R.id.privateBtn);
        final Button publicBtn = root.findViewById(R.id.publicBtn);

        /*
         * Switch between private and public
         */
        privateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            privateBtn.setBackgroundResource(R.drawable.left_shape_button_green);
            publicBtn.setBackgroundResource(R.drawable.right_shape_button_gray);
            active = "private";
            }
        });

        publicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            publicBtn.setBackgroundResource(R.drawable.right_shape_button_green);
            privateBtn.setBackgroundResource(R.drawable.left_shape_button_gray);
            active = "public";
            }
        });

        /*
        * Create training
        */
        Button createBtn = root.findViewById(R.id.nextBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            participantId = Globals.participantId;

            if (
                    TextUtils.isEmpty(interviewTitle.getText().toString())
                    || TextUtils.isEmpty(interviewDesc.getText().toString())
                    || TextUtils.isEmpty(participantId)
            ) {
                Toast.makeText(getContext(),"Por favor, preencha todos os campos",Toast.LENGTH_SHORT).show();
            } else {
                createInterview(participantId);
            }
            }
        });

        return root;
    }

    /**
     * Used to create a new interview
     *
     * @param participantId the id of the participant
     */
    private  void createInterview(String participantId){
        // If the interview is Private, generates a random pin and sends it by email
        if (active.equals("private")) {
            Random random = new Random();
            String creatorUid = FirebaseAuth.getInstance().getUid();

            pin = String.format("%04d", random.nextInt(10000));

            // Gets the user email which the pin will be sent to
            FirebaseFirestore.getInstance().collection("users").document(creatorUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String mail = task.getResult().get("email").toString();
                    String message = "O pin para aceder a esta entrevista é:" + pin;
                    String subject = "Pin da sua entrevista";

                    JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), mail, subject, message);
                    javaMailAPI.execute();
                    sendVideo();
                } else {
                    Log.d("Participant","failed");
                }
                }
            });
        } else {
            sendVideo();
        }
    }

    /**
     * Allows the user to select a video to be uploaded
     */
    private void sendVideo() {
        // Opens the gallery
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");

        startActivityForResult(intent,0);
    }

    /**
     * Uploads the video after it has been selected
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            String filename = UUID.randomUUID().toString();
            final StorageReference videoRef = FirebaseStorage.getInstance().getReference().child("videos/" + filename);

            videoRef.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(getContext(),data.getData());

                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long timeInMillisec = Long.parseLong(time);
                        String timeMS = String.valueOf(timeInMillisec);

                        retriever.release();

                        // Saves a new interview
                        saveNewInterview(uri.toString(),timeMS);
                    }
                });
                }
            });
        }
    }

    /**
     * Saves a new interview to DB
     *
     * @param uri the uri of the video
     * @param timeInMiliSec the total time of the video in milliseconds
     */
    private void saveNewInterview(String uri, String timeInMiliSec) {
        FirebaseFirestore dbTrainings = FirebaseFirestore.getInstance();

        Map<String,Object> training = new HashMap<>();

        training.put("completed", false);
        training.put("description", interviewDesc.getText().toString());
        training.put("name", Globals.participantName);
        training.put("observed_uid", Globals.participantId);
        training.put("owner_uid", FirebaseAuth.getInstance().getUid());
        training.put("profile_pic", Globals.participantPic);

        if (active.equals("public")) {
            training.put("public", true);
        } else {
            training.put("public", false);
        }

        training.put("pin", pin);
        training.put("title", interviewTitle.getText().toString());
        training.put("video_url", uri);
        training.put("time", timeInMiliSec);

        updateUI();

        db.collection("trainings")
                .document()
                .set(training)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getContext(),"Entrevista criada com sucesso", Toast.LENGTH_SHORT).show();
                Globals.participantId = "";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"A Entrevista Não Foi Criada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startRecycler() {
        Query query = db.collection("users");

        FirestoreRecyclerOptions<Participant> options = new FirestoreRecyclerOptions.Builder<Participant>()
                .setQuery(query, Participant.class).build();

        adapter = new ParticipantRecycler(options);

        rParticipant.setHasFixedSize(true);
        rParticipant.setLayoutManager(new LinearLayoutManager(getContext()));
        rParticipant.setAdapter(adapter);
    }

    public void search(String search) {
        Query newQuery = db.collection("users")
                            .orderBy("name")
                            .startAt(search)
                            .endAt(search + "\uf8ff");

        FirestoreRecyclerOptions<Participant> newOptions =
                new FirestoreRecyclerOptions.Builder<Participant>().setQuery(newQuery, Participant.class).build();

        adapter.updateOptions(newOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void updateUI(){
        Intent intent = new Intent(getContext(), Main2Activity.class);

        startActivity(intent);
    }
}
