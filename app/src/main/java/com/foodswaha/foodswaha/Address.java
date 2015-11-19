package com.foodswaha.foodswaha;

/**
 * Created by pharshar on 11/17/2015.
 */
public class Address {
    private int id;
    private String flatNumber;
    private String streetName;
    private String area;
    private String city;
    private String landmark;

    public Address( int id,String flatNumber, String streetName, String area, String city, String landmark) {
        this.id = id;
        this.flatNumber = flatNumber;
        this.streetName = streetName;
        this.area = area;
        this.city = city;
        this.landmark = landmark;
    }

    public Address(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return id == ((Address)o).getId();
    }
}
