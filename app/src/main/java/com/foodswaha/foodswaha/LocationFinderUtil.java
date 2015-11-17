package com.foodswaha.foodswaha;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by pharshar on 10/25/2015.
 */
public class LocationFinderUtil implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult>,
        LocationListener {

    private static final String TAG = "LocationFinderUtil";

    private Context context;
    private Activity activity;
    private AppInitializerActivity appInitializerActivity;
    private static LocationFinderUtil instance;


    LocationFinderUtil(AppInitializerActivity appInitializerActivity){
        Log.e(TAG, " constructor started.");
        this.context = appInitializerActivity;
        this.activity = appInitializerActivity;
        this.appInitializerActivity = appInitializerActivity;
        this.instance = this;

    }

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mCurrentLocation;
    private JSONObject mJsonResponse;


    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static final String GET_HOTEL_DETAILS_URL = "http://104.199.135.27:8080/location";

    private final static int GET_GOOGLE_PLAY_SERVICES_REQUEST_CODE = 1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST_CODE = 2;
    private static final int REQUEST_CHECK_LOCATION_SETTINGS = 3;

    public boolean checkGooglePlayServiceAvailablity() {
        Log.e(TAG," checkGooglePlayServiceAvailablity method started.");

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, GET_GOOGLE_PLAY_SERVICES_REQUEST_CODE);
            dialog.show();
            Log.e(TAG, " GooglePlayServices not available.");
            return false;
        }
        Log.e(TAG, " GooglePlayServices available.");
        return true;
    }

    public void buildGoogleApiClient() {
        Log.e(TAG, " buildGoogleApiClient method started.");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void buildLocationRequest() {
        Log.e(TAG, " buildLocationRequest method started.");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
    }

    public void buildLocationSettingsRequest() {
        Log.e(TAG, " buildLocationSettingsRequest method started.");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    public void checkLocationSettings() {
        Log.e(TAG, " checkLocationSettings method started.");
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this, 2500L, TimeUnit.MILLISECONDS);
    }

    public void connectGoogleAPIClient(){
        Log.e(TAG, " connectGoogleAPIClient method started.");
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }

    }

    public void requestLocationUpdates(){
        Log.e(TAG, " requestLocationUpdates method started.");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e(TAG, " LocationSettingsStatusCodes SUCCESS.");
                requestLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    Log.e(TAG, " LocationSettingsStatusCodes RESOLUTION_REQUIRED.");
                    status.startResolutionForResult(
                            activity,
                            REQUEST_CHECK_LOCATION_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, " Exception occured while trying to do startResolutionForResult  REQUEST_CHECK_LOCATION_SETTINGS :\n"+e);
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.e(TAG, " LocationSettingsStatusCodes SETTINGS_CHANGE_UNAVAILABLE.");
                try {
                    status.startResolutionForResult(
                            activity,
                            REQUEST_CHECK_LOCATION_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, " onConnected method started.");
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, " onConnectionSuspended method started. "+i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, " onConnectionFailed method started.");
        if (connectionResult.hasResolution()) {
            try {
                Log.e(TAG, " onConnectionFailed doing CONNECTION_FAILURE_RESOLUTION_REQUEST");
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, " Exception occurred while doing CONNECTION_FAILURE_RESOLUTION_REQUEST "+e);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, " onLocationChanged method started. with location "+location);
        if (mCurrentLocation == null && location != null) {
            mCurrentLocation = location;
            if (mGoogleApiClient.isConnected()) {
                Log.e(TAG, " trying to removeLocationUpdates from google.");
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
                Log.e(TAG, "mGoogleApiClient got disconnected.");
            }
            buildJsonObjectRequest(mCurrentLocation);
        }
    }

    public static LocationFinderUtil getInstance(){
        Log.e(TAG, "getInstance method started.giving instance as "+instance);
        return instance;
    }

    public JSONObject getJsonResponseData(){
        Log.e(TAG, "getJsonResponseData method started.giving JSONObject as "+mJsonResponse);
        return mJsonResponse;
    }

    public  void buildJsonObjectRequest(Location location) {
        Log.e(TAG, "buildJsonObjectRequest method started.with location as "+location);
        JsonObjectRequest jsonObjectRequest = null;
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        latitude = "12.92";
        longitude="77.61";
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GET_HOTEL_DETAILS_URL,new JSONObject("{\"latitude\":\""+latitude+"\",\"longitude\":\""+longitude+"\"}"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                mJsonResponse = response;
                                Log.e(TAG, "JsonResponse received from server.response is "+mJsonResponse);
                                appInitializerActivity.gotoDisplayHotelsActivity(response);
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
