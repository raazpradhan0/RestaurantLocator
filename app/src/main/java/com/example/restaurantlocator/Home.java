package com.example.restaurantlocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.restaurantlocator.Model.Restaurant;
import com.example.restaurantlocator.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Map;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.//OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.*/
public class Home extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    ViewFlipper viewFlipper;
    GridLayout mainGrid;


    //private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        int image[]={R.drawable.chcikenstation, R.drawable.kfc_offer, R.drawable.pizzahut_offer};
        mainGrid = rootView.findViewById(R.id.mainGrid);
        viewFlipper = rootView.findViewById(R.id.slider);

        setSingleEvent(mainGrid);

        for(int i=0;i<image.length;i++)
        {
            flipperImages(image[i]);
        }
        // Inflate the layout for this fragment
        return rootView;

    }

    public void flipperImages(int image)
    {
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);//4 sec
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);

    }

    public void setSingleEvent(GridLayout mainGrid)
    {
        for (int i=0; i <mainGrid.getChildCount();i++)
        {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(finalI == 0)
                    {
                        MapsActivity map= new MapsActivity();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fLcontent,map, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if(finalI == 1)
                    {
                        Catagories map= new Catagories();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fLcontent,map, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if(finalI == 2)
                    {
                        Profile map= new Profile();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fLcontent,map, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                    else if(finalI == 3)
                    {
                        Search map= new Search();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fLcontent,map, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;

    }



}
