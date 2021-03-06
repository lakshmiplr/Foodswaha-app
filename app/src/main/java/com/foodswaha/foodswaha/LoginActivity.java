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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static String email="";
    private static String mobileNumber="";
    private static String deliveryAddress="";
    private static JSONObject addressJSONObject;
    private static int ADDRESS_COUNT=0;
    private static final String GET_USER_ADDRESS_LIST_URL = "http://104.199.135.27:8080/address";

    private GoogleApiClient mGoogleApiClient;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        findViewById(R.id.signIn).setOnClickListener(this);
        mGoogleApiClient = DisplayHotelsActivity.getGoogleApiClient();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            findViewById(R.id.signIn).setVisibility(View.GONE);
            email = acct.getEmail();
            if("delivery".equals(ChooseDeliveryTypeActivity.getCdt())){
                getUserAddressList();
            }else if("pickup".equals(ChooseDeliveryTypeActivity.getCdt())){
                Intent gotoHotelAddressActivity = new Intent(this,DispalyHotelAddressActivity.class);
                gotoHotelAddressActivity.putExtra("email",acct.getEmail());
                startActivity(gotoHotelAddressActivity);
            }

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn:
                signIn();
                break;

        }
    }

    private void getUserAddressList() {
        if(addressJSONObject!=null){
            JSONArray addressesJSONArray = addressJSONObject.optJSONArray("addresses");
            mobileNumber = addressJSONObject.optString("mobile");
            if(addressesJSONArray==null){
                ADDRESS_COUNT =0;
            }else{
                ADDRESS_COUNT = addressesJSONArray.length();
            }
            if(ADDRESS_COUNT==0){
                finish();
                Intent gotoAddFirstAddressIntent = new Intent(LoginActivity.this, AddAddressActivity.class);
                gotoAddFirstAddressIntent.putExtra("type", "first");
                startActivity(gotoAddFirstAddressIntent);
            }else{
                Intent gotoAddressActivity = new Intent(LoginActivity.this,DisplayAddressActivity.class);
                startActivity(gotoAddressActivity);
            }
        }else{
            JsonObjectRequest jsonObjectRequest = null;
            try {
                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GET_USER_ADDRESS_LIST_URL, new JSONObject("{\"email\":\"" + email + "\"}"),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(addressJSONObject==null){
                                        addressJSONObject = response;
                                    }
                                    JSONArray addressesJSONArray = addressJSONObject.optJSONArray("addresses");
                                    if(addressesJSONArray!=null) {
                                        mobileNumber = response.optString("mobile");
                                        ADDRESS_COUNT = addressesJSONArray.length();
                                    }
                                    if(ADDRESS_COUNT==0){
                                        finish();
                                        Intent gotoAddFirstAddressIntent = new Intent(LoginActivity.this, AddAddressActivity.class);
                                        gotoAddFirstAddressIntent.putExtra("type","first");
                                        startActivity(gotoAddFirstAddressIntent);
                                    }else{
                                        Intent gotoAddressActivity = new Intent(LoginActivity.this,DisplayAddressActivity.class);
                                        startActivity(gotoAddressActivity);
                                    }
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
    }

    public static JSONObject getAddressJSONObject() {
        return addressJSONObject;
    }

    public static void setAddressJSONObject(JSONObject addressJSONObject) {
        LoginActivity.addressJSONObject = addressJSONObject;
    }

    public static String getMobileNumber() {
        return mobileNumber;
    }

    public static String getEmail() {
        return email;
    }

    public static int getAddressCount() {
        return ADDRESS_COUNT;
    }

    public static String getGetUserAddressListUrl() {
        return GET_USER_ADDRESS_LIST_URL;
    }

    public static void setEmail(String email) {
        LoginActivity.email = email;
    }

    public static void setMobileNumber(String mobileNumber) {
        LoginActivity.mobileNumber = mobileNumber;
    }

    public static void setAddressCount(int addressCount) {
        ADDRESS_COUNT = addressCount;
    }

    public static String getDeliveryAddress() {
        return deliveryAddress;
    }

    public static void setDeliveryAddress(String deliveryAddress) {
        LoginActivity.deliveryAddress = deliveryAddress;
    }
}