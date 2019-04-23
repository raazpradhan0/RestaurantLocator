package com.example.restaurantlocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.Model.User;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ImageView restaurantImage;
    private TextView restaurantName, restaurantDescription, restaurantReview ;
    private RatingBar ratingBar;

    private EditText restaurantComment;
    private Button restaurantRateButton;
    private String  restaurantID ="";
    private float starValue;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        restaurantID = getIntent().getStringExtra("pid");

        restaurantImage = (ImageView) findViewById(R.id.restaurant_image_details);
        restaurantName = (TextView) findViewById(R.id.restaurant_name_details);
        restaurantDescription = (TextView) findViewById(R.id.restaurant_description_details);
        restaurantReview = (TextView) findViewById(R.id.restaurant_review_details);
        ratingBar =(RatingBar) findViewById(R.id.ratingBar);
        restaurantRateButton =(Button) findViewById(R.id.restaurant_rat_bttn);
        restaurantComment = (EditText) findViewById(R.id.restaurant_comment);



        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Restaurant Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getRestaurantDetails(restaurantID);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                Toast.makeText(RestaurantDetailActivity.this,"Stars:"+ rating ,Toast.LENGTH_SHORT).show();
                starValue = rating;

            }
        });


        restaurantRateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reviewRestaurant();

            }
        });


    }

    private void reviewRestaurant()
    {

        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate =Calendar.getInstance();
        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currrentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currrentTime.format(calForDate.getTime());


        final DatabaseReference addReview = FirebaseDatabase.getInstance().getReference().child("Reviews:");
        final HashMap<String,Object> reviewMap = new HashMap<>();
        reviewMap.put("pid",restaurantID);
        reviewMap.put("name",restaurantName.getText().toString());
        reviewMap.put("description",restaurantDescription.getText().toString());
        reviewMap.put("comment",restaurantComment.getText().toString());
        reviewMap.put("Stars",starValue);
        reviewMap.put("date",saveCurrentDate);
        reviewMap.put("time",saveCurrentTime);

        addReview.child("User Review").child(Prevalent.currentOnlineUser.getPhone())
                .child("Restaurants").child(restaurantID).updateChildren(reviewMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RestaurantDetailActivity.this,"Review Updated Successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RestaurantDetailActivity.this, Home.class);
                           startActivity(intent);

                        }

                    }
                });


    }

    private void getRestaurantDetails(String restaurantID)
    {
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        restaurantRef.child(restaurantID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
              if(dataSnapshot.exists())
              {
                  Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);

                  restaurantName.setText(restaurant.getRname());
                  restaurantDescription.setText(restaurant.getRdetails());
                  Picasso.get().load(restaurant.getImage()).into(restaurantImage);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
