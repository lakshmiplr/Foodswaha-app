package com.foodswaha.foodswaha;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AddAddressActivity extends AppCompatActivity {
    private static final String POST_ADDRESS_DETAILS_CREATE_URL = "http://104.199.135.27:8080/address/create";
    private static final String POST_ADDRESS_DETAILS_UPDATE_URL = "http://104.199.135.27:8080/address/update";
    private JSONObject mJsonResponse;
    private static final String TAG = "AddaddressActivity";
    Cart cartInstance = Cart.getInstance();
    private static final int REQ_CODE_SPEECH_INPUT = 1;
    private static EditText speechEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        final EditText flatNumber = (EditText) findViewById(R.id.flatNumber);
        final EditText streetName = (EditText) findViewById(R.id.streetName);
        final EditText area = (EditText) findViewById(R.id.area);
        final EditText city = (EditText) findViewById(R.id.city);
        final EditText landMark = (EditText) findViewById(R.id.landmark);
        if(getIntent().getStringExtra("edit")!=null){
            flatNumber.setText(getIntent().getStringExtra("flatNumber"));
            streetName.setText(getIntent().getStringExtra("streetDetails"));
            area.setText(getIntent().getStringExtra("area"));
            city.setText(getIntent().getStringExtra("city"));
            landMark.setText(getIntent().getStringExtra("landMark"));

        }else{
            area.setText(DisplayHotelsActivity.getAREA());
            city.setText(DisplayHotelsActivity.getCITY());
        }


        final EditText mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        mobileNumber.setText(LoginActivity.getMobileNumber());

        String dfeeString = HotelsFragment.getHolder().hotelDFee.getText().toString().replace(":", "");
        final int dfeeInt = Integer.parseInt(dfeeString.replace("Rs", "").trim());
        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));

        final ImageButton flatSpeech = (ImageButton)findViewById(R.id.flatSpeech);
        flatSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechEditText = flatNumber;
                promptSpeechInput();
            }
        });
        final ImageButton streetSpeech = (ImageButton)findViewById(R.id.streetSpeech);
        streetSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechEditText = streetName;
                promptSpeechInput();
            }
        });
        final ImageButton landmarkSpeech = (ImageButton)findViewById(R.id.landmarkSpeech);
        landmarkSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechEditText = landMark;
                promptSpeechInput();
            }
        });
        final ImageButton mobileSpeech = (ImageButton)findViewById(R.id.mobileSpeech);
        mobileSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechEditText = mobileNumber;
                promptSpeechInput();
            }
        });

        final ImageButton flatClear = (ImageButton)findViewById(R.id.flatClear);
        flatClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flatNumber.setText("");
            }
        });
        final ImageButton streetClear = (ImageButton)findViewById(R.id.streetClear);
        streetClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetName.setText("");
            }
        });
        final ImageButton landmarkClear = (ImageButton)findViewById(R.id.landmarkClear);
        landmarkClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landMark.setText("");
            }
        });
        final ImageButton mobileClear = (ImageButton)findViewById(R.id.mobileClear);
        mobileClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNumber.setText("");
            }
        });

        flatNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(flatNumber.getText().toString().isEmpty()){
                        flatSpeech.setVisibility(View.VISIBLE);
                    }else{
                        flatClear.setVisibility(View.VISIBLE);
                    }
                } else {
                    flatSpeech.setVisibility(View.GONE);
                    flatClear.setVisibility(View.GONE);
                }
            }
        });
        streetName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(streetName.getText().toString().isEmpty()){
                        streetSpeech.setVisibility(View.VISIBLE);
                    }else{
                        streetClear.setVisibility(View.VISIBLE);
                    }
                }else{
                    streetSpeech.setVisibility(View.GONE);
                    streetClear.setVisibility(View.GONE);
                }
            }
        });
        landMark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(landMark.getText().toString().isEmpty()){
                        landmarkSpeech.setVisibility(View.VISIBLE);
                    }else{
                        landmarkClear.setVisibility(View.VISIBLE);
                    }
                }else{
                    landmarkSpeech.setVisibility(View.GONE);
                    landmarkClear.setVisibility(View.GONE);
                }
            }
        });
        mobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(mobileNumber.getText().toString().isEmpty()){
                        mobileSpeech.setVisibility(View.VISIBLE);
                    }else{
                        mobileClear.setVisibility(View.VISIBLE);
                    }
                }else{
                    mobileSpeech.setVisibility(View.GONE);
                    mobileClear.setVisibility(View.GONE);
                }
            }
        });

        flatNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, " on onTextChanged method called with query" + cs);
                if (!flatNumber.getText().toString().equals("")) {
                    flatClear.setVisibility(View.VISIBLE);
                    flatSpeech.setVisibility(View.GONE);
                } else {
                    flatClear.setVisibility(View.GONE);
                    flatSpeech.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        streetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, " on onTextChanged method called with query" + cs);
                if (!streetName.getText().toString().equals("")) {
                    streetClear.setVisibility(View.VISIBLE);
                    streetSpeech.setVisibility(View.GONE);

                } else {
                    streetClear.setVisibility(View.GONE);
                    streetSpeech.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        landMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, " on onTextChanged method called with query" + cs);
                if (!landMark.getText().toString().equals("")) {
                    landmarkClear.setVisibility(View.VISIBLE);
                    landmarkSpeech.setVisibility(View.GONE);
                } else {
                    landmarkClear.setVisibility(View.GONE);
                    landmarkSpeech.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, " on onTextChanged method called with query" + cs);
                if (!mobileNumber.getText().toString().equals("")) {
                    mobileClear.setVisibility(View.VISIBLE);
                    mobileSpeech.setVisibility(View.GONE);
                } else {
                    mobileClear.setVisibility(View.GONE);
                    mobileSpeech.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        final View payment = findViewById(R.id.checkout);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correctValues = true;
                EditText wrongEditText=null;
                if(mobileNumber.getText().toString().length()!=10||mobileNumber.getText().toString().matches("\\D+")){
                    mobileNumber.setError("wrong mobile number",null);
                    correctValues = false;
                    wrongEditText = mobileNumber;
                }
                if(streetName.getText().toString().length()<3){
                    streetName.setError("streetdetails needed",null);
                    correctValues = false;
                    wrongEditText = streetName;
                }
                if(flatNumber.getText().toString().length()<1){
                    flatNumber.setError("flatnumber is needed",null);
                    correctValues = false;
                    wrongEditText = flatNumber;
                }
                if(!correctValues){
                    wrongEditText.requestFocus();
                }

                if(correctValues){
                    if(getIntent().getStringExtra("edit")!=null){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();
                    JSONArray addressesJSONArray = addressObject.optJSONArray("addresses");

                    try {
                        JSONObject address = new JSONObject("{\"flatNumber\":\""+flatNumber.getText().toString()+"\"," +
                                "\"streetDetails\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");
                        addressesJSONArray.put(Integer.parseInt(getIntent().getStringExtra("id")),address);
                        addressObject.put("mobile",mobileNumber.getText());
                        LoginActivity.setAddressJSONObject(addressObject);
                        sendUpdateAddressRequestToServer(addressObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else if(getIntent().getStringExtra("type").equals("new")){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();
                    JSONArray addressesJSONArray = addressObject.optJSONArray("addresses");

                    try {
                        JSONObject address = new JSONObject("{\"flatNumber\":\""+flatNumber.getText().toString()+"\"," +
                                "\"streetDetails\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");

                        addressesJSONArray.put(address);
                        addressObject.put("mobile",mobileNumber.getText());
                        LoginActivity.setAddressJSONObject(addressObject);
                        sendUpdateAddressRequestToServer(addressObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else if(getIntent().getStringExtra("type").equals("first")){
                    JSONObject addressObject  = LoginActivity.getAddressJSONObject();

                    try {
                        JSONObject address = new JSONObject("{\"flatNumber\":\""+flatNumber.getText().toString()+"\"," +
                                "\"streetDetails\":\""+streetName.getText().toString()+"\"," +
                                "\"area\":\""+area.getText().toString()+"\"," +
                                "\"city\":\""+city.getText().toString()+"\"," +
                                "\"landmark\":\""+landMark.getText().toString()+"\""+
                                "}");
                        JSONArray addressJSONArray = new JSONArray();
                        addressJSONArray.put(address);
                        addressObject.put("addresses",addressJSONArray);
                        addressObject.put("email",LoginActivity.getEmail());
                        addressObject.put("mobile",mobileNumber.getText());
                        LoginActivity.setAddressJSONObject(addressObject);
                        sendCreateAddressRequestToServer(addressObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                    Intent gotoPaymentIntent = new Intent(AddAddressActivity.this, PaymentActivity.class);
                    startActivity(gotoPaymentIntent);
                }
            }
        });
    }

    private void sendCreateAddressRequestToServer(JSONObject addressObject){
        JsonObjectRequest  jsonObjectPost = new JsonObjectRequest(Request.Method.PUT, POST_ADDRESS_DETAILS_CREATE_URL,addressObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                            }
                            catch (Exception e) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectPost);
    }

    private void sendUpdateAddressRequestToServer(JSONObject addressObject){
        JsonObjectRequest  jsonObjectPost = new JsonObjectRequest(Request.Method.POST, POST_ADDRESS_DETAILS_UPDATE_URL,addressObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                        }
                        catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectPost);
    }


    @Override
    public void onBackPressed() {
        if(LoginActivity.getAddressCount()==0){
            Intent chooseDeliveryTypeActivity = new Intent(AddAddressActivity.this, ChooseDeliveryTypeActivity.class);
            startActivity(chooseDeliveryTypeActivity);
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // here the string converted from your voice
                    String converted_text = result.get(0);
                    if(speechEditText!=null){
                        speechEditText.setText(converted_text);
                    }
                }
                break;
            }
        }
    }

}

