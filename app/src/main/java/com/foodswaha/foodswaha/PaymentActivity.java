package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.PostParams.PaymentPostParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class PaymentActivity extends AppCompatActivity {

    PaymentParams mPaymentParams = new PaymentParams();
    PayuHashes mPayUHashes ;
    PayuConfig payuConfig;
    private PayUChecksum checksum;
    private String var1;
    private String key;
    private PostData postData;

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
        final Button ordersendButton = (Button) findViewById(R.id.ordersend);
        ordersendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderDetailsToServer();
            }
        });


        final ImageButton paymoney = (ImageButton) findViewById(R.id.payumoney);
        paymoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPayumoney();
            }
        });
    }

    private void launchPayumoney() {

        mPaymentParams.setKey("0MQaQP");
        mPaymentParams.setAmount("15.0");
        mPaymentParams.setProductInfo("Tshirt");
        mPaymentParams.setFirstName("foodswaha");
        mPaymentParams.setEmail("foodswaha@gmail.com");
        mPaymentParams.setTxnId("0123479543680");
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
        mPaymentParams.setUdf1("udf1l");
        mPaymentParams.setUdf2("udf2");
        mPaymentParams.setUdf3("udf3");
        mPaymentParams.setUdf4("udf4");
        mPaymentParams.setUdf5("udf5");
        // lets try to get the post params
        generateHashFromServer(mPaymentParams);

//        postData = new PayuWalletPostParams(mPaymentDefaultParams).getPayuWalletPostParams();


    }

    public  void generateHashFromServer(PaymentParams mPaymentParams){

        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayuConstants.KEY, mPaymentParams.getKey()));
        postParamsBuffer.append(concatParams(PayuConstants.AMOUNT, mPaymentParams.getAmount()));
        postParamsBuffer.append(concatParams(PayuConstants.TXNID, mPaymentParams.getTxnId()));
        postParamsBuffer.append(concatParams(PayuConstants.EMAIL, null == mPaymentParams.getEmail() ? "" : mPaymentParams.getEmail()));
        postParamsBuffer.append(concatParams(PayuConstants.PRODUCT_INFO, mPaymentParams.getProductInfo()));
        postParamsBuffer.append(concatParams(PayuConstants.FIRST_NAME, null == mPaymentParams.getFirstName() ? "" : mPaymentParams.getFirstName()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF1, mPaymentParams.getUdf1() == null ? "" : mPaymentParams.getUdf1()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF2, mPaymentParams.getUdf2() == null ? "" : mPaymentParams.getUdf2()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF3, mPaymentParams.getUdf3() == null ? "" : mPaymentParams.getUdf3()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF4, mPaymentParams.getUdf4() == null ? "" : mPaymentParams.getUdf4()));
        postParamsBuffer.append(concatParams(PayuConstants.UDF5, mPaymentParams.getUdf5() == null ? "" : mPaymentParams.getUdf5()));
        postParamsBuffer.append(concatParams(PayuConstants.USER_CREDENTIALS, mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials()));

        // for offer_key
        if(null != mPaymentParams.getOfferKey())
            postParamsBuffer.append(concatParams(PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey()));
        // for check_isDomestic

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        // make api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);

    }

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    class GetHashesFromServerTask extends AsyncTask<String, String, PayuHashes> {

        @Override
        protected PayuHashes doInBackground(String ... postParams) {
            PayuHashes payuHashes = new PayuHashes();
            try {
//                URL url = new URL(PayuConstants.MOBILE_TEST_FETCH_DATA_URL);
//                        URL url = new URL("http://10.100.81.49:80/merchant/postservice?form=2");;

                URL url = new URL("https://payu.herokuapp.com/get_hash");

                // get the payuConfig first
                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while(payuHashIterator.hasNext()){
                    String key = payuHashIterator.next();
                    switch (key){
                        case "payment_hash":
                            payuHashes.setPaymentHash(response.getString(key));
                            break;
                        case "get_merchant_ibibo_codes_hash": //
                            payuHashes.setMerchantIbiboCodesHash(response.getString(key));
                            break;
                        case "vas_for_mobile_sdk_hash":
                            payuHashes.setVasForMobileSdkHash(response.getString(key));
                            break;
                        case "payment_related_details_for_mobile_sdk_hash":
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(response.getString(key));
                            break;
                        case "delete_user_card_hash":
                            payuHashes.setDeleteCardHash(response.getString(key));
                            break;
                        case "get_user_cards_hash":
                            payuHashes.setStoredCardsHash(response.getString(key));
                            break;
                        case "edit_user_card_hash":
                            payuHashes.setEditCardHash(response.getString(key));
                            break;
                        case "save_user_card_hash":
                            payuHashes.setSaveCardHash(response.getString(key));
                            break;
                        case "check_offer_status_hash":
                            payuHashes.setCheckOfferStatusHash(response.getString(key));
                            break;
                        case "check_isDomestic_hash":
                            payuHashes.setCheckIsDomesticHash(response.getString(key));
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return payuHashes;
        }

        @Override
        protected void onPostExecute(PayuHashes payuHashes) {
            super.onPostExecute(payuHashes);
            mPayUHashes = payuHashes;
            mPaymentParams.setHash(mPayUHashes.getPaymentHash());
            launchPayumoneyActivity();



        }
    }

    private void launchPayumoneyActivity() {
        postData = new PaymentPostParams(mPaymentParams, PayuConstants.PAYU_MONEY).getPaymentPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR){
            PayuConfig payuConfig = new PayuConfig();
            payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
            payuConfig.setData(postData.getResult());
            Intent intent = new Intent(this,PaymentsActivity.class);
            intent.putExtra(PayuConstants.PAYU_CONFIG,payuConfig);
            startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
        } else {
            // something went wrong
            Toast.makeText(this,postData.getResult(), Toast.LENGTH_LONG).show();
        }
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
