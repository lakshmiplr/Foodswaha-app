package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class DisplaySubMenuActivity extends AppCompatActivity {

    private Cart cartInstance = Cart.getInstance();
    private static HashMap menuToSubMenuMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_submenu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        displaySubMenu();
    }

    private void displaySubMenu(){
        final TextView hotelName = (TextView) findViewById(R.id.hotelName);
        hotelName.setText(HotelsFragment.getHolder().hotelName.getText());
        final TextView menuNameTextView = (TextView) findViewById(R.id.menuName);
        String menuName = DisplayMenuActivity.getHolder().menuName.getText().toString();
        menuNameTextView.setText(menuName);
        showCartIfAvailable();
        if(menuToSubMenuMap==null){
            menuToSubMenuMap = DisplayMenuActivity.getMenuToSubMenuMap();
        }

        List<SubMenu> subMenuList = (List<SubMenu>) menuToSubMenuMap.get(menuName);

        ListView subMenuListView = (ListView)findViewById(R.id.subMenuList);
        subMenuListView.setAdapter(new SubMenuAdapter(this,
                R.layout.activity_display_submenu_item, subMenuList));


        View cartRelativeLayOut = findViewById(R.id.cartRelativeLayOut);
        ((TextView) cartRelativeLayOut.findViewById(R.id.cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDisplayCart = new Intent(DisplaySubMenuActivity.this, DisplayCartActivity.class);
                gotoDisplayCart.putExtra("from", "submenu");
                startActivity(gotoDisplayCart);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySubMenu();
        showCartIfAvailable();
    }

    private void showCartIfAvailable() {
        View cartRelativeLayOut = findViewById(R.id.cartRelativeLayOut);
        if (cartInstance.getCountOfItems() > 0) {
            cartRelativeLayOut.setVisibility(View.VISIBLE);
            ((TextView) cartRelativeLayOut.findViewById(R.id.cart)).setText(String.valueOf(cartInstance.getCountOfItems()));
            ((TextView) cartRelativeLayOut.findViewById(R.id.total)).setText(String.valueOf(cartInstance.getTotalBill()));
        } else {
            cartRelativeLayOut.setVisibility(View.GONE);
        }
    }
}
