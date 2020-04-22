package com.example.shoptilyoudrop;

import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteItemsActivity extends AppCompatActivity {
    private ArrayList<String> favorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_items);
        ListView listView = (ListView) findViewById(R.id.listview);
        generateListContent();
        listView.setAdapter((new MyListAdapter (this, R.layout.list_item, favorites)));
    }

    private void generateListContent() {
        for(int i = 0; i < 10; i++) {
            favorites.add("Pineapple");
        }
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null) {
                LayoutInflater inflator = LayoutInflater.from(getContext());
                convertView = inflator.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                viewHolder.store = (TextView) convertView.findViewById(R.id.list_item_store);
                viewHolder.price = (TextView) convertView.findViewById(R.id.list_item_price);
                viewHolder.addToCheckout = (Button) convertView.findViewById(R.id.list_item_add_to_checkout);
                viewHolder.addToCheckout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FavoriteItemsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.removeFromFavorites = (Button) convertView.findViewById(R.id.list_item_remove_from_favorites);
                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.name.setText(getItem(position));
            }

            return convertView;
        }

    }

    public class ViewHolder {

        TextView name;
        TextView store;
        TextView price;
        Button addToCheckout;
        Button removeFromFavorites;
    }
}
