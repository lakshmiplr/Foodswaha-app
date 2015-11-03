package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

    Cart mCart = AppInitializerActivity.getCartInstance();
    List mHotelMenuItemSubList;

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
            holder.quantity = (TextView) row.findViewById(R.id.quantity);
            holder.minus = (ImageButton) row.findViewById(R.id.minus);
            holder.plus = (ImageButton) row.findViewById(R.id.plus);
            final TextView quantity = holder.quantity;
            final ImageButton plus =holder.plus;
            final ImageButton minus = holder.minus;
            final TextView itemName = holder.hotelMenuItemSubName;
            final TextView itemCost = holder.hotelMenuItemSubCost;
            final ViewGroup reltiveLayout = (ViewGroup)parent.getParent();
            final View mcartLinearLayout = reltiveLayout.findViewById(R.id.cartLinearLayout);
            final TextView mposition = holder.position;

            holder.plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int mCount = Integer.parseInt(quantity.getText().toString());
                    mCount++;
                    quantity.setText(String.valueOf(mCount));
                    if (mCount == 1) {
                        quantity.setVisibility(View.VISIBLE);
                        minus.setVisibility(View.VISIBLE);
                    }
                    HotelMenuItemSub mHotelMenuItemSub = new HotelMenuItemSub(itemName.getText().toString(),itemCost.getText().toString(),mCount);
                    mCart.getFoodItems().add(mHotelMenuItemSub);
                    mCart.setCountOfItems(mCart.getCountOfItems() + 1);
                    mCart.setTotalBill(mCart.getTotalBill() + Integer.parseInt(mHotelMenuItemSub.getCost().replace("Rs", "").trim()));

                    if(mCart.getCountOfItems()>0){

                        ((TextView)mcartLinearLayout.findViewById(R.id.cart)).setVisibility(View.VISIBLE);
                        ((TextView)mcartLinearLayout.findViewById(R.id.total)).setVisibility(View.VISIBLE);
                        ((ImageButton)mcartLinearLayout.findViewById(R.id.checkout)).setVisibility(View.VISIBLE);
                        ((TextView)mcartLinearLayout.findViewById(R.id.cart)).setText(String.valueOf(mCart.getCountOfItems()));
                        ((TextView)mcartLinearLayout.findViewById(R.id.total)).setText(String.valueOf(mCart.getTotalBill()));
                    }
                    int index = Integer.parseInt(mposition.getText().toString());
                    HotelMenuItemSub hotelMenuItemSub = (HotelMenuItemSub)data.get(index);
                    hotelMenuItemSub.setQuantity(mCount);

                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int mCount = Integer.parseInt(quantity.getText().toString());
                    mCount--;
                    quantity.setText(String.valueOf(mCount));
                    if (mCount == 0) {
                        quantity.setVisibility(View.GONE);
                        minus.setVisibility(View.GONE);
                    }
                    HotelMenuItemSub mHotelMenuItemSub = new HotelMenuItemSub(itemName.getText().toString(),itemCost.getText().toString(),mCount);
                    if(mCart.getFoodItems().contains(mHotelMenuItemSub)){
                        mCart.getFoodItems().remove(mHotelMenuItemSub);
                        mCart.setCountOfItems(mCart.getCountOfItems() - 1);
                        mCart.setTotalBill(mCart.getTotalBill() - Integer.parseInt(mHotelMenuItemSub.getCost().replace("Rs","").trim()));
                        ((TextView)mcartLinearLayout.findViewById(R.id.cart)).setText(String.valueOf(mCart.getCountOfItems()));
                        ((TextView)mcartLinearLayout.findViewById(R.id.total)).setText(String.valueOf(mCart.getTotalBill()));
                    }
                    if(mCart.getCountOfItems()==0){
                        ((TextView)mcartLinearLayout.findViewById(R.id.cart)).setVisibility(View.GONE);
                        ((TextView)mcartLinearLayout.findViewById(R.id.total)).setVisibility(View.GONE);
                        ((ImageButton)mcartLinearLayout.findViewById(R.id.checkout)).setVisibility(View.GONE);
                    }
                    int index = Integer.parseInt(mposition.getText().toString());
                    HotelMenuItemSub hotelMenuItemSub = (HotelMenuItemSub)data.get(index);
                    hotelMenuItemSub.setQuantity(mCount);

                }
            });

            row.setTag(holder);
        }
        else{
            holder = (HotelMenuItemSubHolder)row.getTag();
        }

        holder.position.setText(String.valueOf(position));

        HotelMenuItemSub hotelMenuItemSub = (HotelMenuItemSub)data.get(position);
        try{
            holder.hotelMenuItemSubName.setText(hotelMenuItemSub.getName());
            holder.hotelMenuItemSubCost.setText(hotelMenuItemSub.getCost()+" Rs");
            if(hotelMenuItemSub.getQuantity()>0){
                holder.quantity.setText(String.valueOf(hotelMenuItemSub.getQuantity()));
                holder.quantity.setVisibility(View.VISIBLE);
                holder.minus.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return row;
    }
    class HotelMenuItemSubHolder{
        TextView hotelMenuItemSubName;
        TextView hotelMenuItemSubCost;
        TextView quantity;
        ImageButton plus;
        ImageButton minus;
        TextView position = new TextView(getContext());

    }
}
