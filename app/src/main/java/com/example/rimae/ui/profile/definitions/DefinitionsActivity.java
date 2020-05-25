package com.example.rimae.ui.profile.definitions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rimae.R;
import com.example.rimae.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.UUID;

public class DefinitionsActivity extends Fragment {

    ImageView photo;
    EditText nameInput;
    String currentName;
    private Uri selectedPhotoUri;
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getUid();
    String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       final View root = inflater.inflate(R.layout.fragment_definitions, container, false);

        photo=root.findViewById(R.id.changePhoto);
        nameInput=root.findViewById(R.id.nameInput);
        mStorageRef = FirebaseStorage.getInstance().getReference();




//Ir buscar informação sobre o utilizador logged
        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //Atualizar o ImageView e o TextView com a informação correspondente do utilizador
                    String uri=task.getResult().get("profile_pic").toString();
                    String name = task.getResult().get("name").toString();
                    currentName= name;
                   Picasso.get().load(uri).fit().centerCrop().into(photo);
                   nameInput.setText(name);
                }else{
                    Log.w("Document",task.getException());
                }
            }
        });
        ImageView changePhoto= root.findViewById(R.id.changePhoto);
        changePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

        Button saveChanges=root.findViewById(R.id.saveChangesButton);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Button resetPassword=root.findViewById(R.id.changePassword);

        resetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        return  root;
    }

    public void changePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode== Activity.RESULT_OK){
            selectedPhotoUri= data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedPhotoUri);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void save(){
        String newName=nameInput.getText().toString();

        if(selectedPhotoUri==null){
            Log.d("Definições","Imagem não foi mudada");
        }else{
            Log.w("Definições","Nova Imagem"+selectedPhotoUri);
            updatePhoto();
        }


        if(TextUtils.isEmpty(newName) || newName.equals(currentName)){
            Log.d("Definições","Nome sem mudanças" + newName + "nome antigo"+currentName);
        }else{
            Log.d("Definições","Atualizar nome" + newName + "nome antigo"+ currentName);
            updateName(newName);
        }

        updateUI();
    }


    public void updatePassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(),"An email has been sent to you",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Failed to send you an email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateName(String newName) {
        db.collection("users").document(userId).update("name",newName);
    }

    private void updatePhoto() {
        String filename = UUID.randomUUID().toString();

        final StorageReference imagesRef= mStorageRef.child("images/"+filename);

        imagesRef.putFile(selectedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("users").document(userId).update("profile_pic",uri.toString());
                    }
                });
            }
        });
    }

    private  void updateUI(){
        ProfileFragment fragment2 = new ProfileFragment();
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*public void goBack(View view){
        finish();
    }*/
}
