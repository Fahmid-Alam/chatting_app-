package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    TextView logsignup;
    Button button;
    EditText email, password;

    FirebaseAuth auth;

    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";    // email pattern for checking validity of email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(login.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {


            button = findViewById(R.id.logbutton);
            email = findViewById(R.id.editTexLogEmail);
            password = findViewById(R.id.editTextLogPassword);
            logsignup = findViewById(R.id.logsignup);

            auth = FirebaseAuth.getInstance();


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Email = email.getText().toString();
                    String pass = password.getText().toString();

                    if ((TextUtils.isEmpty(Email))) {        // return true if string length is null or 0
                        Toast.makeText(login.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(pass)) {
                        Toast.makeText(login.this, "Enter The Password", Toast.LENGTH_SHORT).show();
                    } else if (!Email.matches(emailPattern)) {
                        email.setError("Give Proper Email Address");
                    } else if (password.length() < 6) {
                        password.setError("More Then Six Characters");
                        Toast.makeText(login.this, "Password Needs To Be Longer Then Six Characters", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    try {
                                        Intent i = new Intent(login.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } catch (Exception e) {
                                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });


            logsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(login.this, registration.class);
                    startActivity(i);
                    finish();
                }
            });


        }

    }
}