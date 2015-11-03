package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppInitializerActivity extends AppCompatActivity {

    private static final String TAG = "AppInitializer";

    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 3;

    private static Cart mCart = new Cart();


    final LocationFinderUtil lfu = new LocationFinderUtil(this,this,this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG," onCreate method started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_initializer);

        final ProgressBar loading = (ProgressBar)findViewById(R.id.loading);

        if( InternetCheckUtil.isConnectivityAvailable(this) ){
            Log.e(TAG," Internet connection available.");
            loading.setVisibility(View.VISIBLE);
            getHotelsDataFromServer(lfu);
        }
        else{
            Log.e(TAG," Internet connection not available.");
            final TextView noInternet = (TextView)findViewById(R.id.noInternet);
            noInternet.setText("No Internet Connection");

            final ImageButton retry = (ImageButton) findViewById(R.id.retry);
            retry.setImageResource(R.drawable.retry);

            final Context context = this;
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( InternetCheckUtil.isConnectivityAvailable(context) ){
                        Log.e(TAG," Internet connection not available even after retry.");
                        noInternet.setText("");
                        retry.setImageDrawable(null);
                        loading.setVisibility(View.VISIBLE);
                        getHotelsDataFromServer(lfu);
                    }
                }
            });
        }

    }
    private void getHotelsDataFromServer(LocationFinderUtil lfu){
        Log.e(TAG, " getHotelsDataFromServer method started.");
        if(lfu.checkGooglePlayServiceAvailablity()){
            lfu.buildGoogleApiClient();
            lfu.buildLocationRequest();
            lfu.buildLocationSettingsRequest();
            lfu.connectGoogleAPIClient();
            lfu.checkLocationSettings();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG," onActivityResult method started.");
        switch (requestCode) {
            case REQUEST_CHECK_LOCATION_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG," REQUEST_CHECK_LOCATION_SETTINGS is RESULT_OK");
                        lfu.requestLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG," REQUEST_CHECK_LOCATION_SETTINGS is RESULT_CANCELED");
                        finish();
                        break;
                    default:
                        Log.e(TAG," REQUEST_CHECK_LOCATION_SETTINGS is default");
                        break;
                }
        }
    }

    public void gotoDisplayHotelsActivity() {
        Log.e(TAG," gotoDisplayHotelsActivity method started.");
        Intent displayHotelsIntent = new Intent(this, DisplayHotelsActivity.class);
        startActivity(displayHotelsIntent);
        finish();
    }

    public synchronized static Cart  getCartInstance() {
        Log.e(TAG, " getCartInstance method started.");
        return mCart;

    }
}