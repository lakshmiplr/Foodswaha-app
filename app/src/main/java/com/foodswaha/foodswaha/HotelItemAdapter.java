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
public class HotelItemAdapter extends ArrayAdapter<HotelItem> implements Filterable{
    private static final String TAG = "HotelItemAdapter";

    Context context;
    int layoutResourceId;
    List data = null;
    List origData;

    public HotelItemAdapter(Context context, int resource, List<HotelItem> objects) {

        super(context, resource, objects);
        Log.e(TAG, " HotelItemAdapter constructor  started.");
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
        this.origData = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e(TAG, " getView method called with position,"+position);
        View row = convertView;
        HotelItemHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HotelItemHolder();

            //holder.hotelId =  (TextView)row.findViewById(R.id.hotelId);

            holder.hotelName = (TextView)row.findViewById(R.id.hotelName);
            holder.hotelAddress = (TextView)row.findViewById(R.id.hotelAddress);
            holder.hotelDTime = (TextView)row.findViewById(R.id.hotelDTime);
            holder.hotelDFee = (TextView)row.findViewById(R.id.hotelDFee);
            holder.hotelMinOrder = (TextView)row.findViewById(R.id.hotelMinOrder);
            holder.hotelOnTime = (TextView)row.findViewById(R.id.hotelOnTime);
            holder.hotelRatings = (RatingBar)row.findViewById(R.id.hotelRatings);
            //holder.hotelTimings = (TextView)row.findViewById(R.id.hotelTimings);
            holder.hotelImage = (NetworkImageView)row.findViewById(R.id.hotelImage);
            holder.hotelFoodTypes = (TextView)row.findViewById(R.id.hotelFoodTypes);
            holder.hoteldeliveryChargeImage = (ImageView)row.findViewById(R.id.deliveryCharge);
           // holder.rating = (TextView)row.findViewById(R.id.rating);

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
        if(hotelItem.getDeliveryFee().equals("0")){
            holder.hoteldeliveryChargeImage.setImageResource(R.drawable.free_delivery);
            holder.hotelDFee.setText("");
        }
        else{
            holder.hoteldeliveryChargeImage.setImageResource(R.drawable.delivercharge);
            holder.hotelDFee.setText(" : " +hotelItem.getDeliveryFee()+" Rs");
        }
        holder.hotelMinOrder.setText(hotelItem.getMinOrder()+" Rs");
        holder.hotelOnTime.setText(hotelItem.getOnTime() + "%");
        holder.hotelRatings.setRating(Float.valueOf(hotelItem.getRating()));
        //holder.hotelTimings.setText("timings : " + hotelItem.getTimings());
        //holder.rating.setText(hotelItem.getRating());
        holder.hotelFoodTypes.setText(hotelItem.getFoodTypes());
        String imageUrl = "http://104.155.202.28:8080"+hotelItem.getImageUrl();
        setImage(holder.hotelImage, imageUrl);

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
        RatingBar hotelRatings;
        TextView hotelTimings;
        NetworkImageView hotelImage;
        TextView hotelFoodTypes;
        ImageView hoteldeliveryChargeImage;
        TextView rating;
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
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.e(TAG, " performFiltering called");

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<HotelItem> list = origData;

                final List newlist = new ArrayList<HotelItem>();

                String filterableString ;

                for (HotelItem hotelItem : list) {
                    filterableString = hotelItem.getName();
                    if (filterableString.toLowerCase().startsWith(filterString)) {
                        newlist.add(hotelItem);
                    }
                }
                results.values = newlist;
                results.count = newlist.size();
                return results;            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.e(TAG, " publishResults called "+results.count);

                    data = (ArrayList<HotelItem>) results.values;
                    notifyDataSetChanged();
            }
        };
    }
}
