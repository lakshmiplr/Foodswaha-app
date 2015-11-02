package com.foodswaha.foodswaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by pharshar on 9/24/2015.
 */
public class HotelsFragment extends Fragment {

    private static final String TAG = "HotelsFragment";
    private static final String GET_HOTEL_MENU_URL = "http://104.155.202.28:8080/hotel/";
    private static JSONObject mJsonResponse;
    private JsonObjectRequest jsonObjectRequest;
    private static HotelItemAdapter.HotelItemHolder  holder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e(TAG, " onCreateView method started.");
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);
        HotelItemAdapter adapter = DisplayHotelsActivity.getHotelItemAdapter();
        Log.e(TAG, " got HotelItemAdapter from  DisplayHotelsActivity as,"+adapter);
        if(adapter!=null){
            ListView listView1 = (ListView)view.findViewById(R.id.hotelList);
            listView1.setAdapter(adapter);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    holder = (HotelItemAdapter.HotelItemHolder)view.getTag();
                    try {
                        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_HOTEL_MENU_URL+holder.hotelId,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            mJsonResponse = response;
                                            Log.e(TAG, "JsonResponse received from server.response is " + mJsonResponse);
                                            gotoDisplayHotelMenuActivity(mJsonResponse);
                                        }
                                        catch (Exception e) {
                                            Log.e(TAG, " gotoDisplayHotelMenuActivity method got exception. "+e);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectRequest);


                }
            });
        }
        return view;
    }

    public static JSONObject getHotelMenuData(){
        return mJsonResponse;
    }

    public static HotelItemAdapter.HotelItemHolder getHolder() {
        return holder;
    }

    public void gotoDisplayHotelMenuActivity(JSONObject mJsonResponse) {
        Log.e(TAG, " gotoDisplayHotelMenuActivity method started.");
        Intent displayHotelMenuIntent = new Intent(getContext(), DisplayHotelMenuActivity.class);
        startActivity(displayHotelMenuIntent);
    }


}
