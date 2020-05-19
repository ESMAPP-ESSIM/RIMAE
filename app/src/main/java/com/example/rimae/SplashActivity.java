package com.example.rimae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String uid = FirebaseAuth.getInstance().getUid();
        Handler handler = new Handler();

        /*
        * if(uid==null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showLogin();
                }
            }, 2000);
        }else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToHome();
                }
            }, 2000);

        }
        *
        * */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLogin();
            }
        }, 2000);
    }
    private void showLogin() {
        Intent intent = new Intent(
                SplashActivity.this,BeforeLoginActivity.class
        );
        startActivity(intent);
        finish();
    }

    private void goToHome(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
