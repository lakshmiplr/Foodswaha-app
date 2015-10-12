package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pharshar on 10/10/2015.
 */
public class HotelItemAdapter extends ArrayAdapter<HotelItem> {
    Context context;
    int layoutResourceId;
    List data = null;

    public HotelItemAdapter(Context context, int resource, List<HotelItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HotelItemHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HotelItemHolder();

            //holder.hotelId =  (TextView)row.findViewById(R.id.hotelId);
            holder.hotelName = (TextView)row.findViewById(R.id.hotelName);
            holder.hotelAddress = (TextView)row.findViewById(R.id.hotelAddress);
            holder.hotelDTime = (TextView)row.findViewById(R.id.hotelDtime);
            holder.hotelDFee = (TextView)row.findViewById(R.id.hotelDFee);
            holder.hotelMinOrder = (TextView)row.findViewById(R.id.hotelMinOrder);
            holder.hotelOnTime = (TextView)row.findViewById(R.id.hotelOnTime);
            holder.hotelRatings = (TextView)row.findViewById(R.id.hotelRatings);
            holder.hotelTimings = (TextView)row.findViewById(R.id.hotelTimings);

            row.setTag(holder);
        }
        else{
            holder = (HotelItemHolder)row.getTag();
        }

        HotelItem hotelItem = (HotelItem)data.get(position);
       // holder.hotelId.setText(hotelItem.getId());
        holder.hotelName.setText(hotelItem.getName());
        holder.hotelAddress.setText(hotelItem.getAddress());
        holder.hotelDTime.setText(": "+hotelItem.getDeliveryTime()+" min ");
        if(hotelItem.getDeliveryFee().equals("25")){
            ImageView deliveryCharge = (ImageView)row.findViewById(R.id.delivercharge);
            deliveryCharge.setImageResource(R.drawable.free_delivery);
        }
        else{
            holder.hotelDFee.setText(" : " +hotelItem.getDeliveryFee()+" Rs");
        }
        holder.hotelMinOrder.setText("min order : "+hotelItem.getMinOrder()+" Rs");
        holder.hotelOnTime.setText("on time : "+hotelItem.getOnTime()+" % ");
        holder.hotelRatings.setText(": "+hotelItem.getRating());
        holder.hotelTimings.setText("timings : "+hotelItem.getTimings());

        return row;
    }
    class HotelItemHolder{
        //TextView hotelId;
        TextView hotelName;
        TextView hotelAddress;
        TextView hotelDTime;
        TextView hotelDFee;
        TextView hotelMinOrder;
        TextView hotelOnTime;
        TextView hotelRatings;
        TextView hotelTimings;
    }
}
