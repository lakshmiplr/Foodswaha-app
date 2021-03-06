package com.foodswaha.foodswaha;

import android.graphics.Bitmap;

/**
 * Created by pharshar on 10/10/2015.
 */
public class Hotel {
    String id;
    String name;
    String address;
    String area;
    String deliveryTime;
    String deliveryFee;
    String minOrder;
    String onTime;
    String rating;
    String timings;
    String imageUrl;
    Bitmap hotelImage = null;
    String foodTypes;

    public Hotel(String id, String name, String address, String area, String deliveryTime, String deliveryFee, String minOrder, String onTime, String rating, String timings,String imageUrl,String foodTypes) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.area = area;
        this.deliveryTime = deliveryTime;
        this.deliveryFee = deliveryFee;
        this.minOrder = minOrder;
        this.onTime = onTime;
        this.rating = rating;
        this.timings = timings;
        this.imageUrl = imageUrl;
        this.foodTypes = foodTypes;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(Bitmap hotelImage) {
        this.hotelImage = hotelImage;
    }

    public String getFoodTypes() {
        return foodTypes;
    }

    public String getArea() {
        return area;
    }
}
