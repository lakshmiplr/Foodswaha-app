package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChooseDeliveryTypeActivity extends AppCompatActivity
         implements GoogleApiClient.OnConnectionFailedListener{

    Cart cartInstance = Cart.getInstance();
    private static String cdt ="delivery";
    private static JSONObject addressJSONObject = LoginActivity.getAddressJSONObject();
    private static String mobileNumber= LoginActivity.getMobileNumber();
    private static int ADDRESS_COUNT= LoginActivity.getAddressCount();
    private static final String GET_USER_ADDRESS_LIST_URL = LoginActivity.getGetUserAddressListUrl();
    private static String email=LoginActivity.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_delivery_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        TextView dTime = (TextView) findViewById(R.id.dtime);
        TextView dFee = (TextView) findViewById(R.id.dfee);

        dTime.setText(HotelsFragment.getHolder().hotelDTime.getText().toString().replace(":", ""));
        String dfeeString = HotelsFragment.getHolder().hotelDFee.getText().toString().replace(":", "");
        final int dfeeInt = Integer.parseInt(dfeeString.replace("Rs", "").trim());
        dFee.setText(dfeeString);
        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                if (radioButton.getText().equals("delivery")) {
                    total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));
                    cdt = "delivery";
                } else if (radioButton.getText().equals("pickup")) {
                    total.setText(String.valueOf(cartInstance.getTotalBill()));
                    cdt = "pickup";
                }
            }
        });

        final View gotoLoginActiity = findViewById(R.id.checkout);
        gotoLoginActiity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
                if (opr.isDone()) {
                    GoogleSignInResult result = opr.get();
                    GoogleSignInAccount acct = result.getSignInAccount();
                    email = acct.getEmail();
                    if("delivery".equals(ChooseDeliveryTypeActivity.getCdt())){
                        getUserAddressList();
                    }else if("pickup".equals(ChooseDeliveryTypeActivity.getCdt())){
                        Intent gotoHotelAddressActivity = new Intent(ChooseDeliveryTypeActivity.this,DispalyHotelAddressActivity.class);
                        gotoHotelAddressActivity.putExtra("email",acct.getEmail());
                        startActivity(gotoHotelAddressActivity);
                    }

                }else{
                    Intent loginIntent = new Intent(ChooseDeliveryTypeActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });

        cdt="delivery";

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
            if(addressJSONObject.optString("email")==null){
                Intent gotoAddFirstAddressIntent = new Intent(ChooseDeliveryTypeActivity.this, AddAddressActivity.class);
                gotoAddFirstAddressIntent.putExtra("type", "first");
                startActivity(gotoAddFirstAddressIntent);
            }
            else if(ADDRESS_COUNT==0&&addressJSONObject.optString("email")!=null){
                Intent gotoAddFirstAddressIntent = new Intent(ChooseDeliveryTypeActivity.this, AddAddressActivity.class);
                gotoAddFirstAddressIntent.putExtra("type","new");
                startActivity(gotoAddFirstAddressIntent);
            }else{
                Intent gotoAddressActivity = new Intent(ChooseDeliveryTypeActivity.this,DisplayAddressActivity.class);
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
                                        LoginActivity.setAddressJSONObject(response);
                                        LoginActivity.setEmail(email);
                                        addressJSONObject = response;
                                    }
                                    JSONArray addressesJSONArray = addressJSONObject.optJSONArray("addresses");
                                    if(addressesJSONArray!=null) {
                                        mobileNumber = response.optString("mobile");
                                        ADDRESS_COUNT = addressesJSONArray.length();
                                        LoginActivity.setMobileNumber(mobileNumber);
                                        LoginActivity.setAddressCount(ADDRESS_COUNT);
                                    }
                                    if(response.optString("email")==null){
                                        Intent gotoAddFirstAddressIntent = new Intent(ChooseDeliveryTypeActivity.this, AddAddressActivity.class);
                                        gotoAddFirstAddressIntent.putExtra("type", "first");
                                        startActivity(gotoAddFirstAddressIntent);
                                    }
                                    else if(ADDRESS_COUNT==0&&response.optString("email")!=null){
                                        Intent gotoAddFirstAddressIntent = new Intent(ChooseDeliveryTypeActivity.this, AddAddressActivity.class);
                                        gotoAddFirstAddressIntent.putExtra("type","new");
                                        startActivity(gotoAddFirstAddressIntent);
                                    }

                                    else{
                                        Intent gotoAddressActivity = new Intent(ChooseDeliveryTypeActivity.this,DisplayAddressActivity.class);
                                        startActivity(gotoAddressActivity);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
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

    public static String getCdt() {
        return cdt;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        addressJSONObject = LoginActivity.getAddressJSONObject();
        mobileNumber= LoginActivity.getMobileNumber();
        ADDRESS_COUNT= LoginActivity.getAddressCount();
        email=LoginActivity.getEmail();
    }
}
