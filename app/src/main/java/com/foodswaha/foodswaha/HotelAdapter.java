package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Filter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pharshar on 10/10/2015.
 */
public class HotelAdapter extends ArrayAdapter<Hotel> implements Filterable{
    private static final String TAG = "HotelAdapter";
    private static final String HOTEL_IMAGE_REQUEST_URL = "http://104.155.202.28:8080/";


    Context context;
    int layoutResourceId;
    List data = null;
    List origData;

    public HotelAdapter(Context context, int resource, List<Hotel> objects) {

        super(context, resource, objects);
        Log.e(TAG, " HotelAdapter constructor  started.");
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
        this.origData = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        Holder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();

            holder.hotelName = (TextView)row.findViewById(R.id.hotelName);
            holder.hotelAddress = (TextView)row.findViewById(R.id.hotelAddress);
            holder.hotelDTime = (TextView)row.findViewById(R.id.hotelDTime);
            holder.hotelDFee = (TextView)row.findViewById(R.id.hotelDFee);
            holder.hotelMinOrder = (TextView)row.findViewById(R.id.hotelMinOrder);
            holder.hotelOnTime = (TextView)row.findViewById(R.id.hotelOnTime);
            holder.hotelRatings = (RatingBar)row.findViewById(R.id.hotelRatings);
            holder.hotelImage = (NetworkImageView)row.findViewById(R.id.hotelImage);
            holder.hotelFoodTypes = (TextView)row.findViewById(R.id.hotelFoodTypes);
            holder.hoteldeliveryChargeImage = (ImageView)row.findViewById(R.id.deliveryCharge);

            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        Hotel hotel = (Hotel)data.get(position);
        holder.hotelId = hotel.getId();
        holder.hotelName.setText(hotel.getName());
        holder.hotelAddress.setText(hotel.getAddress());
        holder.hotelDTime.setText(": "+hotel.getDeliveryTime()+" min ");
        if(hotel.getDeliveryFee().equals("0")){
            holder.hoteldeliveryChargeImage.setImageResource(R.drawable.free_delivery);
            holder.hotelDFee.setText("");
        }
        else{
            holder.hoteldeliveryChargeImage.setImageResource(R.drawable.deliverycharge_blue);
            holder.hotelDFee.setText(" : " +hotel.getDeliveryFee()+" Rs");
        }
        holder.hotelMinOrder.setText(hotel.getMinOrder()+" Rs");
        holder.hotelOnTime.setText(hotel.getOnTime() + "%");
        holder.hotelRatings.setRating(Float.valueOf(hotel.getRating()));
        holder.hotelFoodTypes.setText(hotel.getFoodTypes());
        String imageUrl = HOTEL_IMAGE_REQUEST_URL+hotel.getImageUrl();
        setImage(holder.hotelImage, imageUrl);

        return row;
    }
    class Holder{
        String hotelId;
        TextView hotelName;
        TextView hotelAddress;
        TextView hotelDTime;
        TextView hotelDFee;
        TextView hotelMinOrder;
        TextView hotelOnTime;
        RatingBar hotelRatings;
        NetworkImageView hotelImage;
        TextView hotelFoodTypes;
        ImageView hoteldeliveryChargeImage;
    }
    public void setImage(final NetworkImageView image,final String url) {
        ImageLoader imageLoader = VolleyRequestQueueFactory.getInstance().getImageLoader();
        image.setImageUrl(url, imageLoader);
    }

    @Override
    public int getCount(){
        return data.size();
    }

    public Filter getFilter()
    {
        return new Filter() {
            @Override
            public FilterResults performFiltering(CharSequence constraint) {
                Log.e(TAG, " performFiltering called");

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<Hotel> list = origData;

                final List newlist = new ArrayList<Hotel>();

                String filterableString ;

                for (Hotel hotel: list) {
                    filterableString = hotel.getName();
                    if (filterableString.toLowerCase().startsWith(filterString)) {
                        newlist.add(hotel);
                    }
                }
                results.values = newlist;
                results.count = newlist.size();
                return results;
            }

            @Override
            public void publishResults(CharSequence constraint, FilterResults results) {
                Log.e(TAG, " publishResults called "+results.count);
                data = (ArrayList<Hotel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
