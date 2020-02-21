package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        price = findViewById(R.id.price);
        food = findViewById(R.id.food);
        addName = findViewById(R.id.add);
        listView = findViewById(R.id.listView);
        Intent intent = getIntent();
        final String fName = intent.getStringExtra("Test");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this , StartActivity.class));
            }
        });
        addName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String foodToText = food.getText().toString();
                String priceToText = price.getText().toString();
                if(foodToText.isEmpty()){
                    Toast.makeText(MainActivity.this, "Invalid Food", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (fName != null && !fName.isEmpty()){
                            String[] emailExt = fName.split("@");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRef = database.getReference("").child("Database");
                            mRef.child(emailExt[0]).child(foodToText).setValue(Double.parseDouble(priceToText));
                            Toast.makeText(MainActivity.this, "Added to "+fName, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        if (fName != null && !fName.isEmpty()) {
            String[] emailExt = fName.split("@");
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Database");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        if(snapshot.getValue()!=null) {
                            list.add(snapshot.getValue().toString());
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
