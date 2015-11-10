package com.foodswaha.foodswaha;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pharshar on 11/3/2015.
 */
public class Cart {
    private Map<Integer,SubMenu> cartItems = new HashMap<Integer,SubMenu>();
    private static final String TAG = "Cart";
    private int totalBill;
    private int countOfItems;
    private static volatile Cart cartInstance;
    private static ArrayList<SubMenu> cartItemsList = new ArrayList<SubMenu>();

    private Cart() {
    }

    public static Cart getInstance(){
        if(cartInstance==null){
            cartInstance =  new Cart();
            Log.e(TAG, " cartInstance creation successfull.");
        }
        return cartInstance;
    }

    public synchronized SubMenu putItemToCart(Integer itemId,SubMenu subMenu) {
        Log.e(TAG, "putting item into cart,with itemId: "+itemId+" and item name: "+subMenu.getName());
        return cartItems.put(itemId, subMenu);
    }

    public synchronized SubMenu  removeItemFromCart(Integer itemId) {
        Log.e(TAG, "removing item from cart,with itemId: "+itemId);
       return cartItems.remove(itemId);
    }

    public synchronized SubMenu getCartItem(Integer itemId){
        Log.e(TAG, "getting item from cart,with itemId: "+itemId);
        return cartItems.get(itemId);
    }

    public synchronized int getTotalBill() {
        return totalBill;
    }

    public synchronized void addToTotalBill(int bill) {
         totalBill+=bill;
    }

    public synchronized int getCountOfItems() {
        return countOfItems;
    }

    public synchronized void incrementCountOfItems() {
        countOfItems++;
    }

    public synchronized void decrementCountOfItems() {
        countOfItems--;
    }

    public synchronized void removeFromTotalBill(int bill) {
        totalBill-=bill;
    }

    public synchronized List<SubMenu> getAllCartItems() {
        cartItemsList.clear();
        cartItemsList.addAll(cartItems.values());
        return cartItemsList;
    }

    public synchronized void clearCart() {
        cartItems.clear();
        countOfItems =0;
        totalBill =0;

    }


}
