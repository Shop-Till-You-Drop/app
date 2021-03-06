package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText price;
    private AutoCompleteTextView food;
    private EditText store;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayAdapter adapter;
    private Button addName;
    private Button logout;
    private Button back;
    private String fName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, savedInstanceState);
        setContentView(R.layout.activity_main);
        price = findViewById(R.id.price);
        store = findViewById(R.id.store);
        food = findViewById(R.id.food);
        logout = findViewById(R.id.logout);
        addName = findViewById(R.id.add);
        back = findViewById(R.id.back);

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
                } else if (storeToText.isEmpty() || storeToText.equals("Favorite")) {
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("").child("Database").child(fName);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getKey());
                }
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                food.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                i.putExtra("Test", fName);
                startActivity(i);
                finish();
            }
        });
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