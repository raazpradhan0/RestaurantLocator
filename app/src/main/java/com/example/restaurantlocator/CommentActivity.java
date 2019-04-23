package com.example.restaurantlocator;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.restaurantlocator.Model.Comments;
import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.example.restaurantlocator.ViewHolder.CommentsViewHolder;
import com.example.restaurantlocator.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class CommentActivity extends AppCompatActivity
{
    private ImageButton postCommentButton;
    private EditText commentInputText;
    private RecyclerView commentsList;

    private DatabaseReference UsersRef, resrurantDatabaseRef;

    private  String Restaurant_ID, current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Restaurant_ID = getIntent().getExtras().get("pid").toString();

        current_user_id = Prevalent.currentOnlineUser.getPhone();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        resrurantDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(Restaurant_ID).child("Comments");

        commentsList = (RecyclerView) findViewById(R.id.comment_list);
        commentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentsList.setLayoutManager(linearLayoutManager);

        commentInputText =(EditText) findViewById(R.id.comment_input);
        postCommentButton =(ImageButton) findViewById(R.id.post_comment_btn);

        postCommentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            String phone  = dataSnapshot.child("phone").getValue().toString();
                            ValidateComment(phone);
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



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options =  new FirebaseRecyclerOptions
                .Builder<Comments>()
                .setQuery(resrurantDatabaseRef,Comments.class)
                .build();
        FirebaseRecyclerAdapter<Comments, CommentsViewHolder> adapter = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options)
        {


            @Override
                    protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model)
                    {
                        holder.txtName.setText(model.getName());
                        holder.txtComment.setText(model.getComment());
                        holder.txtDate.setText(model.getDate());
                        holder.txtTime.setText(model.getTime());
                        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(holder.profileImageView);

                    }


            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_comments_layout,viewGroup,false);
                CommentsViewHolder holder = new CommentsViewHolder(view);
                return holder;
            }
                };

        commentsList.setAdapter(adapter);
        adapter.startListening();
    }


    private void ValidateComment(String phone)
    {
        String commentText = commentInputText.getText().toString();

        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this,"Please Write Text to Comment",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            final String saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
           final String saveCurrentTime = currentTime.format(calendar.getTime());

           final String  randomKey = current_user_id + saveCurrentDate + saveCurrentTime;

            HashMap commentMap  = new HashMap();
            commentMap.put("uid", current_user_id);
            commentMap.put("comment", commentText);
            commentMap.put("date", saveCurrentDate);
            commentMap.put("name", Prevalent.currentOnlineUser.getName());
            commentMap.put("time", saveCurrentTime);
            commentMap.put("phone", phone);

            resrurantDatabaseRef.child(randomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(CommentActivity.this, "Comment Updated",Toast.LENGTH_SHORT).show();
                        commentInputText.getText().clear();

                    }
                    else
                    {
                        Toast.makeText(CommentActivity.this, "Error Try Again!!",Toast.LENGTH_SHORT).show();
                    }

                }
            });



        }
    }
}
