package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private EditText price;
    private AutoCompleteTextView food;
    private EditText store;
    private Button addName;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayAdapter adapter;
    private Button checkout;
    private String fName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        price = findViewById(R.id.price);
        store = findViewById(R.id.store);
        food = findViewById(R.id.food);
        addName = findViewById(R.id.add);
        checkout = findViewById(R.id.checkOut);

        Intent intent = getIntent();
        String tempName = "";

        if (intent.getStringExtra("Test") != null) {
            tempName = intent.getStringExtra("Test");
        }
        if (tempName != null) {
            fName = tempName.replace(".", ",");
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });
        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodToText = food.getText().toString();
                String priceToText = price.getText().toString();
                String storeToText = store.getText().toString();
                if (foodToText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Food", Toast.LENGTH_SHORT).show();
                } else if (storeToText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Store", Toast.LENGTH_SHORT).show();
                } else if (priceToText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Invalid Price", Toast.LENGTH_SHORT).show();
                } else {
                    if (!fName.isEmpty()) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = database.getReference("").child("Database");
                        mRef.child(fName).child(foodToText).child(storeToText).setValue(Double.parseDouble(priceToText));
                        Toast.makeText(MainActivity.this, "Added to " + fName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        final ArrayList<String> list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("").child("Database").child(fName);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        food.setAdapter(adapter);
    }

    public AutoCompleteTextView getFood() {
        return food;
    }

    public EditText getPrice() {
        return price;
    }

    public EditText getStore() {
        return store;
    }

    public void setfName(String newName) {
        fName = newName;
    }
}
