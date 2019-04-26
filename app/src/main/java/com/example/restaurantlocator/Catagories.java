package com.example.restaurantlocator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.restaurantlocator.RestaurantCategory.RestaurantCategory;
import com.example.restaurantlocator.RestaurantCategory.RestaurantCategory1;
import com.example.restaurantlocator.RestaurantCategory.RestaurantCategory2;
import com.example.restaurantlocator.RestaurantCategory.RestaurantCategory3;
import com.example.restaurantlocator.RestaurantCategory.RestaurantCategory4;


public class Catagories extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ListView listView;
    String[] restaurantCatagory={"Nepali Cuisine","Fast Food","Cafe","Dining and Lunch","International Cuisine"};
    Integer[] imageId ={R.drawable.nepali_cuisine,R.drawable.fast_food,R.drawable.cafe,R.drawable.dining_and_lunch,R.drawable.international_cuisine};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_catagories, container, false);

        listView = (ListView)  rootView.findViewById(R.id.listviewCatagories);
        CategoryListView categoryListView = new CategoryListView();
        listView.setAdapter(categoryListView);
        listView.setScrollingCacheEnabled(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position ==0)
                {
                    Intent intent = new Intent(getActivity(), RestaurantCategory.class);
                    startActivity(intent);
                }
                 if (position ==1)
                {
                    Intent intent = new Intent(getActivity(), RestaurantCategory1.class);
                    startActivity(intent);
                }
                if (position ==2)
                {
                    Intent intent = new Intent(getActivity(), RestaurantCategory2.class);
                    startActivity(intent);
                }
                if (position ==3)
                {
                    Intent intent = new Intent(getActivity(), RestaurantCategory3.class);
                    startActivity(intent);
                }
                if (position ==4)
                {
                    Intent intent = new Intent(getActivity(), RestaurantCategory4.class);
                    startActivity(intent);
                }

            }
        });
        return rootView;
    }

    class CategoryListView extends BaseAdapter
    {

        @Override
        public int getCount() {
            return imageId.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent)
        {
            view = getLayoutInflater().inflate(R.layout.content_category,null,true);
            TextView textView = (TextView) view.findViewById(R.id.textViewCategory);
            ImageView imageView =(ImageView) view.findViewById(R.id.catagoryrestaurant);

            imageView.setImageResource(imageId[i]);
            textView.setText(restaurantCatagory[i]);


            return view;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


