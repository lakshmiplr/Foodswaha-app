package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayAddressActivity extends AppCompatActivity {

    private static final String GET_USER_ADDRESS_LIST_URL = "http://104.199.135.27:8080/address";
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        email = getIntent().getStringExtra("email");
        String cdt = ChooseDeliveryTypeActivity.getCdt();
        //TextView emailTextView = (TextView)findViewById(R.id.emptyCart);
        //emailTextView.setText(email+"----"+cdt);
        getUserAddressList();
        final View addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(DisplayAddressActivity.this, AddaddressActivity.class);
                startActivity(loginIntent);
            }
        });

    }

    private void getUserAddressList() {
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GET_USER_ADDRESS_LIST_URL, new JSONObject("{\"email\":\"" + email + "\"}"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                buildAddressAdapter(response);
                            } catch (Exception e) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    private void buildAddressAdapter(JSONObject response) {
        try{
        JSONArray addressesJSONArray = response.getJSONArray("addresses");
        List addressList = new ArrayList<Address>();
        JSONObject address;
        for(int i = 0; i < addressesJSONArray.length(); i++){
            address = addressesJSONArray.getJSONObject(i);
            addressList.add(
                    new Address(
                            address.getString("flat No"),
                            address.getString("address"),
                            address.getString("area"),
                            address.getString("city"),
                            address.getString("landmark")
                    )
            );
        }

            ListView addressListView = (ListView) findViewById(R.id.addressList);
            EditText mobile =(EditText)findViewById(R.id.mobile);
            mobile.setText(response.optString("mobile"));
            addressListView.setAdapter(new AddressAdapter(this,
                    R.layout.activity_display_address_item, addressList));

    } catch (JSONException e) {
    }
    }
}