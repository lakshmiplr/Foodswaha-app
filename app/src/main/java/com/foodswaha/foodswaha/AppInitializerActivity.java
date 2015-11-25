package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

public class AppInitializerActivity extends AppCompatActivity {

    private static final String TAG = "AppInitializer";

    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 3;

    LocationFinderUtil lfu;
    private ProgressBar loading;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, " onCreate method started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_initializer);

        final ProgressBar loading = (ProgressBar)findViewById(R.id.loading);
        final TextView status = (TextView)findViewById(R.id.status);
        this.status = status;

        this.loading = loading;
        lfu = new LocationFinderUtil(this,loading,status);

        if( InternetCheckUtil.isConnectivityAvailable() ){
            Log.e(TAG," Internet connection available.");
            loading.setVisibility(View.VISIBLE);
            getHotelsDataFromServer(lfu);
        }
        else{
            Log.e(TAG, " Internet connection not available.");

            status.setVisibility(View.VISIBLE);
            final ImageButton retry = (ImageButton) findViewById(R.id.retry);
            retry.setVisibility(View.VISIBLE);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (InternetCheckUtil.isConnectivityAvailable()) {
                        Log.e(TAG, " Internet connection available after retry.");
                        status.setVisibility(View.GONE);
                        retry.setVisibility(View.GONE);
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
            status.setVisibility(View.VISIBLE);
            status.setText("checking location settings.");
            lfu.buildGoogleApiClient();
            lfu.buildLocationRequest();
            lfu.buildLocationSettingsRequest();
            lfu.connectGoogleAPIClient();
            lfu.checkLocationSettings();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, " onActivityResult method started.");
        switch (requestCode) {
            case REQUEST_CHECK_LOCATION_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, " REQUEST_CHECK_LOCATION_SETTINGS is RESULT_OK");
                        lfu.requestLocationUpdates();
                        loading.setProgress(35);
                        status.setText("fetching current Location");
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

    public void gotoDisplayHotelsActivity(JSONObject response) {
        Log.e(TAG," gotoDisplayHotelsActivity method started.");
        loading.setProgress(100);
        Intent displayHotelsIntent = new Intent(this, DisplayHotelsActivity.class);
        displayHotelsIntent.putExtra("hotelData",response.toString());
        startActivity(displayHotelsIntent);
        finish();
    }

}