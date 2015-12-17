package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.SdkSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    HashMap<String, String> params = new HashMap<>();
    Cart cartInstance = Cart.getInstance();
    private static final String PUT_ORDER_DETAILS_CREATE_URL = "http://104.199.135.27:8080/order/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        final ImageButton paymoney = (ImageButton) findViewById(R.id.payumoney);
        paymoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPayuMoney();
            }
        });
    }


    private void sendOrderDetailsToServer(){
        JSONObject orderObject = new JSONObject();
        try {
            orderObject.put("email",LoginActivity.getEmail());
            orderObject.put("mobile",LoginActivity.getMobileNumber());
            orderObject.put("hotelid",HotelsFragment.getHolder().hotelId);
            orderObject.put("hotelname",HotelsFragment.getHolder().hotelName.getText());
            orderObject.put("area",DisplayHotelsActivity.getAREA());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:m:s a");
            String date = dateFormat.format(calendar.getTime());
            String time = timeFormat.format(calendar.getTime());
            orderObject.put("date",date);
            orderObject.put("time",time);
            orderObject.put("hotelimageurl",HotelsFragment.getHolder().hotelImageURL);
            orderObject.put("deliverytype",ChooseDeliveryTypeActivity.getCdt());
            orderObject.put("address",LoginActivity.getDeliveryAddress());
            orderObject.put("total", cartInstance.getTotalBill());
            orderObject.put("status","live");
            JSONArray cartJSONArray = new JSONArray();
            for(SubMenu item :cartInstance.getAllCartItems()){
                JSONObject cartJSONObject = new JSONObject("{\"itemname\":\""+item.getName()+"\"," +
                        "\"itemprice\":\""+item.getCost()+"\"," +
                        "\"itemcount\":\""+item.getQuantity()+"\"" +
                        "}");
                cartJSONArray.put(cartJSONObject);
            }
            orderObject.put("cart",cartJSONArray);
            sendCreateOrderRequestToServer(orderObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchPayuMoney() {
        if (SdkSession.getInstance(this) == null) {
            SdkSession.startPaymentProcess(this, params);
        } else {
            SdkSession.createNewInstance(this);
        }
        String transID = uuidString();
        Log.i("app_activity", "transaction is" + transID);
        String hashSequence = "aAWjO9" + "|" + transID + "|" + 2 + "|" + "product_name" + "|" + "lakshmi" + "|"
                + "lakshmi.plr@gmail.com" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "4w5lreTZ";
        params.put("key", "aAWjO9");
        params.put("MerchantId", "5306536");
        String hash = hashCal(hashSequence);
        params.put("TxnId", transID);
        params.put("SURL", "https://www.payumoney.com/mobileapp/payumoney/success.php");
        params.put("FURL", "https://www.payumoney.com/mobileapp/payumoney/failure.php");
        params.put("ProductInfo", "product_name");
        params.put("firstName", "lakshmi");
        params.put("Email", "lakshmi.plr@gmail.com");
        params.put("Phone", "9886403405");
        params.put("Amount", "2");
        params.put("hash", hash);
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        SdkSession.startPaymentProcess(this, params);
    }
    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(data!=null) {
        if (requestCode == SdkSession.PAYMENT_SUCCESS) {
            if (resultCode == RESULT_OK) {
                Log.i("app_activity", "success");
                Log.i("paymentID", data.getStringExtra("paymentId"));
                sendOrderDetailsToServer();
            }

            if (resultCode == RESULT_CANCELED) {
                Log.i("app_activity", "failure");
            }
            //Write your code if there's no result
        }
    }
    public static String randomStringOfLength(int length) {
        StringBuffer buffer = new StringBuffer();
        while (buffer.length() < length) {
            buffer.append(uuidString());
        }

        //this part controls the length of the returned string
        return buffer.substring(0, length);
    }


    private static String uuidString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    private void sendCreateOrderRequestToServer(JSONObject orderObject){
        JsonObjectRequest jsonObjectPost = new JsonObjectRequest(Request.Method.PUT, PUT_ORDER_DETAILS_CREATE_URL,orderObject,
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

}
