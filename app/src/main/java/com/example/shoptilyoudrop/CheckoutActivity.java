package com.example.shoptilyoudrop;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView food;
    private TextView price;
    private TextView store;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedStateInstance);
        food = findViewById(R.id.foodDisplay);
        price = findViewById(R.id.priceDisplay);
        store = findViewById(R.id.storeDisplay);
        total = findViewById(R.id.totalPrice);
    }
}