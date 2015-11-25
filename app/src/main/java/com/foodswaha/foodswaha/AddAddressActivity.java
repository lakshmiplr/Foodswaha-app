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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddAddressActivity extends AppCompatActivity {
    private static final String POST_ADDRESS_DETAILS_CREATE_URL = "http://104.199.135.27:8080/address/create";
    private static final String POST_ADDRESS_DETAILS_UPDATE_URL = "http://104.199.135.27:8080/address/update";
    private JSONObject mJsonResponse;
    private static final String TAG = "AddaddressActivity";
    Cart cartInstance = Cart.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        final EditText flatNumber = (EditText) findViewById(R.id.flatNumber);
        final EditText streetName = (EditText) findViewById(R.id.streetName);
        final EditText area = (EditText) findViewById(R.id.area);
        final EditText city = (EditText) findViewById(R.id.city);
        final EditText landMark = (EditText) findViewById(R.id.landmark);
        if(getIntent().getStringExtra("edit")!=null){
            flatNumber.setText(getIntent().getStringExtra("flatNumber"));
            streetName.setText(getIntent().getStringExtra("streetDetails"));
            area.setText(getIntent().getStringExtra("area"));
            city.setText(getIntent().getStringExtra("city"));
            landMark.setText(getIntent().getStringExtra("landMark"));

        }else{
            area.setText(DisplayHotelsActivity.getAREA());
            city.setText(DisplayHotelsActivity.getCITY());
        }


        final EditText mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        mobileNumber.setText(LoginActivity.getMobileNumber());

        String dfeeString = HotelsFragment.getHolder().hotelDFee.getText().toString().replace(":", "");
        final int dfeeInt = Integer.parseInt(dfeeString.replace("Rs", "").trim());
        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));

        final View payment = findViewById(R.id.checkout);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correctValues = true;
                if(flatNumber.getText().toString().length()<1){
                    flatNumber.setError("flatnumber is needed.");
                    correctValues = false;
                }
                if(streetName.getText().toString().length()<3){
                    streetName.setError("streetdetails needed");
                    correctValues = false;
                }
                if(correctValues){
                    if(getIntent().getStringExtra("edit")!=null){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();
                    JSONArray addressesJSONArray = addressObject.optJSONArray("addresses");

                    try {
                        JSONObject address = new JSONObject("{\"flatNumber\":\""+flatNumber.getText().toString()+"\"," +
                                "\"streetDetails\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");
                        addressesJSONArray.put(Integer.parseInt(getIntent().getStringExtra("id")),address);
                        addressObject.put("mobile",mobileNumber.getText());
                        LoginActivity.setAddressJSONObject(addressObject);
                        sendUpdateAddressRequestToServer(addressObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else if(getIntent().getStringExtra("type").equals("new")){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();
                    JSONArray addressesJSONArray = addressObject.optJSONArray("addresses");

                    try {
                        JSONObject address = new JSONObject("{\"flatNumber\":\""+flatNumber.getText().toString()+"\"," +
                                "\"streetDetails\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");

                        addressesJSONArray.put(address);
                        addressObject.put("mobile",mobileNumber.getText());
                        LoginActivity.setAddressJSONObject(addressObject);
                        sendUpdateAddressRequestToServer(addressObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else if(getIntent().getStringExtra("type").equals("first")){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();

                    try {
                        JSONObject address = new JSONObject("{\"flatNumber\":\""+flatNumber.getText().toString()+"\"," +
                                "\"streetDetails\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");
                        JSONArray addressJSONArray = new JSONArray();
                        addressJSONArray.put(address);
                        addressObject.put("addresses",addressJSONArray);
                        addressObject.put("email",LoginActivity.getEmail());
                        addressObject.put("mobile",mobileNumber.getText());
                        LoginActivity.setAddressJSONObject(addressObject);
                        sendCreateAddressRequestToServer(addressObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                    Intent gotoPaymentIntent = new Intent(AddAddressActivity.this, PaymentActivity.class);
                    startActivity(gotoPaymentIntent);
                }
            }
        });
    }

    private void sendCreateAddressRequestToServer(JSONObject addressObject){
        JsonObjectRequest  jsonObjectPost = new JsonObjectRequest(Request.Method.PUT, POST_ADDRESS_DETAILS_CREATE_URL,addressObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                            }
                            catch (Exception e) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectPost);
    }

    private void sendUpdateAddressRequestToServer(JSONObject addressObject){
        JsonObjectRequest  jsonObjectPost = new JsonObjectRequest(Request.Method.POST, POST_ADDRESS_DETAILS_UPDATE_URL,addressObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                        }
                        catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectPost);
    }


    @Override
    public void onBackPressed() {
        if(LoginActivity.getAddressCount()==0){
            Intent chooseDeliveryTypeActivity = new Intent(AddAddressActivity.this, ChooseDeliveryTypeActivity.class);
            startActivity(chooseDeliveryTypeActivity);
        }
    }

}

