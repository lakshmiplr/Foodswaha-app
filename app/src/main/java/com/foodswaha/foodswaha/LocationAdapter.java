package com.foodswaha.foodswaha;

/**
 * Created by reddyl on 05-11-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter<String> extends ArrayAdapter<String> implements Filterable
{
    Context context;
    int layoutResourceId;

    public List orgData = new ArrayList<String>();
    public List areas = new ArrayList<String>();
    public LocationAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.areas = objects;
        setAreaList();
    }
    public void setAreaList()
    {
        orgData.add("BTM Layout 2nd stage");
        orgData.add("BTM Layout 1st stage");
        orgData.add("BTM Layout");
        orgData.add("JP Nagar");
        orgData.add("Nagavara");
    }


    @Override
    public int getCount(){
        return areas.size();
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                java.lang.String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<String> list = (List<String>) orgData;

                final List newlist = new ArrayList<String>();

                if (!filterString.equals("")){
                    for (String area : list) {
                        if (area.toString().toLowerCase().startsWith(filterString)) {
                            newlist.add(area);
                        }
                    }
                }
                results.values = newlist;
                results.count = newlist.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                areas = (List<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AreaHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new AreaHolder();

            holder.areaName = (TextView)row.findViewById(R.id.menuName);
            row.setTag(holder);
        }
        else{
            holder = (AreaHolder)row.getTag();
        }

        String area = (String)areas.get(position);
        if (holder!=null)
        holder.areaName.setText(area.toString());


        return row;
    }
    class AreaHolder {
        TextView areaName;

    }
}