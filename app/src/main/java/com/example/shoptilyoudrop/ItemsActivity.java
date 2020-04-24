package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> stores = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userName = getIntent().getStringExtra("Test");
        Button back = findViewById(R.id.back);
        displayItems();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ItemsActivity.this, StartActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void displayItems() {
        DatabaseReference ref = firebaseDatabase.getReference("").child("Database").child(userName);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items = new ArrayList<>();
                stores = new ArrayList<>();
                prices = new ArrayList<>();
                for (DataSnapshot foodItem : dataSnapshot.getChildren()) {
                    if (foodItem.child("Item").getChildrenCount() > 0) {
                        items.add(foodItem.getKey());
                        for (DataSnapshot storeItem : foodItem.child("Item").getChildren()) {
                            if (items.size() == stores.size()) {
                                items.add(items.get(items.size() - 1));
                            }
                            stores.add(storeItem.getKey());
                            if (storeItem.getValue() instanceof Double) {
                                prices.add(Double.toString((Double) storeItem.getValue()));
                            } else if (storeItem.getValue() instanceof Long) {
                                prices.add(Long.toString((Long) storeItem.getValue()));
                            }
                            System.out.println(items);
                        }
                    }
                }
                if (items.isEmpty()) {
                    Toast.makeText(ItemsActivity.this, "No favorites found on profile.", Toast.LENGTH_LONG).show();
                }
                items.add("");
                stores.add("");
                prices.add("");
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter((new listAdapter(ItemsActivity.this, items, stores, prices)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class listAdapter extends ArrayAdapter<String> {
        private ArrayList<String> foodType;
        private ArrayList<String> stores;
        private ArrayList<String> prices;

        private listAdapter(Context context, ArrayList<String> foodType, ArrayList<String> store, ArrayList<String> price) {
            super(context, R.layout.list_item, R.id.list_item_name, foodType);
            this.foodType = foodType;
            this.stores = store;
            this.prices = price;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item_1, parent, false);
            TextView name = row.findViewById(R.id.list_item_name);
            TextView store = row.findViewById(R.id.list_item_store);
            TextView price = row.findViewById(R.id.list_item_price);
            Button addToCheckout = row.findViewById(R.id.list_item_add_to_checkout);
            Button removeFromItems = row.findViewById(R.id.list_item_remove_from_items);

            if (position == items.size() - 1) {
                addToCheckout.setText(R.string.back);
                removeFromItems.setText("");
                addToCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ItemsActivity.this, MenuActivity.class);
                        i.putExtra("Test", userName);
                        startActivity(i);
                        finish();
                    }
                });
                removeFromItems.setVisibility(View.GONE);
            } else {
                addToCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ItemsActivity.this, items.get(position) + " added", Toast.LENGTH_SHORT).show();
                    }
                });
                removeFromItems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference refRemove = firebaseDatabase.getReference().child("").child("Database").child(userName);
                        refRemove = refRemove.child(items.get(position)).child("Item").child(stores.get(position));
                        refRemove.removeValue();
                        Toast.makeText(ItemsActivity.this, items.get(position) + " removed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            name.setText(foodType.get(position));
            store.setText(stores.get(position));
            price.setText(prices.get(position));

            return row;
        }
    }
}