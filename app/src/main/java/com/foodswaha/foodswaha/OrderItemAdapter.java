package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pharshar on 11/30/2015.
 */
public class OrderItemAdapter extends ArrayAdapter<OrderItem> {

    Context context;
    int layoutResourceId;
    List data = null;

    public OrderItemAdapter(Context context, int resource, List<OrderItem> objects) {
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

            holder.name = (TextView)row.findViewById(R.id.name);
            holder.quantity = (TextView)row.findViewById(R.id.quantity);
            holder.price = (TextView)row.findViewById(R.id.price);
            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        OrderItem orderItem = (OrderItem)data.get(position);
        holder.name.setText(orderItem.getName());
        holder.quantity.setText("("+orderItem.getQuantity()+")");
        holder.price.setText(orderItem.getPrice()+" Rs ");

        return row;
    }
    class Holder{
        TextView name;
        TextView price;
        TextView quantity;
    }

}
