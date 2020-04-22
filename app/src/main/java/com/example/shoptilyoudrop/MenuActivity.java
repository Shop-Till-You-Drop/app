package com.example.shoptilyoudrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private Button addItem;
    private Button checkout;
    private Button favorites;
    private Button signOut;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        addItem = findViewById(R.id.add_item);
        checkout = findViewById(R.id.checkout);
        favorites = findViewById(R.id.favorites);
        signOut = findViewById(R.id.sign_out);

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
