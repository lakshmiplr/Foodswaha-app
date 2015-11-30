package com.foodswaha.foodswaha;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayOrderDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        OrderAdapter.Holder holder = OrdersFragment.getHolder();

        TextView status = (TextView) findViewById(R.id.status);
        TextView deliveryType = (TextView) findViewById(R.id.deliveryTypeText);
        TextView address = (TextView) findViewById(R.id.address);

        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(holder.total.getText());

        status.setText("Order Status : "+holder.status.toUpperCase());
        deliveryType.setText("Order Type : "+holder.deliveryType.toUpperCase());
        address.setText(holder.address);

        OrderItemAdapter adapter = new OrderItemAdapter(DisplayOrderDetails.this,
                R.layout.activity_display_order_item_details, holder.orderItemList);
        ListView orderItemDetailsListView = (ListView)findViewById(R.id.orderItems);

        orderItemDetailsListView.setAdapter(adapter);

    }

}
