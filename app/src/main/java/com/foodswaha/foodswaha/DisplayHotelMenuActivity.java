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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayHotelMenuActivity extends AppCompatActivity {

    private static final String TAG = "DisplayHotelMenu";
    private static HotelMenuItemAdapter.HotelMenuItemHolder  holder;
    private static Map<String,List<HotelMenuItemSub>> hotelMenuItemMap;

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

        displayHotelMenu();
        final TextView areaText = (TextView)findViewById(R.id.hotelNameText);
        areaText.setText(HotelsFragment.getHolder().hotelName.getText());
    }
    private void displayHotelMenu(){
        JSONObject mHotelMenu = HotelsFragment.getHotelMenuData();
        try {
            JSONArray hotelMenuJSONArray = mHotelMenu.getJSONArray("categories");
            Log.e(TAG, " received hotelMenuJSONArray from server as " + hotelMenuJSONArray);

            hotelMenuItemMap = new HashMap<String,List<HotelMenuItemSub>>();
            String hotelMenuItem;
            List hotelMenuItemSubList;
            List mHotelMenuItemList = new ArrayList<HotelMenuItem>();
            HotelMenuItemSub mHotelMenuItemSub;
            for(int i = 0; i < hotelMenuJSONArray.length(); i++){

                hotelMenuItemSubList = new ArrayList<HotelMenuItemSub>();
                hotelMenuItem = hotelMenuJSONArray.optString(i);
                JSONArray hotelMenuItemSubJSONArray = mHotelMenu.optJSONArray(hotelMenuItem);

                if(hotelMenuItemSubJSONArray!=null){
                    for(int j = 0; j < hotelMenuItemSubJSONArray.length(); j++){

                        JSONObject mHotelMenuItemSubJSONObject = hotelMenuItemSubJSONArray.optJSONObject(j);
                        mHotelMenuItemSub = new HotelMenuItemSub(
                                mHotelMenuItemSubJSONObject.optString("itemname"),
                                mHotelMenuItemSubJSONObject.optString("cost"),
                                mHotelMenuItemSubJSONObject.optString("itemdesc")
                        );
                        hotelMenuItemSubList.add(mHotelMenuItemSub);
                    }

                    mHotelMenuItemList.add(new HotelMenuItem(hotelMenuItem));
                    hotelMenuItemMap.put(hotelMenuItem, hotelMenuItemSubList);
                }

            }

            ListView listView1 = (ListView)findViewById(R.id.hotelMenu);
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


        } catch (JSONException e) {
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
}
