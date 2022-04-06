package com.example.carpoolbuddy;

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
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        //firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.userInputEmail);
        passwordField = findViewById(R.id.userInputPassword);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser currentUser)
    {
        if(currentUser != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void signIn(View v)
    {
        System.out.println("Sign in");
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        try
        {
            mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Log.d("SIGN IN", "successfully signed in");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }
                    else
                    {
                        Log.w("SIGN IN", "failed to sign in", task.getException());
                        Toast.makeText(AuthActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        catch(Exception err)
        {
            Toast.makeText(AuthActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    public void signUp(View v)
    {
        System.out.println("Sign up");
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        try
        {
            mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Log.d("SIGN UP", "successfully signed up the user");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }
                    else
                    {
                        Log.w("SIGN UP", "failed to sign up user", task.getException());
                        Toast.makeText(AuthActivity.this, "Sign up failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        catch(Exception err)
        {
            Toast.makeText(AuthActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }
}