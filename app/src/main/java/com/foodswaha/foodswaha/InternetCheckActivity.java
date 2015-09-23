package com.foodswaha.foodswaha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/*main controller class which will decide first activity to hit on app opening*/
public class InternetCheckActivity extends Activity {
    /*constants start*/

    /*unique TAG string between our activities to identify log statements from this activity.*/
    protected static final String TAG = "InternetCheck_TAG";

    /*constants end*/


    /*checking internet connection.if available going to display hotels.else no internet.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate method called.");
        super.onCreate(savedInstanceState);
        //if internet connection available.
        if(isConnectivityAvailable(this)) {
            gotoDisplayHotels();
        }
        else{
            gotoDisplayHotels();
            //gotoNoInternetActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if internet connection available.
        if(isConnectivityAvailable(this)) {
            gotoDisplayHotels();
        }
        else{
            //gotoNoInternetActivity();
            gotoDisplayHotels();
        }
    }

    /*method to verify internet connectivity*/
    public boolean isConnectivityAvailable(Context context) {
        Log.i(TAG,"isConnectivityAvailable method called.");
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Log.i(TAG,"isConnectivityAvailable : TRUE.");
            return true;
        }
        Log.i(TAG,"isConnectivityAvailable : FALSE.");
        return false;
    }

    /*method to redirect control to display hotels activity.*/
    protected  void gotoDisplayHotels() {
        Log.i(TAG,"gotoDisplayHotels is getting called.");
        Intent displayHotelsIntent = new Intent(this,DisplayHotelsActivity.class);
        startActivity(displayHotelsIntent);
    }

    /*method to redirect control to no internet.*/
    protected void gotoNoInternetActivity(){
        Log.i(TAG,"gotoNoInternetActivity called.");
        Intent displaynoInternetConnectionIntent = new Intent(this,DisplayNoInternetActivity.class);
        startActivity(displaynoInternetConnectionIntent);
    }

}
