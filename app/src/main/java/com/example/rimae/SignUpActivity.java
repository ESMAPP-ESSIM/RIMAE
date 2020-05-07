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

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailInp,nameInp,pwInp,cpwInp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        //Get email name and password
        emailInp=findViewById(R.id.emailInp);
        nameInp=findViewById(R.id.nameInp);
        pwInp=findViewById(R.id.pwInp);
        cpwInp=findViewById(R.id.cpwInp);
    }

    public void login(View view){
        //get the strings

        String email=emailInp.getText().toString();
        String pw= pwInp.getText().toString();
        String cpw=cpwInp.getText().toString();

        Log.d("Sign","entra"+pw+";"+cpw);
       //if(pw==cpw){
            mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Sign",task.toString());
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SignUpActivity.this,"Unable To Create Account",Toast.LENGTH_LONG).show();
                    }
                }
            });
        //}else{

          //  Toast.makeText(SignUpActivity.this,"Passwords must match",Toast.LENGTH_LONG).show();

        //}


    }

    public void back(View view){
        Intent intent=new Intent(this,BeforeLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
