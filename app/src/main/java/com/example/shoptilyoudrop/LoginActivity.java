package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        ((TextView) findViewById(R.id.password)).setTransformationMethod(new HiddenPassTransformationMethod());
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        Button forgetPassword = findViewById(R.id.forgetPassword);
        author = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    toastMessage = "Login Failed";
                    Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                } else {
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
        forgetPassword.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, forgetPasswordActivity.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        author.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                toastMessage = "Login Successful!";
                Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    Intent i = new Intent(LoginActivity.this , MainActivity.class);
                i.putExtra("Test", emailTxt);
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

    public FirebaseAuth getAuthor() {
        return author;
    }

    //Function that hides the character code based off of https://stackoverflow.com/questions/6360222/problem-with-android-password-field-not-hiding-the-last-character-typed/16202479
    private class HiddenPassTransformationMethod implements TransformationMethod {
        private char passwordHider = '\u2022';
        //Passes chars
        @Override
        public CharSequence getTransformation(final CharSequence charSequence, final View view) {
            return new PassCharSequence(charSequence);
        }
        //We are not going to be implementing this function
        @Override
        public void onFocusChanged(final View view, final CharSequence charSequence, final boolean b, final int i, final Rect rect) {
        }

        private class PassCharSequence implements CharSequence {
            private final CharSequence charSequence;
            public PassCharSequence(final CharSequence charSequence) {
                this.charSequence = charSequence;
            }
            //Get the char at every location
            @Override
            public char charAt(final int index) {
                return passwordHider;
            }
            //Get length of current password
            @Override
            public int length() {
                return charSequence.length();
            }
            //Hides all the characters
            @Override
            public CharSequence subSequence(final int start, final int end) {
                return new PassCharSequence(charSequence.subSequence(start, end));
            }
        }
    }
}
