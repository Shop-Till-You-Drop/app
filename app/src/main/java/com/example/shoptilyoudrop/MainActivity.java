package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private EditText price;
    private EditText food;
    private Button addName;
    private ListView listView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        price = findViewById(R.id.price);
        food = findViewById(R.id.food);
        addName = findViewById(R.id.add);
        listView = findViewById(R.id.listView);
        Intent intent = getIntent();
        String tempName = "";

        if (intent.getStringExtra("Test") != null) {
            tempName = intent.getStringExtra("Test");
        }
        if (tempName != null) {
            final String fName = tempName.replace(".", ",");

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
                    if (foodToText.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Invalid Food", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!fName.isEmpty()) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRef = database.getReference("").child("Database");
                            mRef.child(fName).child(foodToText).setValue(Double.parseDouble(priceToText));
                            Toast.makeText(MainActivity.this, "Added to " + fName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            final ArrayList<String> list = new ArrayList<>();
            adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);
            food.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    (MainActivity.this).adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            if (!fName.isEmpty()) {
//            String[] temp = fName.split("@");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Database").child(fName);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue() != null) {
                                list.add(snapshot.getKey() + ": " + snapshot.getValue().toString());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
