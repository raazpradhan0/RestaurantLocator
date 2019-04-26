package com.example.restaurantlocator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.example.restaurantlocator.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Search extends Fragment {
        private DatabaseReference ResturantRef, LikesRef;
        private RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        private SearchView searchView;
        public String query;
        Boolean LikeChecker = false;




    //private OnFragmentInteractionListener mListener;

    public Search() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ResturantRef = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");


        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView  = (SearchView)rootView.findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit( final String Query) {
                query=Query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String Query) {
                query=Query;
                onStart();
                return false;
            }
        });


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return rootView;

    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Restaurant> options =  new FirebaseRecyclerOptions
                .Builder<Restaurant>()
                .setQuery(ResturantRef.orderByChild("Rname").startAt(query),Restaurant.class)
                .build();
        FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull final Restaurant model)
            {
                holder.txtRestauranttName.setText(model.getRname());
                holder.txtRestaurantDescription.setText(model.getRdetails());
                holder.txtRestaurantCatagory.setText(model.getCatagory());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.setLikeButtonStatus(model.getPid());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getActivity(),RestaurantDetailActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);

                    }
                });

                holder.commentpostButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent commentIntent = new Intent(getActivity(),CommentActivity.class);
                        commentIntent.putExtra("pid", model.getPid());
                        startActivity(commentIntent);

                    }
                });


                holder.commentPostTextView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent commentIntent = new Intent(getActivity(),CommentActivity.class);
                        commentIntent.putExtra("pid", model.getPid());
                        startActivity(commentIntent);

                    }
                });

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        LikeChecker = true;

                        LikesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(LikeChecker.equals(true))
                                {
                                    if(dataSnapshot.child(model.getPid()).hasChild(Prevalent.currentOnlineUser.getPhone()))
                                    {
                                        LikesRef.child(model.getPid()).child(Prevalent.currentOnlineUser.getPhone()).removeValue();
                                        LikeChecker = false;

                                    }
                                    else
                                    {
                                        LikesRef.child(model.getPid()).child(Prevalent.currentOnlineUser.getPhone()).setValue(true);
                                        LikeChecker = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });


                    }
                });

            }

            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_layout,parent,false);
                RestaurantViewHolder holder = new RestaurantViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }*/
}

