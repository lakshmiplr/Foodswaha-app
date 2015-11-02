package com.foodswaha.foodswaha;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayHotelsActivity extends AppCompatActivity {

    private static final String TAG = "DisplayHotels";
    private static String AREA ="";

    private static HotelItemAdapter adapter;
    private static boolean isInitialInputSearchVisibile=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, " onCreate method started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotels);
        final ImageButton search = (ImageButton) findViewById(R.id.searchButton3);
        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        final Button btnClear = (Button)findViewById(R.id.btn_clear);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView areaText = (TextView) findViewById(R.id.areaText);
                areaText.setVisibility(View.GONE);
                inputSearch.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                final Drawable upArrow = ContextCompat.getDrawable(DisplayHotelsActivity.this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);

                inputSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                btnClear.setVisibility(View.GONE);
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, " on input search method " + cs);
                if (!inputSearch.getText().toString().equals("")) {
                    btnClear.setVisibility(View.VISIBLE);
                }
                else
                {
                    btnClear.setVisibility(View.GONE);
                }
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);



        final TextView areaText = (TextView)findViewById(R.id.areaText);
        final ImageButton edit = (ImageButton)findViewById(R.id.editButton);


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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int i =tab.getPosition();
                switch(i) {
                    case 0 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.hotels_select_blue);
                        if(isInitialInputSearchVisibile){
                            edit.setVisibility(View.GONE);
                            search.setVisibility(View.GONE);
                            areaText.setVisibility(View.GONE);
                            inputSearch.setVisibility(View.VISIBLE);
                            if (!inputSearch.getText().toString().equals("")) {
                                btnClear.setVisibility(View.VISIBLE);
                            }
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            //imm.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);
                        }
                        else{
                            areaText.setText(AREA);
                            edit.setImageResource(R.drawable.edit);
                            search.setImageResource(R.drawable.search_art);
                            edit.setVisibility(View.VISIBLE);
                            search.setVisibility(View.VISIBLE);
                            areaText.setVisibility(View.VISIBLE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }

                        break;

                    case 1 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.orders1_select_blue);
                        areaText.setVisibility(View.VISIBLE);
                        areaText.setText("Orders");
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if(inputSearch.getVisibility() == View.VISIBLE){
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
                        break;

                    case 2 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.kart_select_blue);
                        areaText.setText("Kart");
                        areaText.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if(inputSearch.getVisibility() == View.VISIBLE){
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
                        break;

                    case 3 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.deals_select_blue);
                        areaText.setText("Deals");
                        areaText.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if(inputSearch.getVisibility() == View.VISIBLE){
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
                        break;

                    case 4 :
                        tabLayout.getTabAt(i).setIcon(R.drawable.menu1_select_blue);
                        areaText.setText("More");
                        areaText.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if(inputSearch.getVisibility() == View.VISIBLE){
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
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
                        if(inputSearch.getVisibility() == View.VISIBLE){
                            isInitialInputSearchVisibile =true;
                        }
                        else{
                            isInitialInputSearchVisibile =false;
                        }
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
            AREA = area;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                final TextView areaText = (TextView)findViewById(R.id.areaText);
                final ImageButton edit = (ImageButton)findViewById(R.id.editButton);
                final ImageButton search = (ImageButton) findViewById(R.id.searchButton3);
                final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
                areaText.setText(AREA);
                areaText.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                inputSearch.setVisibility(View.GONE);
                inputSearch.setText("");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                adapter.getFilter().filter("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View view = this.getCurrentFocus();
                if(view!=null){
                    imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
