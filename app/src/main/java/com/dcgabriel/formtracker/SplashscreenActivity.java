package com.dcgabriel.formtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashscreenActivity extends AppCompatActivity {

    private String TAG = "SplashscreenActivity";
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ***********");
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }
}
