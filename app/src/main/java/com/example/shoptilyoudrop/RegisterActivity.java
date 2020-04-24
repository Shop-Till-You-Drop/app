package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private Button back;
    private FirebaseAuth temp;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ((TextView) findViewById(R.id.password)).setTransformationMethod(new RegisterActivity.HiddenPassTransformationMethod());
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);
        temp = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                if(TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(passwordTxt)){
                    Toast.makeText(RegisterActivity.this, "Empty email", Toast.LENGTH_SHORT).show();
                }
                else if (passwordTxt.length()<8){
                    Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(emailTxt,passwordTxt);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, StartActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void registerUser(String email, String password){
        temp.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // email verification
                    FirebaseUser fuser = temp.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: email not sent " + e.getMessage());
                        }
                    });


                    Toast.makeText(RegisterActivity.this, "Success",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, MenuActivity.class);
                    i.putExtra("Test", getEmail().getText().toString());
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public EditText getEmail() {
        return email;
    }

    public EditText getPassword() {
        return password;
    }

    public Button getRegister() {
        return register;
    }

    public FirebaseAuth getTemp() {
        return temp;
    }

    //Function that hides the character code based off of https://stackoverflow.com/questions/6360222/problem-with-android-password-field-not-hiding-the-last-character-typed/16202479
    private class HiddenPassTransformationMethod implements TransformationMethod {
        private char passwordHider = '\u2022';

        //Passes chars
        @Override
        public CharSequence getTransformation(final CharSequence charSequence, final View view) {
            return new RegisterActivity.HiddenPassTransformationMethod.PassCharSequence(charSequence);
        }

        //We are not going to be implementing this function
        @Override
        public void onFocusChanged(final View view, final CharSequence charSequence, final boolean b, final int i, final Rect rect) {
        }

        private class PassCharSequence implements CharSequence {
            private final CharSequence charSequence;

            private PassCharSequence(final CharSequence charSequence) {
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
                return new RegisterActivity.HiddenPassTransformationMethod.PassCharSequence(charSequence.subSequence(start, end));
            }
        }
    }
}
