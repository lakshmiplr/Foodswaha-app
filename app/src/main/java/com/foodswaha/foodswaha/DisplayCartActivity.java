package com.foodswaha.foodswaha;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DisplayCartActivity extends AppCompatActivity {

    Cart mCart = AppInitializerActivity.getCartInstance();
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cart);
        from = getIntent().getStringExtra("from");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        dispalycart();
    }
    private void dispalycart(){

        ((TextView)findViewById(R.id.cart_show)).setText(String.valueOf(mCart.getCountOfItems()));
        ((TextView)findViewById(R.id.cart_show_total)).setText(String.valueOf(mCart.getTotalBill()));

        List<HotelMenuItemSub> mCartItemList = (List<HotelMenuItemSub>) mCart.getFoodItems();

        ListView listView1 = (ListView)findViewById(R.id.cart_items);
        listView1.setAdapter(new CartItemAdapter(DisplayCartActivity.this,
                R.layout.activity_display_cart_items, mCartItemList));
    }

}
