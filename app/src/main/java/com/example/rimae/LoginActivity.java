package com.example.rimae;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText emailInp,pwInp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailInp = findViewById(R.id.emailInp);
        pwInp = findViewById(R.id.pwInp);
    }

    public void login(View view){
        String email = emailInp.getText().toString();
        String pw = pwInp.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Enviar o user loggado para a proxima activity
                    FirebaseUser user = mAuth.getCurrentUser();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userInfo", user);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void back(View view){
        Intent intent = new Intent(this, BeforeLoginActivity.class);

        startActivity(intent);
        finish();
    }
}
