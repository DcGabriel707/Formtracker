package com.dcgabriel.formtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private Spinner dateFormatSpinner;
    private String dateFormat;

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
                    dateFormat = getString(R.string.MMddyyyy);
                    saveSharedPreference();
                    loadSharedPreference(); //for debugging
                } else if (position == 1) { //if dd/mm/yyyy is selected
                    dateFormat = getString(R.string.ddMMyyyy);
                    saveSharedPreference();
                    loadSharedPreference(); //dddddddddddddddddddddddddddddddddddddddfor debugging
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadSharedPreference();
    }

    private void saveSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DateFormat", dateFormat);
        editor.apply();
        Toast.makeText(this, "sharedPreference saved", Toast.LENGTH_SHORT).show();
    }

    private void loadSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String loadedDateFormat = sharedPreferences.getString("DateFormat", "N/A");

        Toast.makeText(this, loadedDateFormat, Toast.LENGTH_SHORT).show();

        if (loadedDateFormat.equals(getString(R.string.MMddyyyy)))
            dateFormatSpinner.setSelection(0);
        else
            dateFormatSpinner.setSelection(1);
    }

}
