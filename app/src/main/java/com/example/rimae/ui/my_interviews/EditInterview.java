package com.example.rimae.ui.my_interviews;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.VideoActivity;
import com.example.rimae.models.Participant;
import com.example.rimae.recyclers.ParticipantRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EditInterview extends AppCompatActivity {

    EditText interTitle,interDesc,interPart;
    String searchFilter="";
    Button saveBtn,viewBtn,deleteBtn;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ParticipantRecycler adapter;
    RecyclerView rParticipant;
    String interviewId="";
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_interview);
        rParticipant=findViewById(R.id.rParticipant);
        //Ir buscar variaveis
        interTitle=findViewById(R.id.interviewTitle);
        interDesc= findViewById(R.id.interviewDesc);
        interPart=findViewById(R.id.interParti);
        saveBtn=findViewById(R.id.saveBtn);
        viewBtn=findViewById(R.id.nextBtn);
        deleteBtn=findViewById(R.id.deleteBtn);

        interviewId=getIntent().getStringExtra("interviewId");

        db.collection("trainings").document(interviewId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    interTitle.setText(task.getResult().get("title").toString());
                    interDesc.setText(task.getResult().get("description").toString());
                    interPart.setText(task.getResult().get("name").toString());
                    if(!task.getResult().get("owner_uid").toString().equals(mAuth.getUid())){
                        saveBtn.setVisibility(View.GONE);
                        deleteBtn.setVisibility(View.GONE);
                        interTitle.setEnabled(false);
                        interDesc.setEnabled(false);
                        interPart.setEnabled(false);
                    }
                }
            }
        });
        //Começar filtro de recycler e recycler
        startRecycler();
        interPart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter=interPart.getText().toString();
                search(searchFilter);
            }
        });
    }

    /*
     *Função para atualizar o recyclerview
     *
     */

    /**
     *
     * @param search
     */
    private void search(String search) {
        Query newQuery = db.collection("users").orderBy("name").startAt(search).endAt(search + "\uf8ff");

        FirestoreRecyclerOptions<Participant> newOptions = new FirestoreRecyclerOptions.Builder<Participant>().setQuery(newQuery, Participant.class).build();

        adapter.updateOptions(newOptions);
        Log.d("Participants",search);
    }

    /*
     *Função para iniciar a recycler view
     *
     */

    /**
     *
     */
    private void startRecycler() {
        Query query= db.collection("users");

        FirestoreRecyclerOptions<Participant> options= new FirestoreRecyclerOptions.Builder<Participant>()
                .setQuery(query, Participant.class).build();

        adapter = new ParticipantRecycler(options);


        rParticipant.setHasFixedSize(true);
        rParticipant.setLayoutManager(new LinearLayoutManager(this));
        rParticipant.setAdapter(adapter);
    }

    /**
     *
     * @param view
     */
    public void back(View view){
        finish();
    }

    /**
     *
     * @param view
     */
    public void viewInterview(View view){
        Intent intent = new Intent(this, MyVideoActivity.class);
        intent.putExtra("interviewId",interviewId);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @param view
     */
    public void deleteBtn(View view){
        db.collection("trainings").document(interviewId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditInterview.this,"Entrevista Removida com sucesso",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     * @param view
     */
    public void editInterview(View view){
        db.collection("trainings").document(interviewId).update("title",interTitle.getText().toString());
        db.collection("trainings").document(interviewId).update("description",interDesc.getText().toString());
        if(!Globals.participantId.isEmpty()){
            db.collection("trainings").document(interviewId).update("observed_uid",Globals.participantId);
            db.collection("trainings").document(interviewId).update("name",Globals.participantName);
            db.collection("trainings").document(interviewId).update("profile_pic",Globals.participantPic);
        }
        Toast.makeText(EditInterview.this,"Entrevista Alterada com sucesso",Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /**
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
