package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class DisplayNoInternetActivity extends Activity
        implements NetworkConnectivityStatusChangeReceiverListner {

    private NetworkConnectivityStatusChangeReceiver networkConnectivityStatusChangeReceiver;

    protected static final String TAG = "No_Internet_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkConnectivityStatusChangeReceiver = new NetworkConnectivityStatusChangeReceiver();
        networkConnectivityStatusChangeReceiver.addListener(this, this);
        registerReceiver(networkConnectivityStatusChangeReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        setContentView(R.layout.activity_display_no_internet);
    }


    @Override
    protected void onResume() {
        super.onResume();
        networkConnectivityStatusChangeReceiver.addListener(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkConnectivityStatusChangeReceiver.removeListener(this);
    }

    protected  void gotoDisplayHotels() {
       Intent displayHotelsIntent = new Intent(this,DisplayHotelsActivity.class);
       startActivity(displayHotelsIntent);
   }

    @Override
    public void networkConnectivityAvailable() {
        gotoDisplayHotels();
    }

    @Override
    public void networkConnectivityUnavailable() {

    }

}
