package com.foodswaha.foodswaha;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayCartActivity extends AppCompatActivity {

    Cart cartInstance = Cart.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ((TextView)findViewById(R.id.title)).setText("Cart");

        dispalycart();
    }
    private void dispalycart(){

        ((TextView)findViewById(R.id.cart)).setText(String.valueOf(cartInstance.getCountOfItems()));
        ((TextView)findViewById(R.id.cart)).setTypeface(null, Typeface.BOLD);
        ((TextView)findViewById(R.id.total)).setText(String.valueOf(cartInstance.getTotalBill()));

        final ListView cartItemsListView = (ListView)findViewById(R.id.cart_items);
        cartItemsListView.setAdapter(new CartAdapter(DisplayCartActivity.this,
                R.layout.activity_display_cart_items, cartInstance.getAllCartItems()));

        ImageButton home = (ImageButton)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartInstance.getCountOfItems() > 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            DisplayCartActivity.this);
                    alertDialogBuilder
                            .setMessage("Clear Cart and go to hotels?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    cartInstance.clearCart();
                                    Intent homeIntent = new Intent(DisplayCartActivity.this, DisplayHotelsActivity.class);
                                    homeIntent.putExtra("hotelData", DisplayHotelsActivity.getHotelDataJSONString());
                                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homeIntent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Intent homeIntent = new Intent(DisplayCartActivity.this, DisplayHotelsActivity.class);
                    homeIntent.putExtra("hotelData", DisplayHotelsActivity.getHotelDataJSONString());
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            }
        });

        TextView clearCart = (TextView)findViewById(R.id.cart);
        final View emptyCart = findViewById(R.id.emptyCart);
        final View cartRelativeLayOut = findViewById(R.id.cartRelativeLayOut);
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DisplayCartActivity.this);

                alertDialogBuilder
                        .setMessage("Clear Cart?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cartInstance.clearCart();
                                cartItemsListView.setVisibility(View.GONE);
                                emptyCart.setVisibility(View.VISIBLE);
                                cartRelativeLayOut.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        final View chooseDeliveryType = findViewById(R.id.checkout);
        chooseDeliveryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(DisplayCartActivity.this, ChooseDeliveryTypeActivity.class);
                startActivity(loginIntent);
            }
        });

    }

}
