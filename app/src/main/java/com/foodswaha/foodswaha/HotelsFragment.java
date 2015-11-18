package com.foodswaha.foodswaha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pharshar on 9/24/2015.
 */
public class HotelsFragment extends Fragment {

    private static final String TAG = "HotelsFragment";
    private static final String GET_HOTEL_MENU_URL = "http://104.199.135.27:8080/hotel/";
    private static JSONObject menuJSONObject =null;
    private JsonObjectRequest jsonObjectRequest;
    private static HotelAdapter.Holder  holder;
    private  Map hotelToMenuJSONObjectDataMap = new HashMap<String,JSONObject>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e(TAG, " onCreateView method started.");
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);
        HotelAdapter adapter = DisplayHotelsActivity.getHotelAdapter();
        Log.e(TAG, " got HotelItemAdapter from  DisplayHotelsActivity as,"+adapter);
        if(adapter!=null){
            ListView hotelListView = (ListView)view.findViewById(R.id.hotelList);
            hotelListView.setAdapter(adapter);

            hotelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    holder = (HotelAdapter.Holder) view.getTag();
                    DisplayMenuActivity.setMenuToSubMenuMap(null);
                    menuJSONObject = (JSONObject) hotelToMenuJSONObjectDataMap.get(holder.hotelId);
                    if (menuJSONObject == null) {
                        try {
                            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_HOTEL_MENU_URL + holder.hotelId,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {

                                                hotelToMenuJSONObjectDataMap.put(holder.hotelId, response);
                                                Log.e(TAG, "Menu JsonResponse received from server.response is " + response);
                                                Intent displayHotelMenuIntent = new Intent(getContext(), DisplayMenuActivity.class);
                                                displayHotelMenuIntent.putExtra("menuData",response.toString());
                                                startActivity(displayHotelMenuIntent);
                                            } catch (Exception e) {
                                                Log.e(TAG, " gotoDisplayHotelMenuActivity method got exception. " + e);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, " gotoDisplayHotelMenuActivity method onErrorResponse got exception. " + error);
                                    Intent displayHotelMenuIntent = new Intent(getContext(), DisplayMenuActivity.class);
                                    displayHotelMenuIntent.putExtra("menuData",new JSONObject().toString());
                                    startActivity(displayHotelMenuIntent);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        VolleyRequestQueueFactory.getInstance().getRequestQueue().add(jsonObjectRequest);
                    } else {
                        Intent displayHotelMenuIntent = new Intent(getContext(), DisplayMenuActivity.class);
                        displayHotelMenuIntent.putExtra("menuData", menuJSONObject.toString());
                        startActivity(displayHotelMenuIntent);
                    }

                }
            });
            hotelListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (view != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
        return view;
    }

    public static HotelAdapter.Holder getHolder() {
        return holder;
    }

}
