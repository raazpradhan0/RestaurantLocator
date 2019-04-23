package com.example.restaurantlocator.RestaurantCategory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.R;
import com.example.restaurantlocator.RestaurantDetailActivity;
import com.example.restaurantlocator.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.squareup.picasso.Picasso;

public class RestaurantCategory extends AppCompatActivity {
    private DatabaseReference ResturantRef;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_category);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nepali Cuisine");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child("Restaurants")
                .orderByChild("Catagory")
                .equalTo("Nepali Cuisine");

        FirebaseRecyclerOptions<Restaurant> options = new FirebaseRecyclerOptions
                .Builder<Restaurant>()
                .setQuery(query, Restaurant.class)
                .build();
        FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(options) {
            @NonNull

            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull final Restaurant model) {
                holder.txtRestauranttName.setText(model.getRname());
                holder.txtRestaurantDescription.setText(model.getRdetails());
                holder.txtRestaurantCatagory.setText(model.getCatagory());
                Picasso.get().load(model.getImage()).into(holder.imageView);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RestaurantCategory.this, RestaurantDetailActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_layout, parent, false);
                RestaurantViewHolder holder = new RestaurantViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
