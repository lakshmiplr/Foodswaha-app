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

    /*method to verify internet connectivity*/
    public static boolean isConnectivityAvailable(Context context) {

        Log.e(TAG, " isConnectivityAvailable method called.");
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
