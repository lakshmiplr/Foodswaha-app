package com.foodswaha.foodswaha;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayHotelsActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    private static final String TAG = "DisplayHotels";
    private static String AREA ="";
    private static String CITY ="";

    private static HotelAdapter adapter;
    private static boolean inputSearchVisibile = false;
    private static String hotelDataJSONString;
    private static GoogleApiClient mGoogleApiClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, " onCreate method started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        final ImageButton search = (ImageButton) findViewById(R.id.searchButton);
        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        final Button btnClear = (Button)findViewById(R.id.btn_clear);
        final TextView title = (TextView)findViewById(R.id.title);
        final ImageButton edit = (ImageButton)findViewById(R.id.editButton);
        final Button voice = (Button) findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                inputSearch.setVisibility(View.VISIBLE);
                voice.setVisibility(View.VISIBLE);
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
                voice.setVisibility(View.VISIBLE);
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, " on onTextChanged method called with query" + cs);
                if (!inputSearch.getText().toString().equals("")) {
                    btnClear.setVisibility(View.VISIBLE);
                    voice.setVisibility(View.GONE);
                } else {
                    btnClear.setVisibility(View.GONE);
                    voice.setVisibility(View.VISIBLE);
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


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEditLocationActivity();
            }
        });

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

                    tabLayout.getTabAt(i).setIcon(R.drawable.deals_unselect);
                    break;

                case 3 :
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
                int i = tab.getPosition();
                switch (i) {
                    case 0:
                        tabLayout.getTabAt(i).setIcon(R.drawable.hotels_select_blue);
                        if (inputSearchVisibile) {
                            edit.setVisibility(View.GONE);
                            search.setVisibility(View.GONE);
                            title.setVisibility(View.GONE);
                            inputSearch.setVisibility(View.VISIBLE);
                            if (!inputSearch.getText().toString().equals("")) {
                                btnClear.setVisibility(View.VISIBLE);
                                voice.setVisibility(View.GONE);
                            }
                            else{
                                voice.setVisibility(View.VISIBLE);
                            }
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        } else {
                            title.setText(AREA);
                            edit.setVisibility(View.VISIBLE);
                            search.setVisibility(View.VISIBLE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            voice.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                        }

                        break;

                    case 1:
                        tabLayout.getTabAt(i).setIcon(R.drawable.orders1_select_blue);
                        title.setText("Orders");
                        title.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if (inputSearch.getVisibility() == View.VISIBLE) {
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            voice.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
                        break;

                    case 2:
                        tabLayout.getTabAt(i).setIcon(R.drawable.deals_select_blue);
                        title.setText("Deals");
                        title.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if (inputSearch.getVisibility() == View.VISIBLE) {
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            voice.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
                        break;

                    case 3:
                        tabLayout.getTabAt(i).setIcon(R.drawable.menu1_select_blue);
                        title.setText("More");
                        title.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        if (inputSearch.getVisibility() == View.VISIBLE) {
                            inputSearch.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            voice.setVisibility(View.GONE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            imm.hideSoftInputFromWindow(tabLayout.getApplicationWindowToken(), 0);
                        }
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                switch (i) {
                    case 0:
                        tabLayout.getTabAt(i).setIcon(R.drawable.hotels_unselect);
                        if (inputSearch.getVisibility() == View.VISIBLE) {
                            inputSearchVisibile = true;
                        } else {
                            inputSearchVisibile = false;
                        }
                        break;

                    case 1:
                        tabLayout.getTabAt(i).setIcon(R.drawable.orders1_unselect);
                        break;

                    case 2:
                        tabLayout.getTabAt(i).setIcon(R.drawable.deals_unselect);
                        break;

                    case 3:
                        tabLayout.getTabAt(i).setIcon(R.drawable.menu1_unselect);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        hotelDataJSONString = getIntent().getStringExtra("hotelData");
        JSONObject jsonResponseData = null;
        try {
            jsonResponseData = new JSONObject(hotelDataJSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        displayHotels(jsonResponseData);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void gotoEditLocationActivity() {
        Intent editLocationActivity = new Intent(this, EditLocationActivity.class);
        startActivity(editLocationActivity);
    }

    private void displayHotels(JSONObject response){
        Log.e(TAG, " displayHotels method started.");
        try {
            String area = response.optString("area");
            TextView title = (TextView)findViewById(R.id.title);
            title.setText(area);
            AREA = area;
            CITY = response.optString("city");

            Log.e(TAG, " received area of user from server as "+area);
            JSONArray hotelsJSONArray = response.optJSONArray("hotels");
            Log.e(TAG, " received hotelsJSONArray from server as "+hotelsJSONArray);
            List hotelList = new ArrayList<Hotel>();
            JSONObject hotel;
            if (hotelsJSONArray!=null) {
                for (int i = 0; i < hotelsJSONArray.length(); i++) {
                    hotel = hotelsJSONArray.getJSONObject(i);
                    hotelList.add(
                            new Hotel(
                                    hotel.getString("id"),
                                    hotel.getString("name"),
                                    hotel.getString("area"),
                                    hotel.getString("delivery time"),
                                    hotel.getString("delivery fees"),
                                    hotel.getString("min order"),
                                    hotel.getString("on time"),
                                    hotel.getString("rating"),
                                    hotel.getString("timings"),
                                    hotel.getString("image url"),
                                    hotel.getString("food types")
                            )
                    );
                }
            }

            adapter = new HotelAdapter(this,
                    R.layout.hotel_item, hotelList);

            Log.e(TAG, " HotelItemAdapter created using hoteldata from server as "+adapter);

        } catch (JSONException e) {
        }
    }
    public static HotelAdapter getHotelAdapter(){
        Log.e(TAG, " getHotelItemAdapter method started.");
        return adapter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                TextView title = (TextView)findViewById(R.id.title);
                ImageButton edit = (ImageButton)findViewById(R.id.editButton);
                ImageButton search = (ImageButton) findViewById(R.id.searchButton);
                EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
                Button voice = (Button) findViewById(R.id.voice);
                title.setText(AREA);
                title.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                inputSearch.setVisibility(View.GONE);
                inputSearch.setText("");
                voice.setVisibility(View.GONE);
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

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // here the string converted from your voice
                    String converted_text = result.get(0);
                    EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
                    inputSearch.setText(converted_text);
                }
                break;
            }
            case OrdersFragment.RC_SIGN_IN: {
                OrdersFragment fragment = (OrdersFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.orders);
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    public static String getHotelDataJSONString() {
        return hotelDataJSONString;
    }

    public static GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static String getAREA() {
        return AREA;
    }

    public static String getCITY() {
        return CITY;
    }
}
