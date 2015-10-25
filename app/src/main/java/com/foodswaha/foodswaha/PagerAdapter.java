package com.foodswaha.foodswaha;

/**
 * Created by pharshar on 9/24/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "PagerAdapter";

    private int mNumOfTabs =5;
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HotelsFragment hotelsFragment = new HotelsFragment();
                return hotelsFragment;
            case 1:
                OrdersFragment ordersFragment = new OrdersFragment();
                return ordersFragment;
            case 2:
                KartFragment kartFragment = new KartFragment();
                return kartFragment;
            case 3:
                DealsFragment dealsFragment = new DealsFragment();
                return dealsFragment;
            case 4:
                MenuFragment menuFragment = new MenuFragment();
                return menuFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
