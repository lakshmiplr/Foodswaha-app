package com.foodswaha.foodswaha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by pharshar on 9/24/2015.
 */
public class HotelsFragment extends Fragment {

    private static final String TAG = "HotelsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e(TAG, " onCreateView method started.");
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);
        HotelItemAdapter adapter = DisplayHotelsActivity.getHotelItemAdapter();
        Log.e(TAG, " got HotelItemAdapter from  DisplayHotelsActivity as,"+adapter);
        if(adapter!=null){
            ListView listView1 = (ListView)view.findViewById(R.id.hotelList);
            listView1.setAdapter(adapter);
        }
        return view;
    }


}
