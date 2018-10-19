package com.dcgabriel.formtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {

    private Spinner dateFormatSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateFormatSpinner = findViewById(R.id.dateFormatSpinner);
        ArrayAdapter<String> dateFormatAdapter = new ArrayAdapter<>(Settings.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.dateFormatList));
        dateFormatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateFormatSpinner.setAdapter(dateFormatAdapter);
        dateFormatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) { //if mm/dd/yyyy is selected

                } else if (position == 1) { //if dd/mm/yyyy is selected

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
