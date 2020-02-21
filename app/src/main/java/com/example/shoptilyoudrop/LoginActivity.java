package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth author;
    private String emailTxt;
    private String toastMessage;
    private String passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        author = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    toastMessage = "Login Failed";
                    Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                }
                else {
                    emailTxt = email.getText().toString();
                    passwordTxt = "";
                    if (password.getText().toString().isEmpty()) {
                        toastMessage = "Login Failed";
                        Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        passwordTxt = password.getText().toString();
                        loginUser(emailTxt, passwordTxt);
                    }
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        author.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    toastMessage = "Login Successful!";
                    Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this , MainActivity.class);
//                    Intent i = new Intent(LoginActivity.this , MainActivity.class);
                    i.putExtra("Test",emailTxt);
                    startActivity(i);
                    finish();
                }
        });

        author.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toastMessage = "Login Failed";
                Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public EditText getEmail() {
        return email;
    }

    public EditText getPassword() {
        return password;
    }

    public Button getLogin() {
        return login;
    }

    public String getToastMessage() {
        return toastMessage;
    }

}
