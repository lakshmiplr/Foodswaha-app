package com.foodswaha.foodswaha;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;


public class EditLocationActivity extends AppCompatActivity {

    public LocationAdapter<String> adapter ;
    public List<String> areas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        ListView areaList = (ListView) findViewById(R.id.areaList);
        areaList.setAdapter(getAreaAdapter());
        final EditText area = (EditText) findViewById(R.id.areaSearch);
        final Button clear = (Button) findViewById(R.id.clearArea);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area.setText("");
                clear.setVisibility(View.GONE);
            }
        });
        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!area.getText().toString().equals("")) {
                    clear.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(s);
                } else {
                    clear.setVisibility(View.GONE);
                    adapter.getFilter().filter(" ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayAdapter getAreaAdapter() {
         adapter = new LocationAdapter<String>(this,
                 R.layout.activity_display_hotel_menu_item,areas
                );
        return adapter;
    }




}
