package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class DisplayHotelMenuItemSub extends AppCompatActivity {

    Cart mCart = AppInitializerActivity.getCartInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotel_menu_item_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        displayHotelMenuItemSub();
    }

    private void displayHotelMenuItemSub(){
        String menuName = DisplayHotelMenuActivity.getHolder().hotelMenuItemName.getText().toString();

        final TextView areaText = (TextView)findViewById(R.id.menuNameText);
        areaText.setText(menuName);
        final View mcartLinearLayout = findViewById(R.id.cartLinearLayout);
        if(mCart.getCountOfItems()>0){

            ((TextView)mcartLinearLayout.findViewById(R.id.cart)).setVisibility(View.VISIBLE);
            ((TextView)mcartLinearLayout.findViewById(R.id.total)).setVisibility(View.VISIBLE);
            ((ImageButton)mcartLinearLayout.findViewById(R.id.checkout)).setVisibility(View.VISIBLE);
            ((TextView)mcartLinearLayout.findViewById(R.id.cart)).setText(String.valueOf(mCart.getCountOfItems()));
            ((TextView)mcartLinearLayout.findViewById(R.id.total)).setText(String.valueOf(mCart.getTotalBill()));
        }
        else{
            mcartLinearLayout.setVisibility(View.GONE);
        }

        Map menuItemMap = DisplayHotelMenuActivity.getHotelMenuItemMap();
        List<HotelMenuItemSub> mHotelMenuItemSubList = (List<HotelMenuItemSub>) menuItemMap.get(menuName);

        ListView listView1 = (ListView)findViewById(R.id.hotelMenuSub);
        listView1.setAdapter(new HotelMenuItemSubAdapter(this,
                R.layout.activity_display_hotel_menu_sub_item, mHotelMenuItemSubList ));

        ((TextView) mcartLinearLayout.findViewById(R.id.cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDisplayCart = new Intent(getBaseContext(), DisplayCartActivity.class);
                startActivity(gotoDisplayCart);
            }
        });

    }



}
