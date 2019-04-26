package com.example.restaurantlocator.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantlocator.Interface.ItemClickListner;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.example.restaurantlocator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsViewHolder extends RecyclerView.ViewHolder
    {
    public TextView txtName,txtComment, txtDate, txtTime  ;
    public CircleImageView profileImageView;



    View mView;
    String currentUserPone;



    public CommentsViewHolder(@NonNull View itemView)
    {
        super(itemView);
        mView = itemView;

        txtName = (TextView) itemView.findViewById(R.id.comment_username);
        txtComment = (TextView) itemView.findViewById(R.id.comment_text);
        txtDate = itemView.findViewById(R.id.comment_date);
        txtTime =(TextView) itemView.findViewById(R.id.comment_time);
        profileImageView =  itemView.findViewById(R.id.profile_image);

        currentUserPone = Prevalent.currentOnlineUser.getPhone();
    }

}
