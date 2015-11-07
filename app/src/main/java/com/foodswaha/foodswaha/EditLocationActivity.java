package com.foodswaha.foodswaha;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class EditLocationActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 1;
    public LocationAdapter<String> adapter ;
    public List<String> areas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final EditText area = (EditText) findViewById(R.id.areaSearch);
        final Button clear = (Button) findViewById(R.id.clearArea);
        final Button voice = (Button) findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area.setText("");
                clear.setVisibility(View.GONE);
                voice.setVisibility(View.VISIBLE);
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
                    voice.setVisibility(View.GONE);
                    adapter.getFilter().filter(s);
                } else {
                    clear.setVisibility(View.GONE);
                    voice.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        displayAreas();
    }

    private void displayAreas() {
        ListView areaList = (ListView) findViewById(R.id.areaList);
        areaList.setAdapter(getAreaAdapter());
    }

    public ArrayAdapter getAreaAdapter() {
         adapter = new LocationAdapter<String>(this,
                 R.layout.activity_display_hotel_menu_item,areas
                );
        return adapter;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // here the string converted from your voice
                    String converted_text = result.get(0);
                    //adapter.getFilter().filter(converted_text);
                    EditText area = (EditText) findViewById(R.id.areaSearch);
                    area.setText(converted_text);
                }
                break;
            }

        }
    }


}
