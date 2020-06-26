package com.example.rimae.ui.profile.admin;

import android.os.Build;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rimae.Globals;
import com.example.rimae.R;
import com.example.rimae.models.Question;
import com.example.rimae.recyclers.QuestionRecycler;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.HashMap;
import java.util.Map;


public class EditMarkerFragment extends Fragment {

    EditText markerName,newQuestion;
    Button chooseColor,addQuestion,saveChanges,back;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    QuestionRecycler adapter;
    RecyclerView rQuestions;
    String markerID=Globals.currentMarker;
    ColorPicker colorPicker;
    String newHexCode="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root=inflater.inflate(R.layout.fragment_edit_marker,container,false);

        back=root.findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });
        markerName=root.findViewById(R.id.markerName);
        db.collection("bookmarks_categories").document(markerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    markerName.setText(task.getResult().get("name").toString());
                    String color = task.getResult().get("color").toString();
                    int red =Integer.valueOf(color.substring(1,3),16);
                    int green=Integer.valueOf(color.substring(3,5),16);
                    int blue = Integer.valueOf(color.substring(5,7),16);
                    Log.d("Marker","Color"+ red + green + blue);
                    colorPicker = new ColorPicker(getActivity(),red,green,blue);
                    chooseColor=root.findViewById(R.id.chooseColor);
                    chooseColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            colorPicker.show();
                            Button okColor=colorPicker.findViewById(R.id.okColorButton);
                            okColor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText hexCode=colorPicker.findViewById(R.id.codHex);
                                    newHexCode="#"+hexCode.getText().toString();
                                    Log.d("Marker","Nova cor"+newHexCode);
                                    colorPicker.dismiss();
                                }
                            });
                        }
                    });

                }
            }
        });

        Query query = db.collection("bookmarks_categories").document(markerID).collection("questions");
        FirestoreRecyclerOptions<Question> options=new FirestoreRecyclerOptions.Builder<Question>()
                .setQuery(query,Question.class).build();
        adapter=new QuestionRecycler(options);
        rQuestions=root.findViewById(R.id.rQuestions);
        rQuestions.setHasFixedSize(true);
        rQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rQuestions.setAdapter(adapter);
        saveChanges=root.findViewById(R.id.nextBtn);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("bookmarks_categories").document(markerID).update("name",markerName.getText().toString());
                if(!newHexCode.isEmpty()){
                    db.collection("bookmarks_categories").document(markerID).update("color",newHexCode);
                }
            }
        });

        newQuestion=root.findViewById(R.id.newQuestionText);
        addQuestion=root.findViewById(R.id.addQuestion);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newQuestion.getText().toString().isEmpty()){
                    Map<String,Object> question= new HashMap<>();
                    question.put("question",newQuestion.getText().toString());
                    db.collection("bookmarks_categories").document(markerID).collection("questions").document().set(question);
                }else{
                    Toast.makeText(getContext(),"Uma pergunta tem qque ter texto",Toast.LENGTH_LONG).show();
                }
            }
        });
        return  root;
    }

    public void updateUI(){
        MarkersFragment fragment2 = new MarkersFragment();
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
}
