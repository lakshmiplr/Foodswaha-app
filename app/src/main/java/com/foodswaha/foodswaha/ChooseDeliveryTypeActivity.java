package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ChooseDeliveryTypeActivity extends AppCompatActivity {

    Cart cartInstance = Cart.getInstance();
    private static String cdt ="delivery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_delivery_type);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        TextView dTime = (TextView) findViewById(R.id.dtime);
        TextView dFee = (TextView) findViewById(R.id.dfee);

        dTime.setText(HotelsFragment.getHolder().hotelDTime.getText().toString().replace(":", ""));
        String dfeeString = HotelsFragment.getHolder().hotelDFee.getText().toString().replace(":", "");
        final int dfeeInt = Integer.parseInt(dfeeString.replace("Rs", "").trim());
        dFee.setText(dfeeString);
        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                if (radioButton.getText().equals("delivery")) {
                    total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));
                    cdt ="delivery";
                } else if (radioButton.getText().equals("pickup")) {
                    total.setText(String.valueOf(cartInstance.getTotalBill()));
                    cdt ="pickup";
                }
            }
        });

        final View gotoAddress = findViewById(R.id.checkout);
        gotoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ChooseDeliveryTypeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }


    public static String getCdt() {
        return cdt;
    }
}
