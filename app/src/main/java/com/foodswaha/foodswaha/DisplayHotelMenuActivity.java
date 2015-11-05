package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayHotelMenuActivity extends AppCompatActivity {

    private static final String TAG = "DisplayHotelMenu";
    private static HotelMenuItemAdapter.HotelMenuItemHolder holder;
    private static Map<String, List<HotelMenuItemSub>> hotelMenuItemMap;
    private static List mHotelMenuItemList;
    Cart mCart = AppInitializerActivity.getCartInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotel_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final TextView areaText = (TextView) findViewById(R.id.hotelNameText);
        areaText.setText(HotelsFragment.getHolder().hotelName.getText());

        final View mcartLinearLayout = findViewById(R.id.cart_linear_menu);
        ((TextView) mcartLinearLayout.findViewById(R.id.cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDisplayCart = new Intent(DisplayHotelMenuActivity.this, DisplayCartActivity.class);
                gotoDisplayCart.putExtra("from", "menu");
                startActivity(gotoDisplayCart);
            }
        });

        showCartIfAvailable();

        displayHotelMenu();
    }

    private void displayHotelMenu() {
        JSONObject mHotelMenu = HotelsFragment.getHotelMenuData();
        try {
            JSONArray hotelMenuJSONArray = mHotelMenu.optJSONArray("categories");
            Log.e(TAG, " received hotelMenuJSONArray from server as " + hotelMenuJSONArray);

            if (hotelMenuJSONArray != null) {

                if (hotelMenuItemMap == null) {
                    hotelMenuItemMap = new HashMap<String, List<HotelMenuItemSub>>();
                    mHotelMenuItemList = new ArrayList<HotelMenuItem>();
                    String hotelMenuItem;
                    List hotelMenuItemSubList;

                    HotelMenuItemSub mHotelMenuItemSub;
                    for (int i = 0; i < hotelMenuJSONArray.length(); i++) {

                        hotelMenuItemSubList = new ArrayList<HotelMenuItemSub>();
                        hotelMenuItem = hotelMenuJSONArray.optString(i);
                        JSONArray hotelMenuItemSubJSONArray = mHotelMenu.optJSONArray(hotelMenuItem);

                        if (hotelMenuItemSubJSONArray != null) {
                            for (int j = 0; j < hotelMenuItemSubJSONArray.length(); j++) {

                                JSONObject mHotelMenuItemSubJSONObject = hotelMenuItemSubJSONArray.optJSONObject(j);
                                mHotelMenuItemSub = new HotelMenuItemSub(
                                        mHotelMenuItemSubJSONObject.optString("itemname"),
                                        mHotelMenuItemSubJSONObject.optString("cost"), 0,
                                        hotelMenuItem, HotelsFragment.getHolder().hotelName.getText().toString()

                                );
                                hotelMenuItemSubList.add(mHotelMenuItemSub);
                            }

                            mHotelMenuItemList.add(new HotelMenuItem(hotelMenuItem));
                            hotelMenuItemMap.put(hotelMenuItem, hotelMenuItemSubList);
                        }

                    }
                }
                if (mHotelMenuItemList != null) {
                    ListView listView1 = (ListView) findViewById(R.id.hotelMenu);
                    listView1.setAdapter(new HotelMenuItemAdapter(this,
                            R.layout.activity_display_hotel_menu_item, mHotelMenuItemList));
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            holder = (HotelMenuItemAdapter.HotelMenuItemHolder) view.getTag();
                            gotoDisplayHotelMenuSubActivity();

                        }
                    });
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HotelMenuItemAdapter.HotelMenuItemHolder getHolder() {
        return holder;
    }

    public static Map getHotelMenuItemMap() {
        return hotelMenuItemMap;
    }

    public void gotoDisplayHotelMenuSubActivity() {
        Log.e(TAG, " gotoDisplayHotelMenuActivity method started.");
        Intent displayHotelMenuIntent = new Intent(getBaseContext(), DisplayHotelMenuItemSub.class);
        startActivity(displayHotelMenuIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCartIfAvailable();
    }

    private void showCartIfAvailable() {
        final View mcartLinearLayout = findViewById(R.id.cart_linear_menu);
        if (mCart.getCountOfItems() > 0) {

             mcartLinearLayout.setVisibility(View.VISIBLE);
            ((TextView) mcartLinearLayout.findViewById(R.id.cart)).setText(String.valueOf(mCart.getCountOfItems()));
            ((TextView) mcartLinearLayout.findViewById(R.id.total)).setText(String.valueOf(mCart.getTotalBill()));
        } else {
            mcartLinearLayout.setVisibility(View.GONE);
        }
    }

}
