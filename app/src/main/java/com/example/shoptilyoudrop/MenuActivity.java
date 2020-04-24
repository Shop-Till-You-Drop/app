package com.example.shoptilyoudrop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class MenuActivity extends AppCompatActivity {

    private String userName;

    private GoogleSignInClient mGoogleSignInClient;

    private static final String TAG = "MenuActivity";
    private static final String ARG_NAME = "username";

    public static void startActivity(Context context, String username) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(ARG_NAME, username);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button addItem = findViewById(R.id.add_item);
        Button checkout = findViewById(R.id.checkout);
        Button favorites = findViewById(R.id.favorites);
        Button items = findViewById(R.id.items);
        Button signOut = findViewById(R.id.sign_out);

        Intent intent = getIntent();
        String tempName = "";

        if (intent.getStringExtra("Test") != null) {
            tempName = intent.getStringExtra("Test");
        }
        if (tempName != null) {
            userName = tempName.replace(".", ",");
        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                i.putExtra("Test", userName);
                startActivity(i);
                finish();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, CheckoutActivity.class);
                i.putExtra("Test", userName);
                startActivity(i);
                finish();
            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, FavoriteItemsActivity.class);
                i.putExtra("Test", userName);
                startActivity(i);
                finish();
            }
        });

        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ItemsActivity.class);
                i.putExtra("Test", userName);
                startActivity(i);
                finish();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MenuActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MenuActivity.this, StartActivity.class));

            }
        });
    }
}