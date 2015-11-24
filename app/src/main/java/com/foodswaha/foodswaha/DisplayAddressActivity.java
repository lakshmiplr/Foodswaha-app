package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayAddressActivity extends AppCompatActivity {

    Cart cartInstance = Cart.getInstance();
    private static List addressList = new ArrayList<Address>();
    private ArrayAdapter<Address> addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        String cdt = ChooseDeliveryTypeActivity.getCdt();
        //TextView emailTextView = (TextView)findViewById(R.id.emptyCart);
        //emailTextView.setText(email+"----"+cdt);
        buildAddressAdapter(LoginActivity.getAddressJSONObject());
        final View addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(DisplayAddressActivity.this, AddAddressActivity.class);
                addAddressIntent.putExtra("type","new");
                startActivity(addAddressIntent);
            }
        });
        String dfeeString = HotelsFragment.getHolder().hotelDFee.getText().toString().replace(":", "");
        final int dfeeInt = Integer.parseInt(dfeeString.replace("Rs", "").trim());
        final TextView total = ((TextView) findViewById(R.id.total));
        total.setText(String.valueOf(cartInstance.getTotalBill() + dfeeInt));

        final View gotoPayment = findViewById(R.id.checkout);
        gotoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPaymentIntent = new Intent(DisplayAddressActivity.this, PaymentActivity.class);
                startActivity(gotoPaymentIntent);
            }
        });

    }

    private void buildAddressAdapter(JSONObject response) {
        try{

            addressList.clear();
            JSONArray addressesJSONArray = response.getJSONArray("addresses");
                JSONObject address;
                for(int i = 0; i < addressesJSONArray.length(); i++){;
                    address = addressesJSONArray.getJSONObject(i);
                    if(DisplayHotelsActivity.getAREA().equals(address.optString("area"))){
                        addressList.add(
                                new Address(
                                        address.getString("flatNumber"),
                                        address.getString("streetDetails"),
                                        address.getString("area"),
                                        address.getString("city"),
                                        address.getString("landmark")
                                )
                        );
                    }

            }
            ListView addressListView = (ListView) findViewById(R.id.addressList);
            EditText mobile =(EditText)findViewById(R.id.mobile);
            mobile.setText(response.optString("mobile"));
            addressAdapter = new AddressAdapter(this,
                    R.layout.activity_display_address_item, addressList);
            addressListView.setAdapter(addressAdapter);

    } catch (JSONException e) {
            e.printStackTrace();
    }
    }

    public static List getAddressList() {
        return addressList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildAddressAdapter(LoginActivity.getAddressJSONObject());
    }
}