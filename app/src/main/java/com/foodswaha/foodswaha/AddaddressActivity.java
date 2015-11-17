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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AddaddressActivity extends AppCompatActivity {
    private static final String POST_ADDRESS_DETAILS_URL = "http://104.155.202.28:8080/address";
    private JSONObject mJsonResponse;
    private static final String TAG = "AddaddressActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        EditText area = (EditText) findViewById(R.id.area);
        EditText city = (EditText) findViewById(R.id.city);
        area.setText(DisplayHotelsActivity.getAREA());
        city.setText(DisplayHotelsActivity.getCITY());

        final View payment = findViewById(R.id.checkout);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAddressToServer();
            }
        });
    }

    private void sendAddressToServer(){
        EditText doorNumber = (EditText) findViewById(R.id.doorNumber);
        EditText streetNumber = (EditText) findViewById(R.id.streetNumber);
        EditText area = (EditText) findViewById(R.id.area);
        EditText city = (EditText) findViewById(R.id.city);
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
                                Intent paymentIntent = new Intent(AddaddressActivity.this, PaymentActivity.class);
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

