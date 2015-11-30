package com.foodswaha.foodswaha;

import java.util.List;

/**
 * Created by pharshar on 11/29/2015.
 */
public class Order {
    private String imageUrl;
    private String hotelName;
    private String area;
    private String date;
    private String time;
    private String total;
    private String status;
    private String deliveryType;
    private String address;
    private List<OrderItem> orderItemList;

    public Order(String imageUrl, String hotelName, String area, String date, String time, String total,
                 String status, String deliveryType, String address, List<OrderItem> orderItemList) {
        this.imageUrl = imageUrl;
        this.hotelName = hotelName;
        this.area = area;
        this.date = date;
        this.time = time;
        this.total = total;
        this.status = status;
        this.deliveryType = deliveryType;
        this.address = address;
        this.orderItemList = orderItemList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getArea() {
        return area;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }
}
