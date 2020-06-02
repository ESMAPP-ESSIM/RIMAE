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

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ParticipantRecycler adapter;
    EditText participantName;
    RecyclerView rParticipant;
    String searchFilter="";
    String active="public";
    String pin="";
    String participantId="";
    EditText interviewTitle,interviewDesc;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_interview, container, false);
        //Get the variables
        interviewTitle= root.findViewById(R.id.interTitle);
        interviewDesc=root.findViewById(R.id.interDesc);

        rParticipant= root.findViewById(R.id.rParticipant);
        participantName=root.findViewById(R.id.interParti);
        startRecycler();
        /*
        *Detetar alteraçoes do filtro
         */
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
        final Button publicBtn=root.findViewById(R.id.publicBtn);

        /*
         * Alterar entre privado e publico
         */
        privateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privateBtn.setBackgroundResource(R.drawable.left_shape_button_green);
                publicBtn.setBackgroundResource(R.drawable.right_shape_button_gray);
                active="private";
                Log.d("Participants",active);
            }
        });

        publicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicBtn.setBackgroundResource(R.drawable.right_shape_button_green);
                privateBtn.setBackgroundResource(R.drawable.left_shape_button_gray);
                active="public";
            }
        });

        /*
        *Criar Entrevista
        *
         */
        Button createBtn = root.findViewById(R.id.nextBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantId= Globals.participantId;

                if(TextUtils.isEmpty(interviewTitle.getText().toString())||TextUtils.isEmpty(interviewDesc.getText().toString())||TextUtils.isEmpty(participantId)){
                   Log.d("Participant",interviewTitle.getText().toString()+interviewDesc.getText().toString()+participantId);
                   Toast.makeText(getContext(),"Por favor preencha todos os campos",Toast.LENGTH_SHORT).show();
               }else{
                   createInterview(participantId);
               }
            }
        });
        return root;
    }
    private  void createInterview(String participantId){
        //If Private Generate random pin and send email with the pin
        if(active.equals("private")){
            Random random=new Random();
            pin=String.format("%04d",random.nextInt(10000));
            Log.d("Participant","Pin:" + pin);
            String creatorUid = FirebaseAuth.getInstance().getUid();
            //Ir buscar o email para enviar
            FirebaseFirestore.getInstance().collection("users").document(creatorUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d("Participant", "Dentro da função: " + task.getResult().get("email").toString());
                        String mail = task.getResult().get("email").toString();
                        String message = "O pin para aceder a esta entrevista é:" +pin;
                        String subject = "Pin da sua entrevista";
                        JavaMailAPI javaMailAPI=new JavaMailAPI(getContext(),mail,subject,message);
                        javaMailAPI.execute();
                        sendVideo();
                    }else{
                        Log.d("Participant","failed");
                    }
                }
            });
        }else{
            sendVideo();
        }

    }

    /*
    *Função para abrir a galeria
     */
    private void sendVideo() {
        //Abrir galeria
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent,0);
    }

    /*
    * Upload Video depois de escolher o video
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0 && resultCode== Activity.RESULT_OK){
            String filename= UUID.randomUUID().toString();
            final StorageReference videoRef= FirebaseStorage.getInstance().getReference().child("videos/"+filename);
            videoRef.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(getContext(),data.getData());
                            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            long timeInMillisec=Long.parseLong(time);
                            String timeMS=String.valueOf(timeInMillisec);
                            retriever.release();
                            //Criar nova Entrevista
                            saveNewInterview(uri.toString(),timeMS);
                        }
                    });
                }
            });
        }
    }

    /*
    *Função para criar uma nova entrevista
     */
    private void saveNewInterview(String uri,String timeInMiliSec) {
        FirebaseFirestore dbTrainings=FirebaseFirestore.getInstance();
        //Mapa da entrevista

        Map<String,Object> training = new HashMap<>();
        training.put("completed",false);
        training.put("description",interviewDesc.getText().toString());
        training.put("name",Globals.participantName);
        training.put("observed_uid",Globals.participantId);
        training.put("owner_uid",FirebaseAuth.getInstance().getUid());
        training.put("profile_pic",Globals.participantPic);
        if(active.equals("public")){
            training.put("public",true);
        }else{
            training.put("public",false);
        }

        training.put("pin",pin);
        training.put("title",interviewTitle.getText().toString());
        training.put("video_url",uri);
        training.put("time" , timeInMiliSec);
        updateUI();

        db.collection("trainings").document().set(training).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               Toast.makeText(getContext(),"Entrevista criada com sucesso",Toast.LENGTH_SHORT).show();
                Globals.participantId="";

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"A Entrevista Não Foi Criada",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    *Função para iniciar a recycler view
    *
    */

    private void startRecycler() {
        Query query= db.collection("users");

        FirestoreRecyclerOptions<Participant> options= new FirestoreRecyclerOptions.Builder<Participant>()
                .setQuery(query, Participant.class).build();

        adapter = new ParticipantRecycler(options);


        rParticipant.setHasFixedSize(true);
        rParticipant.setLayoutManager(new LinearLayoutManager(getContext()));
        rParticipant.setAdapter(adapter);
    }

    /*
    *Função para atualizar o recyclerview
    *
    */

    public void search(String search) {
        Query newQuery = db.collection("users").orderBy("name").startAt(search).endAt(search + "\uf8ff");

        FirestoreRecyclerOptions<Participant> newOptions = new FirestoreRecyclerOptions.Builder<Participant>().setQuery(newQuery, Participant.class).build();

        adapter.updateOptions(newOptions);
        Log.d("Participants",search);
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
