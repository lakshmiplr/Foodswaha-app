package com.foodswaha.foodswaha;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
 * Using location settings.
 * <p/>
 * Uses the {@link com.google.android.gms.location.SettingsApi} to ensure that the device's system
 * settings are properly configured for the app's location needs. When making a request to
 * Location services, the device's system settings may be in a state that prevents the app from
 * obtaining the location data that it needs. For example, GPS or Wi-Fi scanning may be switched
 * off. The {@code SettingsApi} makes it possible to determine if a device's system settings are
 * adequate for the location request, and to optionally invoke a dialog that allows the user to
 * enable the necessary settings.
 * <p/>
 * This code allows the user to request location updates using the ACCESS_FINE_LOCATION setting
 * (as specified in AndroidManifest.xml). The code requires that the device has location enabled
 * and set to the "High accuracy" mode. If location is not enabled, or if the location mode does
 * not permit high accuracy determination of location, the activity uses the {@code SettingsApi}
 * to invoke a dialog without requiring the developer to understand which settings are needed for
 * different Location requirements.
 */
public class DisplayHotelsActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>,
        NetworkConnectivityStatusChangeReceiverListner {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST_CODE = 1;

    public final static int GET_GOOGLE_PLAY_SERVICES_REQUEST_CODE = 2;

    protected static final String TAG = "Location_TAG";
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_LOCATION_SETTINGS = 3;

    protected static final int VOICE_RECOGNITION_REQUEST_CODE = 4;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    protected String mFacebookId;

    protected String mGmailId;

    protected String mMobileNumber;

    private NetworkConnectivityStatusChangeReceiver networkConnectivityStatusChangeReceiver;

    private boolean is_Location_Settings_Check_done = false;

    private boolean checkLocationSettings_called_flag = true;

    private String GET_HOTEL_DETAILS_URL = "http://192.168.1.105:3000/hotels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_hotels);
        //networkConnectivityStatusChangeReceiver = new NetworkConnectivityStatusChangeReceiver();
        //networkConnectivityStatusChangeReceiver.addListener(this, this);
        //registerReceiver(networkConnectivityStatusChangeReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        try {
            displayHotels(new JSONObject("{\"area\" : \"btm 3rd stage\"}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*protected void CheckLocationRuntimePermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            DisplayHotelsActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }*/


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected void buildGoogleApiClient() {
        //Create GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.i(TAG, "GoogleApiClient created.");
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void buildLocationRequest() {
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                // Sets the desired interval for active location updates. This interval is
                // inexact. You may not receive updates at all if no location sources are available, or
                // you may receive them slower than requested. You may also receive updates faster than
                // requested if other applications are requesting location at a faster interval.
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                // Sets the fastest rate for active location updates. This interval is exact, and your
                // application will never receive updates faster than this value.
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        Log.i(TAG, "LocationRequest created.");
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        Log.i(TAG, "LocationSettingsRequest created.");
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this, 500L, TimeUnit.MILLISECONDS);
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        //final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can initialize location
                // requests here.
                Log.i(TAG, "All location settings are satisfied.");
                is_Location_Settings_Check_done =true;
                if(mGoogleApiClient.isConnected()){
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                    Log.i(TAG, "disconnecting and reconnecting GoogleApiClient as all location settings checks are over.");
                    mGoogleApiClient.connect();
                }
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. But could be fixed by showing the user
                // a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    status.startResolutionForResult(
                            DisplayHotelsActivity.this,
                            REQUEST_CHECK_LOCATION_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the
                // settings so we won't show the dialog.
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_LOCATION_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        is_Location_Settings_Check_done =true;
                        if(mGoogleApiClient.isConnected()){
                            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                            mGoogleApiClient.disconnect();
                            Log.i(TAG, "disconnecting and reconnecting GoogleApiClient as all location settings checks are over.");
                            mGoogleApiClient.connect();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                    default:
                        break;
                }
        }
    }


    private boolean checkGooglePlayServiceAvailablity() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) {
            Log.i(TAG, "GooglePlayServices is not available");
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, GET_GOOGLE_PLAY_SERVICES_REQUEST_CODE);
            dialog.show();
            return false;
        } else {
            Log.i(TAG, "GooglePlayServices is available");
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //networkConnectivityStatusChangeReceiver.addListener(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // networkConnectivityStatusChangeReceiver.removeListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected to Google Play Services.");
        if (is_Location_Settings_Check_done && mCurrentLocation == null) {
            Log.i(TAG, "GoogleApiClient requesting for location updates to Google Play Services.");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Location services connection resolution failed with code " + connectionResult.getErrorCode());
                Log.i(TAG, "Location services connection resolution failed with exception" + e);
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrentLocation == null && location != null) {
            mCurrentLocation = location;
            //send location to server
            SendLocationDataToServer(mCurrentLocation);
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
                Log.i(TAG, "GoogleApiClient disconnected");
            }
        }
        if (mCurrentLocation != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
                Log.i(TAG, "GoogleApiClient disconnected");
            }
        }
    }

    /*Send Location data to server to get hotels list .
     pass it to DisplayHotelsActivity to show them to user.*/
    protected void SendLocationDataToServer(Location location) {
        Log.d(TAG, "Current Location is" + location);
        Log.d(TAG, "Location data sending to server");
        buildJsonObjectRequest(location);
        //getAccountDetails();
        //TextView address = new TextView(getBaseContext());
        //address.setText("facebookId :"+mFacebookId+" ,gmailId: "+mGmailId+" ,mobileNumber: "+mMobileNumber);
        //setContentView(address);

    }


    protected void testVoice(){
        gatherSpeech("FoodSwaha", 1);
    }

    public void gatherSpeech(String prompt,int maxResults)
    {
        Intent recognizeIntent = getRecognizeIntent(prompt,maxResults);
        try
        {
            startActivityForResult(recognizeIntent, VOICE_RECOGNITION_REQUEST_CODE);
        }
        catch (ActivityNotFoundException actNotFound)
        {
            Log.w(TAG, "did not find the speech activity, not doing it");
        }
    }
    public Intent getRecognizeIntent(String promptToUse, int maxResultsToReturn)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResultsToReturn);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, promptToUse);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10);
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        return intent;
    }

    protected void buildJsonObjectRequest(Location location) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = null;
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GET_HOTEL_DETAILS_URL,new JSONObject("{\"latitude\":\""+latitude+"\",\"longitude\":\""+longitude+"\"}"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                 Log.i(TAG, "response received from server " + response.toString());
                                 displayHotels(response);
                            }
                            catch (Exception e) {
                                Log.i(TAG, "Error occurred while sending request to server "+response.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "Error occurred while receiving response from server "+error.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public void networkConnectivityAvailable() {
        Log.i(TAG, "network connectivity available");
        if(mGoogleApiClient == null && mCurrentLocation == null && checkGooglePlayServiceAvailablity()) {
            buildGoogleApiClient();
            buildLocationRequest();
            buildLocationSettingsRequest();
            //mGoogleApiClient.connect();
            Log.i(TAG, "GoogleApiClient connected to check location settings.");
            if(checkLocationSettings_called_flag){
                checkLocationSettings_called_flag =false;
                checkLocationSettings();
            }

        }
        else if(mGoogleApiClient!=null && mCurrentLocation == null && checkGooglePlayServiceAvailablity() && checkLocationSettings_called_flag) {
            checkLocationSettings_called_flag = false;
            checkLocationSettings();
        }
    }

    @Override
    //harsha
    public void networkConnectivityUnavailable() {
        Log.i(TAG,"network connectivity not available,going to noInternet activity.");
        gotoNoInternetActivity();
    }

    protected void gotoNoInternetActivity(){
        Intent noInternetConnection = new Intent(this,DisplayNoInternetActivity.class);
        startActivity(noInternetConnection);
    }

    protected void getAccountDetails(){
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccounts();
        for (Account account : accounts) {
            String accountName = account.name;
            String accountType = account.type;
            if(accountType.contains("com.facebook")){
                mFacebookId = account.name;
            }
            else if(accountType.contains("com.google")){
                mGmailId  = account.name;
            }

        }
        TelephonyManager telephonyManager = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        mMobileNumber = telephonyManager.getLine1Number();
    }

    protected void displayHotels(JSONObject response){

        TextView areaText = (TextView)findViewById(R.id.areaText);
        try {
            String area = response.getString("area");
            areaText.setText(area);

        } catch (JSONException e) {
            Log.i(TAG,"error occured while parsing server data");
        }
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationSettingsRequest();
                    checkLocationSettings();
                } else {
                }
            }
        }
    }*/
}
