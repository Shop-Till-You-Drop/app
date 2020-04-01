package com.example.shoptilyoudrop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText userEmail;
    private Button resetLink;
    private Button back;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = findViewById(R.id.Email);
        resetLink = findViewById(R.id.resetLink);
        back = findViewById(R.id.back);

        firebaseAuth = FirebaseAuth.getInstance();

        resetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userEmail.getText().toString().equals("")) {
                    firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Reset sent to email", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotPasswordActivity.this, StartActivity.class));
                            } else {
                                if(task.getException().getMessage() != null) {
                                    Toast.makeText(ForgotPasswordActivity.this,
                                            task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email was empty. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPasswordActivity.this, StartActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public EditText getUserEmail() {
        return userEmail;
    }

    public Button getResetLink() {
        return resetLink;
    }
}