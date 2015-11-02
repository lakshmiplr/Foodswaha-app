package com.foodswaha.foodswaha;

import java.util.List;

/**
 * Created by pharshar on 11/1/2015.
 */
public class HotelMenuItem {

    String name;
    List HotelMenuItemSub;

    public HotelMenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
class HotelMenuItemSub{
    String name;
    String cost;
    String description;

    public HotelMenuItemSub(String name, String cost, String description) {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
