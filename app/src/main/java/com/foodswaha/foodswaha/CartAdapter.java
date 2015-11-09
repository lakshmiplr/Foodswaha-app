package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pharshar on 11/2/2015.
 */
public class CartAdapter extends ArrayAdapter<SubMenu> {
    private static final String TAG = "HotelMenuItemSubAdapter";

    Context context;
    int layoutResourceId;
    List data = null;

    Cart cartInstance = Cart.getInstance();
    List subMenuList;
    private static HashMap menuToSubMenuMap = DisplayMenuActivity.getMenuToSubMenuMap();

    public CartAdapter(Context context, int resource, List<SubMenu> objects) {

        super(context, resource, objects);
        Log.e(TAG, " HotelMenuItemSubAdapter constructor  started.");
        this.context = context;
        this.layoutResourceId = resource;
        this.data = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();

            holder.name = (TextView)row.findViewById(R.id.name);
            holder.price = (TextView)row.findViewById(R.id.price);
            holder.quantity = (TextView) row.findViewById(R.id.quantity);
            holder.quantity.setTypeface(null, Typeface.BOLD);
            holder.minus = (ImageButton) row.findViewById(R.id.minus);
            holder.plus = (ImageButton) row.findViewById(R.id.plus);

            final TextView quantity = holder.quantity;
            final ImageButton plus =holder.plus;
            final ImageButton minus = holder.minus;
            final TextView itemName = holder.name;
            final TextView itemPrice = holder.price;
            final ViewGroup reltiveLayout = (ViewGroup)parent.getParent();
            final View cartLinearLayout = reltiveLayout.findViewById(R.id.cartRelativeLayOut);
            final TextView itemPosition = holder.position;
            final TextView hotelName = holder.hotelName;
            final TextView menuName = holder.menuName;
            final TextView idText = holder.id;

            holder.plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(quantity.getText().toString());
                    int id = Integer.parseInt(idText.getText().toString());
                    count++;
                    quantity.setText(String.valueOf(count));
                    if (count == 1) {
                        quantity.setVisibility(View.VISIBLE);
                        minus.setVisibility(View.VISIBLE);
                        SubMenu subMenu = new SubMenu(id,itemName.getText().toString(),itemPrice.getText().toString().replace("Rs", "").trim(),count,menuName.getText().toString(),hotelName.getText().toString());
                        cartInstance.putItemToCart(id, subMenu);
                    }else{
                        SubMenu subMenu = cartInstance.getCartItem(id);
                        subMenu.setQuantity(count);
                    }

                    cartInstance.incrementCountOfItems();
                    cartInstance.addToTotalBill(Integer.valueOf(itemPrice.getText().toString().replace("Rs", "").trim()));
                    if(cartInstance.getCountOfItems()==1){
                        cartLinearLayout.setVisibility(View.VISIBLE);
                    }
                    ((TextView)cartLinearLayout.findViewById(R.id.cart)).setText(String.valueOf(cartInstance.getCountOfItems()));
                    ((TextView)cartLinearLayout.findViewById(R.id.cart)).setTypeface(null, Typeface.BOLD);
                    ((TextView)cartLinearLayout.findViewById(R.id.total)).setText(String.valueOf(cartInstance.getTotalBill()));

                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(quantity.getText().toString());
                    int id = Integer.parseInt(idText.getText().toString());
                    count--;
                    if (count == 0) {
                        quantity.setVisibility(View.GONE);
                        minus.setVisibility(View.GONE);
                        cartInstance.removeItemFromCart(id);
                    }else{
                        SubMenu subMenu = cartInstance.getCartItem(id);
                        subMenu.setQuantity(count);
                    }
                    quantity.setText(String.valueOf(count));
                    cartInstance.decrementCountOfItems();
                    cartInstance.removeFromTotalBill(Integer.valueOf(itemPrice.getText().toString().replace("Rs", "").trim()));

                    if(cartInstance.getCountOfItems()>0){
                        ((TextView)cartLinearLayout.findViewById(R.id.cart)).setText(String.valueOf(cartInstance.getCountOfItems()));
                        ((TextView)cartLinearLayout.findViewById(R.id.cart)).setTypeface(null, Typeface.BOLD);
                        ((TextView)cartLinearLayout.findViewById(R.id.total)).setText(String.valueOf(cartInstance.getTotalBill()));
                        notifyDataSetChanged();
                    }else{
                        cartLinearLayout.setVisibility(View.GONE);

                        if(context instanceof DisplayCartActivity){
                            if(( (DisplayCartActivity)context).from.equals("submenu")){
                                Intent i = new Intent(context,DisplaySubMenuActivity.class);
                                ((Activity)getContext()).startActivity(i);
                            }
                            else{
                                Intent i = new Intent(context,DisplayMenuActivity.class);
                                ((Activity)getContext()).startActivity(i);
                            }
                        }
                    }
                }
            });

            row.setTag(holder);
        }
        else{
            holder = (Holder)row.getTag();
        }

        holder.position.setText(String.valueOf(position));
        SubMenu subMenu = (SubMenu)data.get(position);
        holder.id.setText(String.valueOf(subMenu.getId()));
        try{
            holder.menuName.setText(subMenu.getMenuName());
            holder.hotelName.setText(subMenu.getHotelName());
            holder.name.setText(subMenu.getName());
            holder.price.setText(subMenu.getCost()+" Rs");
            if(subMenu.getQuantity()>0){
                holder.quantity.setText(String.valueOf(subMenu.getQuantity()));
                holder.quantity.setVisibility(View.VISIBLE);
                holder.minus.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return row;
    }
    class Holder{
        TextView id = new TextView(getContext());
        TextView name;
        TextView price;
        TextView quantity;
        ImageButton plus;
        ImageButton minus;
        TextView position = new TextView(getContext());
        TextView hotelName = new TextView(getContext());
        TextView menuName = new TextView(getContext());

    }
}
