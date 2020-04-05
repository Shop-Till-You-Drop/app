package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private Button back;
    private FirebaseAuth temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
}
