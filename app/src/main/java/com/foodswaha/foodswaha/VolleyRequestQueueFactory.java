package com.foodswaha.foodswaha;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by reddyl on 10-10-2015.
 */
public class VolleyRequestQueueFactory {

    private static VolleyRequestQueueFactory mInstance = null;
    private RequestQueue mRequestQueue;

    private VolleyRequestQueueFactory(){
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleyRequestQueueFactory getInstance(){
        if(mInstance == null){
            mInstance = new VolleyRequestQueueFactory();
        }
        return mInstance;
    }

    public  RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

}
