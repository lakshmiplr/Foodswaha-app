package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by pharshar on 11/2/2015.
 */
public class CartItemAdapterForHome extends ArrayAdapter<HotelMenuItemSub> {
    private static final String TAG = "HotelMenuItemSubAdapter";

    Context context;
    int layoutResourceId;
    List data = null;

    Cart mCart = AppInitializerActivity.getCartInstance();
    List mHotelMenuItemSubList;
    Map menuItemMap = DisplayHotelMenuActivity.getHotelMenuItemMap();


    public CartItemAdapterForHome(Context context, int resource, List<HotelMenuItemSub> objects) {

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
            holder.quantity.setTypeface(null, Typeface.BOLD);
            holder.minus = (ImageButton) row.findViewById(R.id.minus);
            holder.plus = (ImageButton) row.findViewById(R.id.plus);
            final TextView quantity = holder.quantity;
            final ImageButton plus =holder.plus;
            final ImageButton minus = holder.minus;
            final TextView itemName = holder.hotelMenuItemSubName;
            final TextView itemCost = holder.hotelMenuItemSubCost;
            final TextView mposition = holder.position;
            final TextView hotelName = holder.hotelName;
            final TextView menuName = holder.menuName;
            final ViewGroup mListView = (ViewGroup)parent.getParent();
            final View mLinearLayout = mListView.findViewById(R.id.cart_show_linear);

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
                    HotelMenuItemSub mHotelMenuItemSub = new HotelMenuItemSub(itemName.getText().toString(),itemCost.getText().toString().replace("Rs", "").trim(),mCount,menuName.getText().toString(),hotelName.getText().toString());
                    if(mCart.getFoodItems().contains(mHotelMenuItemSub)){
                        mCart.getFoodItems().remove(mHotelMenuItemSub);
                    }
                    mCart.getFoodItems().add(mHotelMenuItemSub);
                    mCart.setCountOfItems(mCart.getCountOfItems() + 1);
                    mCart.setTotalBill(mCart.getTotalBill() + Integer.parseInt(mHotelMenuItemSub.getCost()));

                    if(mCart.getCountOfItems()>0){

                        ((TextView)mLinearLayout.findViewById(R.id.cart_show)).setText(String.valueOf(mCart.getCountOfItems()));
                        ((TextView)mLinearLayout.findViewById(R.id.cart_show_total)).setText(String.valueOf(mCart.getTotalBill()));
                        //((DisplayHotelsActivity)context).cartTab.setText(String.valueOf(mCart.getCountOfItems()));
                    }
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
                    HotelMenuItemSub mHotelMenuItemSub = new HotelMenuItemSub(itemName.getText().toString(),itemCost.getText().toString().replace("Rs", "").trim(),mCount,menuName.getText().toString(),hotelName.getText().toString());
                    if(mCart.getFoodItems().contains(mHotelMenuItemSub) ){
                        int count = ((HotelMenuItemSub)mCart.getFoodItems().get(mCart.getFoodItems().indexOf(mHotelMenuItemSub))).getQuantity();
                        if( count>1 ){
                            ((HotelMenuItemSub)mCart.getFoodItems().get(mCart.getFoodItems().indexOf(mHotelMenuItemSub))).setQuantity(count-1);
                        }
                        else if (count==1){

                            List<HotelMenuItemSub> mHotelMenuItemSubList = (List<HotelMenuItemSub>) menuItemMap.get(menuName.getText().toString());
                            ((HotelMenuItemSub)mHotelMenuItemSubList.get(mHotelMenuItemSubList.indexOf(mHotelMenuItemSub))).setQuantity(0);
                            menuItemMap.put(menuName.getText().toString(),mHotelMenuItemSubList);
                            mCart.getFoodItems().remove(mHotelMenuItemSub);
                        }
                        mCart.setCountOfItems(mCart.getCountOfItems() - 1);
                        mCart.setTotalBill(mCart.getTotalBill() - Integer.parseInt(mHotelMenuItemSub.getCost()));
                        ((TextView)mLinearLayout.findViewById(R.id.cart_show)).setText(String.valueOf(mCart.getCountOfItems()));
                        ((TextView)mLinearLayout.findViewById(R.id.cart_show_total)).setText(String.valueOf(mCart.getTotalBill()));
                        //((DisplayHotelsActivity)context).cartTab.setText(String.valueOf(mCart.getCountOfItems()));
                        notifyDataSetChanged();
                    }
                    if(mCart.getCountOfItems()==0){

                        final TabLayout tabLayout = (TabLayout)((Activity)context).findViewById(R.id.tab_layout);
                        tabLayout.getTabAt(0).select();
                    }
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
            holder.menuName.setText(hotelMenuItemSub.getMenuName());
            holder.hotelName.setText(hotelMenuItemSub.getHotelName());
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
        TextView hotelName = new TextView(getContext());
        TextView menuName = new TextView(getContext());

    }
}
