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
import java.util.Map;

public class DisplayHotelMenuItemSub extends AppCompatActivity {

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

        Map menuItemMap = DisplayHotelMenuActivity.getHotelMenuItemMap();
        List<HotelMenuItemSub> mHotelMenuItemSubList = (List<HotelMenuItemSub>) menuItemMap.get(menuName);

        ListView listView1 = (ListView)findViewById(R.id.hotelMenuSub);
        listView1.setAdapter(new HotelMenuItemSubAdapter(this,
                R.layout.activity_display_hotel_menu_sub_item,mHotelMenuItemSubList ));
    }


}
