package com.foodswaha.foodswaha;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by pharshar on 10/25/2015.
 */
public class InternetCheckUtil {

    private static final String TAG = "InternetCheckUtil";
    private static ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getAppContext()
                                                      .getSystemService(Context.CONNECTIVITY_SERVICE);

    /*method to verify internet connectivity*/
    public static boolean isConnectivityAvailable() {

        Log.e(TAG, " isConnectivityAvailable method called.");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Log.e(TAG, " isConnectivityAvailable method returned true.");
            return true;
        }
        Log.e(TAG, " isConnectivityAvailable method returned false.");
        return false;
    }
}
