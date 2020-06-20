package com.example.rimae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String uid = FirebaseAuth.getInstance().getUid();
        Handler handler = new Handler();

        if (uid == null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLogin();
                }
            }, 2000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToHome();
                }
            }, 2000);
        }
    }
    private void showLogin() {
        Intent intent = new Intent(SplashActivity.this, BeforeLoginActivity.class);

        startActivity(intent);
        finish();
    }

    private void goToHome(){
        Intent intent = new Intent(SplashActivity.this, Main2Activity.class);

        startActivity(intent);
        finish();
    }
}
