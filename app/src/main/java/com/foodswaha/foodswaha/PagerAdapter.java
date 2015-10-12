package com.foodswaha.foodswaha;

/**
 * Created by pharshar on 9/24/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs =5;
    //private String tabTitles[] = new String[] { "Hotels", "Orders", "Places","Deals" };

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
            /*case 2:
                UserAddressesFragment userAddressesFragment = new UserAddressesFragment();
                return userAddressesFragment;
            case 3:
                FavFragment favFragment = new FavFragment();
                return favFragment;*/
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

    /*@Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }*/
}
