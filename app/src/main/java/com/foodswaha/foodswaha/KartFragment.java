package com.foodswaha.foodswaha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pharshar on 9/24/2015.
 */
public class KartFragment extends Fragment {

    Cart mCart = AppInitializerActivity.getCartInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_kart, container, false);
        //Intent gotoCart = new Intent(getContext(),DisplayCartActivity.class);
        //startActivity(gotoCart);
        View view =inflater.inflate(R.layout.fragment_kart, container, false);
        if(mCart.getCountOfItems()==0){
            view.findViewById(R.id.emptyCart).setVisibility(View.VISIBLE);
            view.findViewById(R.id.cart_show_linear).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.emptyCart).setVisibility(View.GONE);
            view.findViewById(R.id.cart_show_linear).setVisibility(View.VISIBLE);

            //((DisplayHotelsActivity)getActivity()).cartTab.setText(String.valueOf(mCart.getCountOfItems()));
            ((TextView)view.findViewById(R.id.cart_show)).setText(String.valueOf(mCart.getCountOfItems()));
            ((TextView)view.findViewById(R.id.cart_show_total)).setText(String.valueOf(mCart.getTotalBill()));

            List<HotelMenuItemSub> mCartItemList = (List<HotelMenuItemSub>) mCart.getFoodItems();

            ListView listView1 = (ListView)view.findViewById(R.id.cartList);
            listView1.setAdapter(new CartItemAdapterForHome(getContext(),
                    R.layout.activity_display_cart_items, mCartItemList));
        }
        return view;
    }

}
