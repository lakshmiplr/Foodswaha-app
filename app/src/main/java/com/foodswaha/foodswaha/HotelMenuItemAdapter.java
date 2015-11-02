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
 * Created by pharshar on 11/1/2015.
 */
public class HotelMenuItemAdapter extends ArrayAdapter<HotelMenuItem> {

    private static final String TAG = "HotelMenuItemAdapter";

    Context context;
    int layoutResourceId;
    List data = null;

    public HotelMenuItemAdapter(Context context, int resource, List<HotelMenuItem> objects) {

        super(context, resource, objects);
        Log.e(TAG, " HotelMenuItemAdapter constructor  started.");
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e(TAG, " getView method called with position,"+position);
        View row = convertView;
        HotelMenuItemHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HotelMenuItemHolder();

            holder.hotelMenuItemName = (TextView)row.findViewById(R.id.hotelMenuItem);
            row.setTag(holder);
        }
        else{
            holder = (HotelMenuItemHolder)row.getTag();
        }

        HotelMenuItem hotelMenuItem = (HotelMenuItem)data.get(position);
        holder.hotelMenuItemName.setText(hotelMenuItem.getName());


        return row;
    }
    class HotelMenuItemHolder{
        TextView hotelMenuItemName;

    }

}

