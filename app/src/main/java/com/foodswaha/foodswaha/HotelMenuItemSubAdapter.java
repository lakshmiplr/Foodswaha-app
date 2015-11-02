package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pharshar on 11/2/2015.
 */
public class HotelMenuItemSubAdapter extends ArrayAdapter<HotelMenuItemSub> {
    private static final String TAG = "HotelMenuItemSubAdapter";

    Context context;
    int layoutResourceId;
    List data = null;

    public HotelMenuItemSubAdapter(Context context, int resource, List<HotelMenuItemSub> objects) {

        super(context, resource, objects);
        Log.e(TAG, " HotelMenuItemSubAdapter constructor  started.");
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e(TAG, " getView method called with position,"+position);
        View row = convertView;
        HotelMenuItemSubHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HotelMenuItemSubHolder();

            holder.hotelMenuItemSubName = (TextView)row.findViewById(R.id.hotelMenuSubItem);
            holder.hotelMenuItemSubCost = (TextView)row.findViewById(R.id.hotelMenuSubItemPrice);
            row.setTag(holder);
        }
        else{
            holder = (HotelMenuItemSubHolder)row.getTag();
        }

        HotelMenuItemSub hotelMenuItemSub = (HotelMenuItemSub)data.get(position);
        try{
            holder.hotelMenuItemSubName.setText(hotelMenuItemSub.getName());
            holder.hotelMenuItemSubCost.setText(hotelMenuItemSub.getCost()+" Rs");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return row;
    }
    class HotelMenuItemSubHolder{
        TextView hotelMenuItemSubName;
        TextView hotelMenuItemSubCost;

    }
}
