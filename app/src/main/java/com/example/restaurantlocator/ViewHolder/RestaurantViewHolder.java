package com.example.restaurantlocator.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.restaurantlocator.Interface.ItemClickListner;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.example.restaurantlocator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtRestauranttName, txtRestaurantDescription, txtRestaurantCatagory, txtdisplayNumberOfLikes, commentPostTextView ;
    public ImageView imageView;
    public ItemClickListner listner;

    public ImageButton likeButton, commentpostButton;
    View mView;
    int countLikes;
    String currentUserPone;
    DatabaseReference LikesRef;



    public RestaurantViewHolder(@NonNull View itemView)
    {
        super(itemView);
        mView = itemView;

        imageView = (ImageView) itemView.findViewById(R.id.restaurant_image);
        txtRestauranttName = (TextView) itemView.findViewById(R.id.restaurant_name);
        txtRestaurantDescription = (TextView) itemView.findViewById(R.id.restaurant_description);
        txtRestaurantCatagory = itemView.findViewById(R.id.restaurant_catagory);

        commentpostButton =(ImageButton) itemView.findViewById(R.id.comment_btn);
        commentPostTextView =(TextView) itemView.findViewById(R.id.add_comment);

        likeButton = (ImageButton) itemView.findViewById(R.id.like_bttn);
        txtdisplayNumberOfLikes =(TextView) itemView.findViewById(R.id.displayNoOfLikes);

        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        currentUserPone = Prevalent.currentOnlineUser.getPhone();
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);

    }

    public void setLikeButtonStatus(final String pid)
    {
        LikesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(pid).hasChild(currentUserPone))
                {
                    countLikes = (int) dataSnapshot.child(pid).getChildrenCount(); //count number of likes
                    likeButton.setImageResource(R.drawable.like);
                    txtdisplayNumberOfLikes.setText(Integer.toString(countLikes));
                }
                else
                {
                    countLikes = (int) dataSnapshot.child(pid).getChildrenCount(); //count number of likes
                    likeButton.setImageResource(R.drawable.dislike);
                    txtdisplayNumberOfLikes.setText(Integer.toString(countLikes));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
