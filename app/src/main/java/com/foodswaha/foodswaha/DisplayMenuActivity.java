package com.foodswaha.foodswaha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayMenuActivity extends AppCompatActivity {

    private static final String TAG = "DisplayMenuActivity";
    private static MenuAdapter.Holder holder;
    private static HashMap<String, List<SubMenu>> menuToSubMenuMap;
    private static List menuList;
    private Cart cartInstance = Cart.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final TextView hotelName = (TextView) findViewById(R.id.hotelNameText);
        final TextView min = (TextView) findViewById(R.id.min);
        final TextView delFee = (TextView) findViewById(R.id.delFee);
        final TextView delTime = (TextView) findViewById(R.id.delTime);
        hotelName.setText(HotelsFragment.getHolder().hotelName.getText());
        min.setText("min: " + HotelsFragment.getHolder().hotelMinOrder.getText());
        delTime.setText(" time" + HotelsFragment.getHolder().hotelDTime.getText());
        delFee.setText(" fee" + HotelsFragment.getHolder().hotelDFee.getText());

        final View cartRelativeLayOut = findViewById(R.id.cartRelativeLayOut);
        ((TextView) cartRelativeLayOut.findViewById(R.id.cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDisplayCart = new Intent(DisplayMenuActivity.this, DisplayCartActivity.class);
                gotoDisplayCart.putExtra("from", "menu");
                startActivity(gotoDisplayCart);
            }
        });

        showCartIfAvailable();
        displayHotelMenu();
    }

    private void displayHotelMenu() {

        String hotelName = HotelsFragment.getHolder().hotelName.getText().toString();
        String menuString = getIntent().getStringExtra("menuData");

        JSONObject menuJSONObject = null;
        try {
            menuJSONObject = new JSONObject(menuString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray hotelMenuJSONArray = menuJSONObject.optJSONArray("categories");
            Log.e(TAG, " received hotelMenuJSONArray from server as " + hotelMenuJSONArray);
            if (hotelMenuJSONArray != null) {

                if (menuToSubMenuMap == null) {
                    menuToSubMenuMap = new HashMap<String, List<SubMenu>>();
                    menuList = new ArrayList<Menu>();
                    String menuName;
                    SubMenu subMenuObject;
                    List subMenuList;
                    for (int i = 0; i < hotelMenuJSONArray.length(); i++) {
                        subMenuList = new ArrayList<SubMenu>();
                        menuName = hotelMenuJSONArray.optString(i);
                        JSONArray subMenuJSONArray = menuJSONObject.optJSONArray(menuName);

                        if (subMenuJSONArray != null) {
                            for (int j = 0; j < subMenuJSONArray.length(); j++) {

                                JSONObject subMenuJSONObject = subMenuJSONArray.optJSONObject(j);
                                subMenuObject = new SubMenu(
                                        subMenuJSONObject.optInt("id"),
                                        subMenuJSONObject.optString("itemname"),
                                        subMenuJSONObject.optString("cost"), 0,
                                        menuName,hotelName

                                );
                                subMenuList.add(subMenuObject);
                            }

                            menuList.add(new Menu(menuName));
                            menuToSubMenuMap.put(menuName, subMenuList);
                        }

                    }
                }
                if (menuList != null) {
                    ListView menuListView = (ListView) findViewById(R.id.menuList);
                    menuListView.setAdapter(new MenuAdapter(this,
                            R.layout.activity_display_menu_item, menuList));
                    menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            holder = (MenuAdapter.Holder) view.getTag();
                            Intent displayHotelMenuIntent = new Intent(getBaseContext(), DisplaySubMenuActivity.class);
                            startActivity(displayHotelMenuIntent);

                        }
                    });
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MenuAdapter.Holder getHolder() {
        return holder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCartIfAvailable();
    }

    private void showCartIfAvailable() {
        final View cartRelativeLayOut = findViewById(R.id.cartRelativeLayOut);
        if (cartInstance.getCountOfItems() > 0) {
            cartRelativeLayOut.setVisibility(View.VISIBLE);
            ((TextView) cartRelativeLayOut.findViewById(R.id.cart)).setText(String.valueOf(cartInstance.getCountOfItems()));
            ((TextView) cartRelativeLayOut.findViewById(R.id.total)).setText(String.valueOf(cartInstance.getTotalBill()));
        } else {
            cartRelativeLayOut.setVisibility(View.GONE);
        }
    }

    public static HashMap<String, List<SubMenu>> getMenuToSubMenuMap() {
        return menuToSubMenuMap;
    }

    public static void setMenuToSubMenuMap(HashMap<String, List<SubMenu>> menuToSubMenuMap) {
        DisplayMenuActivity.menuToSubMenuMap = menuToSubMenuMap;
    }
}
