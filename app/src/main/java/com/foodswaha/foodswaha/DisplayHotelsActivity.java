package com.foodswaha.foodswaha;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayHotelsActivity extends AppCompatActivity {

    private static final String TAG = "DisplayHotels";

    private static HotelItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, " onCreate method started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            switch(i) {
                case 0 :
                    tabLayout.getTabAt(i).setIcon(R.drawable.hotels_select_blue);
                    break;

                case 1 :
                    tabLayout.getTabAt(i).setIcon(R.drawable.orders1_unselect);
                    break;

                case 2 :
                    tabLayout.getTabAt(i).setIcon(R.drawable.kart_unselect);
                    break;

                case 3 :
                    tabLayout.getTabAt(i).setIcon(R.drawable.deals_unselect);
                    break;

                case 4 :
                    tabLayout.getTabAt(i).setIcon(R.drawable.menu1_unselect);
                    break;

                default :
                    break;
            }

        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int i =tab.getPosition();
                switch(i) {
                    case 0 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.hotels_select_blue);
                        break;

                    case 1 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.orders1_select_blue);
                        break;

                    case 2 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.kart_select_blue);
                        break;

                    case 3 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.deals_select_blue);
                        break;

                    case 4 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.menu1_select_blue);
                        break;

                    default :
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int i =tab.getPosition();
                switch(i) {
                    case 0 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.hotels_unselect);
                        break;

                    case 1 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.orders1_unselect);
                        break;

                    case 2 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.kart_unselect);
                        break;

                    case 3 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.deals_unselect);
                        break;

                    case 4 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.menu1_unselect);
                        break;

                    default :
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        LocationFinderUtil lfu = LocationFinderUtil.getInstance();
        Log.e(TAG," trying to retrive LocationFinderUtil object and got response as "+lfu);
        if(lfu!=null){
            JSONObject jsonResponseData = lfu.getJsonResponseData();
            displayHotels(jsonResponseData);
        }
        /*try {
            displayHotels(new JSONObject("{\"area\" : \"btm 2nd stage\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


    /*protected void CheckLocationRuntimePermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            DisplayHotelsActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }*/

    protected void displayHotels(JSONObject response){
        Log.e(TAG," displayHotels method started.");
        TextView areaText = (TextView)findViewById(R.id.areaText);
        try {
            String area = response.getString("area");
            areaText.setText(area);

            Log.e(TAG, " received area of user from server as "+area);

            JSONArray hotelsJSONArray = response.getJSONArray("hotels");

            Log.e(TAG, " received hotelsJSONArray from server as "+hotelsJSONArray);

            List hotelList = new ArrayList<HotelItem>();
            JSONObject hotelItem;
            for(int i = 0; i < hotelsJSONArray.length(); i++){
                hotelItem = hotelsJSONArray.getJSONObject(i);
                hotelList.add(
                        new HotelItem(
                                hotelItem.getString("id"),
                                hotelItem.getString("name"),
                                hotelItem.getString("area"),
                                hotelItem.getString("delivery time"),
                                hotelItem.getString("delivery fees"),
                                hotelItem.getString("min order"),
                                hotelItem.getString("on time"),
                                hotelItem.getString("rating"),
                                hotelItem.getString("timings"),
                                hotelItem.getString("image url"),
                                hotelItem.getString("food types")
                        )
                );
            }

            adapter = new HotelItemAdapter(this,
                    R.layout.hotel_item1, hotelList);

            Log.e(TAG, " HotelItemAdapter created using hoteldata from server as "+adapter);

        } catch (JSONException e) {
        }
    }
    public static HotelItemAdapter getHotelItemAdapter(){
        Log.e(TAG, " getHotelItemAdapter method started.");
        return adapter;
    }

}
