package com.example.rimae;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void login(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void back(View view){
        Intent intent=new Intent(this,BeforeLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
