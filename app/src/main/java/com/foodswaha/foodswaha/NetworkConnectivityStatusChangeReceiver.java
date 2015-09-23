package com.foodswaha.foodswaha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NetworkConnectivityStatusChangeReceiver extends BroadcastReceiver {

    protected static final String TAG = "Connectivity_Change";

    protected List<NetworkConnectivityStatusChangeReceiverListner> listeners;
    protected Boolean connected;

    public NetworkConnectivityStatusChangeReceiver() {
        listeners = new ArrayList<NetworkConnectivityStatusChangeReceiverListner>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent!=null && intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                Log.i(TAG, "Network " + networkInfo.getTypeName() + " connected");
                connected = true;
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(TAG, "There's no network connectivity");
                connected = false;
            }
        }
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(NetworkConnectivityStatusChangeReceiverListner listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkConnectivityStatusChangeReceiverListner listener) {

        if(connected == true) {
            listener.networkConnectivityAvailable();
        }
        else {
            listener.networkConnectivityUnavailable();
        }
    }

    public void addListener(NetworkConnectivityStatusChangeReceiverListner listener,Context context) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
            Log.i(TAG,listener.getClass().getSimpleName()+" added to NetworkConnectivityStatusChangeReceiver");
            if(connected==null) {
                 connected = isConnectivityAvailable(context);
                 Log.i(TAG,"current connectivity status is: "+connected);
            }
            notifyState(listener);
        }
    }

    public void removeListener(NetworkConnectivityStatusChangeReceiverListner listener) {
        if(listeners.contains(listener)){
            listeners.remove(listener);
            Log.i(TAG, listener.getClass().getSimpleName() + " removed from NetworkConnectivityStatusChangeReceiver");
        }
    }

    public boolean isConnectivityAvailable(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}