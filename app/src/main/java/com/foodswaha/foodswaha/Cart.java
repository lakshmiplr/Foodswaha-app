package com.foodswaha.foodswaha;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pharshar on 11/3/2015.
 */
public class Cart {
    private List foodItems = new ArrayList<HotelMenuItemSub>();
    private int totalBill;
    private int countOfItems;

    public Cart(List foodItems, int totalBill, int countOfItems) {
        this.foodItems = foodItems;
        this.totalBill = totalBill;
        this.countOfItems = countOfItems;
    }

    public Cart() {
    }

    public synchronized List getFoodItems() {
        return foodItems;
    }

    public synchronized void setFoodItems(List foodItems) {
        this.foodItems = foodItems;
    }

    public synchronized int getTotalBill() {
        return totalBill;
    }

    public synchronized void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public synchronized int getCountOfItems() {
        return countOfItems;
    }

    public synchronized void setCountOfItems(int countOfItems) {
        this.countOfItems = countOfItems;
    }
}
