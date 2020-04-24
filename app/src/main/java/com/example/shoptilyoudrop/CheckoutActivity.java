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

public class CheckoutActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> stores = new ArrayList<>();
    ArrayList<String> quantities = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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
                quantities = new ArrayList<>();
                prices = new ArrayList<>();
                for (DataSnapshot foodItem : dataSnapshot.getChildren()) {
                    if(foodItem != null) {
                        for (DataSnapshot storeItem : foodItem.getChildren()) {
                            if(storeItem.getKey().matches("Checkout[0-9]*")) {
                                for (DataSnapshot checkoutItem : storeItem.getChildren()) {
                                    if(checkoutItem.getKey().equals("qty")) {
                                        quantities.add(checkoutItem.getValue().toString());
                                    } else if (checkoutItem.getValue() != null) {
                                        items.add(foodItem.getKey());
                                        stores.add(checkoutItem.getKey());
                                        if (checkoutItem.getValue() instanceof Double) {
                                            prices.add(Double.toString((Double) checkoutItem.getValue()));
                                        } else if (checkoutItem.getValue() instanceof Long) {
                                            prices.add(Long.toString((Long) checkoutItem.getValue()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (items.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "No items found on profile.", Toast.LENGTH_LONG).show();
                } else {
                    double total = 0 ;
                    for(int i = 0; i < items.size(); i++) {
                        total += Double.parseDouble(prices.get(i)) * Integer.parseInt(quantities.get(i));
                    }
                    items.add(Double.toString(total));
                }
                if(items.size() == stores.size()) {
                    items.add("0.00");
                }
                stores.add("");
                quantities.add("");
                prices.add("");
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter((new CheckoutActivity.listAdapter(CheckoutActivity.this, items, stores, quantities, prices)));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CheckoutActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class listAdapter extends ArrayAdapter<String> {
        private ArrayList<String> foodType;
        private ArrayList<String> stores;
        private ArrayList<String> quantities;
        private ArrayList<String> prices;

        private listAdapter(Context context, ArrayList<String> foodType, ArrayList<String> store, ArrayList<String> quantities, ArrayList<String> price) {
            super(context, R.layout.list_item_2, R.id.list_item_name, foodType);
            this.foodType = foodType;
            this.stores = store;
            this.quantities = quantities;
            this.prices = price;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CheckoutActivity.ViewHolder mainViewHolder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_2, parent, false);
                CheckoutActivity.ViewHolder viewHolder = new CheckoutActivity.ViewHolder();
                viewHolder.name = convertView.findViewById(R.id.list_item_name);
                viewHolder.store = convertView.findViewById(R.id.list_item_store);
                viewHolder.price = convertView.findViewById(R.id.list_item_price);
                viewHolder.quantity = convertView.findViewById(R.id.list_item_quantity);
                viewHolder.increaseQuantity = convertView.findViewById(R.id.list_item_increase_quantity);
                viewHolder.removeFromCheckout = convertView.findViewById(R.id.list_item_remove_from_checkout);

                if (position == items.size() - 1) {
                    viewHolder.increaseQuantity.setText(R.string.back);
                    viewHolder.removeFromCheckout.setText("");
                    viewHolder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(CheckoutActivity.this, MenuActivity.class);
                            i.putExtra("Test", userName);
                            startActivity(i);
                            finish();
                        }
                    });
                    viewHolder.removeFromCheckout.setVisibility(View.GONE);
                } else {
                    viewHolder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CheckoutActivity.this, items.get(position) + " added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    viewHolder.removeFromCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference refFavorite = firebaseDatabase.getReference().child("").child("Database").child(userName);
                            refFavorite = refFavorite.child(items.get(position)).child("Favorite").child(stores.get(position));
                            refFavorite.setValue(Double.parseDouble(prices.get(position)));
                            Toast.makeText(CheckoutActivity.this, items.get(position) + " added to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                viewHolder.name.setText(foodType.get(position));
                viewHolder.store.setText(stores.get(position));
                viewHolder.quantity.setText(quantities.get(position));
                viewHolder.price.setText(prices.get(position));
                convertView.setTag(viewHolder);
            } else {
                System.out.println(foodType);
                System.out.println(stores);
                System.out.println(prices);
                mainViewHolder = (CheckoutActivity.ViewHolder) convertView.getTag();
                mainViewHolder.name = convertView.findViewById(R.id.list_item_name);
                mainViewHolder.store = convertView.findViewById(R.id.list_item_store);
                mainViewHolder.price = convertView.findViewById(R.id.list_item_price);
                mainViewHolder.quantity = convertView.findViewById(R.id.list_item_price);
                mainViewHolder.increaseQuantity = convertView.findViewById(R.id.list_item_increase_quantity);
                mainViewHolder.removeFromCheckout = convertView.findViewById(R.id.list_item_remove_from_checkout);

                if (position == items.size() - 1) {
                    mainViewHolder.increaseQuantity.setText(R.string.back);
                    mainViewHolder.removeFromCheckout.setText("");
                    mainViewHolder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(CheckoutActivity.this, MenuActivity.class);
                            i.putExtra("Test", userName);
                            startActivity(i);
                            finish();
                        }
                    });
                    mainViewHolder.removeFromCheckout.setVisibility(View.GONE);
                } else {
                    mainViewHolder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CheckoutActivity.this, items.get(position) + " added", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mainViewHolder.removeFromCheckout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference refFavorite = firebaseDatabase.getReference().child("").child("Database").child(userName);
                            refFavorite = refFavorite.child(items.get(position)).child("Favorite").child(stores.get(position));
                            refFavorite.setValue(Double.parseDouble(prices.get(position)));
                            Toast.makeText(CheckoutActivity.this, items.get(position) + " added to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                mainViewHolder.name.setText(foodType.get(position));
                mainViewHolder.store.setText(stores.get(position));
                mainViewHolder.quantity.setText(quantities.get(position));
                mainViewHolder.price.setText(prices.get(position));
            }

            return convertView;
        }
    }

    public class ViewHolder{
        TextView name;
        TextView store;
        TextView quantity;
        TextView price;
        Button increaseQuantity;
        Button removeFromCheckout;
    }

}