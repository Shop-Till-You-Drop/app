package com.example.shoptilyoudrop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteItemsActivity extends AppCompatActivity {
    private ArrayList<String> favorites = new ArrayList<>();
    private ArrayList<String> stores = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private String userName;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_items);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userName = getIntent().getStringExtra("Test");
        generateListContent();
    }

    private void generateListContent() {
        DatabaseReference ref = firebaseDatabase.getReference("").child("Database").child(userName);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favorites = new ArrayList<>();
                stores = new ArrayList<>();
                prices = new ArrayList<>();
                for (DataSnapshot snapshotfood : dataSnapshot.getChildren()) {
                    if(snapshotfood.child("Favorite").getChildrenCount() > 0) {
                        favorites.add(snapshotfood.getKey());
                        for (DataSnapshot snapshotstore : snapshotfood.child("Favorite").getChildren()) {
                            if(favorites.size() == stores.size()) {
                                favorites.add(favorites.get(favorites.size() - 1));
                            }
                            stores.add(snapshotstore.getKey());
                            if(snapshotstore.getValue() instanceof Double) {
                                prices.add(Double.toString((Double) snapshotstore.getValue()));
                            } else if (snapshotstore.getValue() instanceof Long) {
                                prices.add(Long.toString((Long) snapshotstore.getValue()));
                            }
                        }
                    }
                }
                if(favorites.isEmpty()) {
                    Toast.makeText(FavoriteItemsActivity.this, "No favorites found on profile.", Toast.LENGTH_LONG).show();
                }
                favorites.add("");
                stores.add("");
                prices.add("");
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter((new MyListAdapter (FavoriteItemsActivity.this, favorites, stores, prices)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavoriteItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private ArrayList<String> foodType;
        private ArrayList<String> stores;
        private ArrayList<String> prices;

        private MyListAdapter(Context context, ArrayList<String> foodType, ArrayList<String> store, ArrayList<String> price) {
            super(context, R.layout.list_item, R.id.list_item_name, foodType);
            this.foodType = foodType;
            this.stores = store;
            this.prices = price;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item, parent, false);
            TextView name = row.findViewById(R.id.list_item_name);
            TextView store = row.findViewById(R.id.list_item_store);
            TextView price = row.findViewById(R.id.list_item_price);
            Button addToCheckout = row.findViewById(R.id.list_item_add_to_checkout);
            Button removeFromFavorites = row.findViewById(R.id.list_item_remove_from_favorites);

            if(position == favorites.size() - 1) {
                addToCheckout.setText(R.string.back);
                removeFromFavorites.setText("");
                addToCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(FavoriteItemsActivity.this, MenuActivity.class);
                        i.putExtra("Test", userName);
                        startActivity(i);
                        finish();
                    }
                });
                removeFromFavorites.setVisibility(View.GONE);
            } else {
                addToCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FavoriteItemsActivity.this, favorites.get(position) + " added", Toast.LENGTH_SHORT).show();
                    }
                });
                removeFromFavorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference refRemove = firebaseDatabase.getReference().child("").child("Database").child(userName);
                        refRemove = refRemove.child(favorites.get(position)).child("Favorite").child(stores.get(position));
                        refRemove.removeValue();
                        Toast.makeText(FavoriteItemsActivity.this, favorites.get(position) + " removed", Toast.LENGTH_SHORT).show();
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
