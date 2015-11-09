package com.foodswaha.foodswaha;

import java.util.List;

/**
 * Created by pharshar on 11/1/2015.
 */
public class Menu {

    String name;
    List HotelMenuItemSub;

    public Menu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
