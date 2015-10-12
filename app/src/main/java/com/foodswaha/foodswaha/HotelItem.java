package com.foodswaha.foodswaha;

/**
 * Created by pharshar on 10/10/2015.
 */
public class HotelItem {
    String id;
    String name;
    String address;
    String deliveryTime;
    String deliveryFee;
    String minOrder;
    String onTime;
    String rating;
    String timings;

    public HotelItem(String id, String name, String address, String deliveryTime, String deliveryFee, String minOrder, String onTime, String rating, String timings) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.deliveryTime = deliveryTime;
        this.deliveryFee = deliveryFee;
        this.minOrder = minOrder;
        this.onTime = onTime;
        this.rating = rating;
        this.timings = timings;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getMinOrder() {
        return minOrder;
    }

    public String getOnTime() {
        return onTime;
    }

    public String getRating() {
        return rating;
    }

    public String getTimings() {
        return timings;
    }
}
