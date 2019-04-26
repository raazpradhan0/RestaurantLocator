package com.example.restaurantlocator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.Model.User;
import com.example.restaurantlocator.Pravelent.Prevalent;
import com.example.restaurantlocator.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class NavigationDrawer extends AppCompatActivity {
    private DrawerLayout mDrawableLayout;
    private ActionBarDrawerToggle mToggle;

    private DatabaseReference ResturantRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Paper.init(this);


        mDrawableLayout =(DrawerLayout) findViewById(R.id.navigationDrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawableLayout,R.string.open,R.string.close);
        mDrawableLayout.addDrawerListener(mToggle);
        NavigationView nvDrawer =(NavigationView)  findViewById(R.id.nv);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawersContent(nvDrawer);
        FragmentManager manager =getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fLcontent, new Home()).commit();

        View headerView = nvDrawer.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.profile_image);


        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Restaurant> options =  new FirebaseRecyclerOptions
                .Builder<Restaurant>()
                .setQuery(ResturantRef,Restaurant.class)
                .build();
        FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull Restaurant model)
            {
                holder.txtRestauranttName.setText(model.getRname());
                holder.txtRestaurantDescription.setText(model.getRdetails());
                holder.txtRestaurantCatagory.setText(model.getCatagory());
                Picasso.get().load(model.getImage()).into(holder.imageView);

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

/*        recyclerView.setAdapter(adapter);
        adapter.startListening();*/



    }

    public void  selectItemDrawer(MenuItem menuItem)
    {
        Fragment myFragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.home:
                fragmentClass = Home.class;
                Home home = new Home();
                FragmentManager manager1 = getSupportFragmentManager();
                manager1.beginTransaction().replace(R.id.fLcontent,home).commit();
                break;
            case R.id.categories:
                fragmentClass = Catagories.class;
                Catagories category = new Catagories();
                FragmentManager manager3 = getSupportFragmentManager();
                manager3.beginTransaction().replace(R.id.fLcontent,category).commit();
                break;

            case R.id.map:
                MapsActivity map = new MapsActivity();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.fLcontent,map).commit();
                break;

            case R.id.search:
                Search search = new Search();
                FragmentManager manager4 = getSupportFragmentManager();
                manager4.beginTransaction().replace(R.id.fLcontent,search).commit();
                break;

            case  R.id.profile:
                Profile profile = new Profile();
                FragmentManager manager2 = getSupportFragmentManager();
                manager2.beginTransaction().replace(R.id.fLcontent,profile).commit();
                break;

            case R.id.settings:
                fragmentClass = Settings.class;
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Paper.book().destroy();
                Intent intent= new Intent(NavigationDrawer.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                break;

                default:
                    fragmentClass = Home.class;

        }

        try{
            myFragment =(Fragment) fragmentClass.newInstance();



        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        //FragmentManager fragmentManager = getSupportFragmentManager();
       //fragmentManager.beginTransaction().replace(R.id.fLcontent,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawableLayout.closeDrawers();

    }
    private void setupDrawersContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectItemDrawer(menuItem);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
       if(mToggle.onOptionsItemSelected(menuItem))
       {
           return true;
       }
        return super.onOptionsItemSelected(menuItem);

    }


    public void onClick(View view) {


    }


   /* @Override
    public void onBackPressed()
    {
        DrawerLayout drawerLayout =(DrawerLayout) findViewById(R.id.navigationDrawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
            finishAffinity();
        }

    }
*/
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

