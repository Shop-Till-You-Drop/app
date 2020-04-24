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
        displayItems();
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
                    if(foodItem != null) {
                        items.add(foodItem.getKey());
                        for (DataSnapshot storeItem : foodItem.getChildren()) {
                            if(!storeItem.getKey().equals("Favorite") && !storeItem.getKey().matches("Checkout[0-9]*")) {
                                if (items.size() == stores.size()) {
                                    items.add(items.get(items.size() - 1));
                                }
                                stores.add(storeItem.getKey());
                                if (storeItem.getValue() instanceof Double) {
                                    prices.add(Double.toString((Double) storeItem.getValue()));
                                } else if (storeItem.getValue() instanceof Long) {
                                    prices.add(Long.toString((Long) storeItem.getValue()));
                                }
                            }
                        }
                    }
                }

                if (items.isEmpty()) {
                    Toast.makeText(ItemsActivity.this, "No items found on profile.", Toast.LENGTH_LONG).show();
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
            super(context, R.layout.list_item_1, R.id.list_item_name, foodType);
            this.foodType = foodType;
            this.stores = store;
            this.prices = price;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_1, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.list_item_name);
                viewHolder.store = convertView.findViewById(R.id.list_item_store);
                viewHolder.price = convertView.findViewById(R.id.list_item_price);
                viewHolder.addToCheckout = convertView.findViewById(R.id.list_item_add_to_checkout);
                viewHolder.addToFavorites = convertView.findViewById(R.id.list_item_add_to_favorites);

                if (position == items.size() - 1) {
                    viewHolder.addToCheckout.setText(R.string.back);
                    viewHolder.addToFavorites.setText("");
                    viewHolder.addToCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ItemsActivity.this, MenuActivity.class);
                            i.putExtra("Test", userName);
                            startActivity(i);
                            finish();
                        }
                    });
                    viewHolder.addToFavorites.setVisibility(View.GONE);
                } else {
                    viewHolder.addToCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ItemsActivity.this, items.get(position) + " added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    viewHolder.addToFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference refFavorite = firebaseDatabase.getReference().child("").child("Database").child(userName);
                            refFavorite = refFavorite.child(items.get(position)).child("Favorite").child(stores.get(position));
                            refFavorite.setValue(Double.parseDouble(prices.get(position)));
                            Toast.makeText(ItemsActivity.this, items.get(position) + " added to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                viewHolder.name.setText(foodType.get(position));
                viewHolder.store.setText(stores.get(position));
                viewHolder.price.setText(prices.get(position));
                convertView.setTag(viewHolder);
            } else {
                System.out.println(foodType);
                System.out.println(stores);
                System.out.println(prices);
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.name = convertView.findViewById(R.id.list_item_name);
                mainViewHolder.store = convertView.findViewById(R.id.list_item_store);
                mainViewHolder.price = convertView.findViewById(R.id.list_item_price);
                mainViewHolder.addToCheckout = convertView.findViewById(R.id.list_item_add_to_checkout);
                mainViewHolder.addToFavorites = convertView.findViewById(R.id.list_item_add_to_favorites);

                if (position == items.size() - 1) {
                    mainViewHolder.addToCheckout.setText(R.string.back);
                    mainViewHolder.addToFavorites.setText("");
                    mainViewHolder.addToCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ItemsActivity.this, MenuActivity.class);
                            i.putExtra("Test", userName);
                            startActivity(i);
                            finish();
                        }
                    });
                    mainViewHolder.addToFavorites.setVisibility(View.GONE);
                } else {
                    mainViewHolder.addToCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ItemsActivity.this, items.get(position) + " added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mainViewHolder.addToFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference refFavorite = firebaseDatabase.getReference().child("").child("Database").child(userName);
                            refFavorite = refFavorite.child(items.get(position)).child("Favorite").child(stores.get(position));
                            refFavorite.setValue(Double.parseDouble(prices.get(position)));
                            Toast.makeText(ItemsActivity.this, items.get(position) + " added to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                mainViewHolder.name.setText(foodType.get(position));
                mainViewHolder.store.setText(stores.get(position));
                mainViewHolder.price.setText(prices.get(position));
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView name;
        TextView store;
        TextView price;
        Button addToCheckout;
        Button addToFavorites;
    }

}