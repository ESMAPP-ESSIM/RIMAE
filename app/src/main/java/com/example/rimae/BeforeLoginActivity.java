package com.example.rimae;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * BeforeLoginActivity is the activity that the user will
 * access before login and that will allow him to sign up
 * or login into the rest of the app
 *
 */
public class BeforeLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);
    }

    /**
     * Used to navigate to the Login activity
     *
     * @param view
     */
    public void goLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }

    /**
     *
     * Used to navigate to the SignUp activity
     *
     * @param view
     */
    public void goSign(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);

        startActivity(intent);
        finish();
    }
}
