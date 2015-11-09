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
public class MenuAdapter extends ArrayAdapter<Menu> {

    private static final String TAG = "MenuAdapter";
    Context context;
    int layoutResourceId;
    List data = null;

    public MenuAdapter(Context context, int resource, List<Menu> objects) {

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
        Holder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();

            holder.menuName = (TextView)row.findViewById(R.id.menuName);
            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        Menu menu = (Menu)data.get(position);
        holder.menuName.setText(menu.getName());


        return row;
    }
    class Holder{
        TextView menuName;

    }

}

