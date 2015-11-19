package com.foodswaha.foodswaha;

/**
 * Created by pharshar on 11/17/2015.
 */
public class Address {
    private String flatNumber;
    private String streetName;
    private String area;
    private String city;
    private String landmark;

    public Address( String flatNumber, String streetName, String area, String city, String landmark) {
        this.flatNumber = flatNumber;
        this.streetName = streetName;
        this.area = area;
        this.city = city;
        this.landmark = landmark;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getLandmark() {
        return landmark;
    }

}
