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
    int quantity;
    String menuName;
    String hotelName;

    public HotelMenuItemSub(String name, String cost, int quantity,String menuName,String hotelName) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.menuName =menuName;
        this.hotelName = hotelName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HotelMenuItemSub)
            return name.equals(((HotelMenuItemSub) obj).getName());
        else
            return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
