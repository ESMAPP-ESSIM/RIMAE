package com.example.rimae;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * SignUpActivity is the activity that the user will
 * use to sign up to use the app.
 * It is used so the user can register himself
 *
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailInp, nameInp, pwInp;
    Button selectphoto;
    private Uri selectedPhotoUri;
    private StorageReference mStorageRef;
    private String uid;

    private CircleImageView imgPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        //Get email name and password
        emailInp = findViewById(R.id.emailInp);
        nameInp = findViewById(R.id.nameInp);
        pwInp = findViewById(R.id.pwInp);
        selectphoto = findViewById(R.id.photo_button);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imgPrev = findViewById(R.id.selected_photo_prev);
    }

    /**
     * Used to select a photo to be added to the user profile
     *
     * @param view
     */
    public void selectPhoto(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            selectedPhotoUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedPhotoUri);

                imgPrev.setImageBitmap(bitmap);
                selectphoto.setAlpha(0f);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to register the user
     * If it succeeds, the user will be registered and will be logged in and access
     * If it fails, the user will be notified and will remain in the same activity
     *
     * @param view
     */
    public void login(View view){
        //get the strings
        String email = emailInp.getText().toString();
        String pw = pwInp.getText().toString();
        String name = nameInp.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    uid = user.getUid();

                    if (imgPrev.getDrawable() == null){
                        saveUserToFirebase("http://www.coffeebrain.org/wiki/images/9/93/PEOPLE-NoFoto.JPG");
                    } else {
                        uploadImageToStorage();
                    }

                    updateUI();
                } else {
                    Toast.makeText(SignUpActivity.this,"Unable To Create Account", Toast.LENGTH_LONG).show();
                }
                }
            });
        }

    }

    /**
     * Used to upload the selected image and to add it to the user profile
     */
    private void uploadImageToStorage() {
        String filename = UUID.randomUUID().toString();

        final StorageReference imagesRef = mStorageRef.child("images/" + filename);

        imagesRef.putFile(selectedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        saveUserToFirebase(uri.toString());
                    }
                });
            }
        });
    }

    /**
     * Saves the user to the database
     *
     * @param uri a string containing the uri of the image of the user
     */
    private void saveUserToFirebase(String uri) {
        Log.d("Register","User uid: "+uid);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> user = new HashMap<>();

        user.put("name", nameInp.getText().toString());
        user.put("email", emailInp.getText().toString());
        user.put("profile_pic", uri);
        user.put("points", 0);

        db.collection("users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Register","User Saved to database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Register","Failed to save", e);
            }
        });

    }

    /**
     * Allows the user to go to the previous activity: the BeforeLogin activity
     *
     * @param view
     */
    public void back(View view) {
        Intent intent = new Intent(this, BeforeLoginActivity.class);

        startActivity(intent);
        finish();
    }

    /**
     * Allows the user to go to the next activity: the Main2 activity
     */
    private void updateUI() {
        Intent intent = new Intent(SignUpActivity.this, Main2Activity.class);

        startActivity(intent);
        finish();
    }
}
