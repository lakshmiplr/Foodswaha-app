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
    private static final String POST_ADDRESS_DETAILS_URL = "http://104.199.135.27:8080/address";
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
            streetName.setText(getIntent().getStringExtra("streetName"));
            area.setText(getIntent().getStringExtra("area"));
            city.setText(getIntent().getStringExtra("city"));
            landMark.setText(getIntent().getStringExtra("landMark"));

        }else{
            area.setText(DisplayHotelsActivity.getAREA());
            city.setText(DisplayHotelsActivity.getCITY());
        }


        EditText mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        mobileNumber.setText(LoginActivity.getMobileNumber());

        String dfeeString = HotelsFragment.getHolder().hotelDFee.getText().toString().replace(":", "");
        final int dfeeInt = Integer.parseInt(dfeeString.replace("Rs", "").trim());
        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));

        final View payment = findViewById(R.id.checkout);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("edit")!=null){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();
                    JSONArray addressesJSONArray = addressObject.optJSONArray("addresses");

                    try {
                        JSONObject address = new JSONObject("{\"flat No\":\""+flatNumber.getText().toString()+"\"," +
                                "\"address\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");
                        addressesJSONArray.put(Integer.parseInt(getIntent().getStringExtra("id")),address);
                        LoginActivity.setAddressJSONObject(addressObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                }


                Intent gotoPaymentIntent = new Intent(AddAddressActivity.this, PaymentActivity.class);
                startActivity(gotoPaymentIntent);

            }
        });
    }

    private void sendAddressToServer(){
        EditText doorNumber = (EditText) findViewById(R.id.flatNumber);
        EditText streetNumber = (EditText) findViewById(R.id.streetName);
        TextView area = (TextView) findViewById(R.id.area);
        TextView city = (TextView) findViewById(R.id.city);
        EditText landmark = (EditText) findViewById(R.id.landmark);
        EditText mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        if( mobileNumber.getText().toString().length() == 10 ) {
            mobileNumber.setError("mobilenumber should be 10 digits");
        }
        String email = LoginActivity.getEmail();
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, POST_ADDRESS_DETAILS_URL,new JSONObject("{\"doorNumber\":\""+doorNumber.getText().toString()+"\"," +
                    "\"streetNumber\":\""+streetNumber.getText().toString()+"\"," +
                    "\"area\":\""+area.getText().toString()+"\"," +
                    "\"city\":\""+city.getText().toString()+"\"," +
                    "\"landmark\":\""+landmark.getText().toString()+"\"," +
                    "\"mobileNumber\":\""+mobileNumber.getText().toString()+"\"," +
                    "\"email\":\""+email+"\"," +
                    "}"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                mJsonResponse = response;
                                Log.e(TAG, "JsonResponse received from server.response is " + mJsonResponse);
                                Intent paymentIntent = new Intent(AddAddressActivity.this, PaymentActivity.class);
                                startActivity(paymentIntent);
                            }
                            catch (Exception e) {
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



}

