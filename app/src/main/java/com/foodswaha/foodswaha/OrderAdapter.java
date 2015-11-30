package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by pharshar on 11/29/2015.
 */
public class OrderAdapter extends ArrayAdapter<Order> {

    Context context;
    int layoutResourceId;
    List data = null;

    public OrderAdapter(Context context, int resource, List<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Holder holder =null;
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();

            holder.hotelImage = (NetworkImageView)row.findViewById(R.id.hotelimage);
            holder.hotelName = (TextView)row.findViewById(R.id.hotelname);
            holder.hotelArea = (TextView)row.findViewById(R.id.area);
            holder.date = (TextView)row.findViewById(R.id.date);
            holder.time = (TextView)row.findViewById(R.id.time);
            holder.total = (TextView)row.findViewById(R.id.total);
            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        Order order = (Order)data.get(position);
        holder.hotelName.setText(order.getHotelName());
        holder.hotelArea.setText(order.getArea());
        holder.date.setText(order.getDate());
        holder.time.setText(order.getTime());
        holder.total.setText(order.getTotal()+" Rs");
        setImage(holder.hotelImage, order.getImageUrl());

        return row;
    }
    class Holder{
        TextView hotelName;
        NetworkImageView hotelImage;
        TextView hotelArea;
        TextView date;
        TextView time;
        TextView total;
    }

    public void setImage(final NetworkImageView image,final String url) {
        ImageLoader imageLoader = VolleyRequestQueueFactory.getInstance().getImageLoader();
        image.setImageUrl(url, imageLoader);
    }

}
