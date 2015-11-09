package com.foodswaha.foodswaha;

import java.io.Serializable;

/**
 * Created by pharshar on 11/8/2015.
 */
public class SubMenu implements Serializable {
    int id;
    String name;
    String cost;
    int quantity;
    String menuName;
    String hotelName;

    public SubMenu(int id,String name, String cost, int quantity,String menuName,String hotelName) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.menuName =menuName;
        this.hotelName = hotelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (obj instanceof SubMenu)
            return id == ((SubMenu)obj).getId();
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
