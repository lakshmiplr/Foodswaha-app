package com.foodswaha.foodswaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pharshar on 9/24/2015.
 */
public class OrdersFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    public static final int RC_SIGN_IN = 9001;
    private static GoogleApiClient mGoogleApiClient;
    private View view;
    private static ArrayAdapter adapter;
    private ListView orderListView;

    private static final String GET_ORDER_DETAILS_URL = "http://104.199.135.27:8080/order/get";
    private static List<Order> orderList;
    private static Order selectedOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        view.findViewById(R.id.signIn).setOnClickListener(this);
        orderListView = (ListView)view.findViewById(R.id.orderList);
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(orderList!=null){
                    selectedOrder = orderList.get(position);
                }
                Intent displayOrderDetailsIntent = new Intent(getContext(), DisplayOrderDetails.class);
                startActivity(displayOrderDetailsIntent);

            }
        });
        mGoogleApiClient = DisplayHotelsActivity.getGoogleApiClient();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            view.findViewById(R.id.signIn).setVisibility(View.GONE);
            view.findViewById(R.id.orderList).setVisibility(View.VISIBLE);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            if (result.isSuccess()) {
                view.findViewById(R.id.signIn).setVisibility(View.GONE);
                view.findViewById(R.id.orderList).setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            JsonArrayRequest jsonArrayRequest = null;
            if(adapter==null){
                try {
                    jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, GET_ORDER_DETAILS_URL,new JSONObject("{\"email\":\""+acct.getEmail()+"\"}"),
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        orderList = new ArrayList<Order>();
                                        OrderItem orderItem;
                                        List<OrderItem> orderItemList =null;
                                        for(int i=0;i<response.length();i++){
                                            JSONObject orderJSONObject = response.getJSONObject(i);
                                            JSONArray orderItemJSONArray = orderJSONObject.optJSONArray("cart");
                                            if(orderItemJSONArray!=null){
                                                JSONObject orderItemJSONObject;
                                                orderItemList = new ArrayList<OrderItem>();
                                                for(int j = 0; j < orderItemJSONArray.length(); j++){
                                                    orderItemJSONObject = orderItemJSONArray.getJSONObject(j);
                                                    orderItem = new OrderItem(orderItemJSONObject.optString("itemname"),
                                                            orderItemJSONObject.optString("itemcount"),orderItemJSONObject.optString("itemprice"));
                                                    orderItemList.add(orderItem);
                                                }
                                            }
                                            Order order =new Order(orderJSONObject.optString("hotelimageurl"),orderJSONObject.optString("hotelname"),
                                                    orderJSONObject.optString("area"),orderJSONObject.optString("date"),orderJSONObject.optString("time"),
                                                    orderJSONObject.optString("total"),orderJSONObject.optString("status"),
                                                    orderJSONObject.optString("deliverytype"),orderJSONObject.optString("address"),orderItemList);
                                            orderList.add(order);
                                        }


                                        adapter = new OrderAdapter(getContext(),
                                                R.layout.activity_display_order_item, orderList);
                                        orderListView.setAdapter(adapter);

                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "error occurred while getting hotel data from server. "+error);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonArrayRequest);
            }else{
                orderListView.setAdapter(adapter);
            }

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signIn:
                signIn();
                break;

        }
    }

    public static void setAdapter(ArrayAdapter adapter) {
        OrdersFragment.adapter = adapter;
    }

    public static Order getSelectedOrder() {
        return selectedOrder;
    }
}