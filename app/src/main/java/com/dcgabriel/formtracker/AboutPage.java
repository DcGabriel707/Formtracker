package com.dcgabriel.formtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AboutPage extends AppCompatActivity {

    CardView sendFeedbackCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendFeedbackCardView = findViewById(R.id.sendFeedbackCardView);
        sendFeedbackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutPage.this, "sending", Toast.LENGTH_SHORT).show();
                Intent sendFeedback = new Intent(Intent.ACTION_SENDTO);
                sendFeedback.setData(Uri.parse("mailto:"));
                sendFeedback.putExtra(Intent.EXTRA_EMAIL, "gcarapplications@gmail.com");
                sendFeedback.putExtra(Intent.EXTRA_SUBJECT, "Axcept feedback");
                startActivity(sendFeedback);
            }
        });
    }


}
