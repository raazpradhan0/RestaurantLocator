package com.example.restaurantlocator;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<String>
{

    private  String[] restaurantCatagory;
    private  Integer[] imageId;
    private Activity context;
    public CustomListView(Activity context, String[] restaurantCatagory, Integer[] imageId)
        {
        super(context, R.layout.fragment_catagories, restaurantCatagory);

        this.restaurantCatagory = restaurantCatagory;
        this.imageId = imageId;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r== null)
        {
            LayoutInflater  layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.fragment_catagories,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) r.getTag();
        }
            viewHolder.imageView.setImageResource(imageId[position]);
            viewHolder.textView.setText(restaurantCatagory[position]);
            return r;
    }
    class ViewHolder
    {
        TextView textView;
        ImageView imageView;
        ViewHolder (View v)
        {
            textView = (TextView) v.findViewById(R.id.textViewCategory);
            imageView =(ImageView) v.findViewById(R.id.catagoryrestaurant);
        }
    }
}
