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
    int count = 0;

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
                sendFeedback.putExtra(Intent.EXTRA_EMAIL, new String[]{"gcarapplications@gmail.com"});
                sendFeedback.putExtra(Intent.EXTRA_SUBJECT, "Axcept feedback");
                startActivity(sendFeedback);
            }
        });
    }

    public void moreApps(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
        if (intent != null) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Gabriel+Carmen"));
            startActivity(intent);
        }
    }

    public void nothingUnusual(View view) {
        count++;
        if (count == 10) {
            Toast.makeText(this, "10", Toast.LENGTH_SHORT).show();
        }
        if (count == 20) {
            Toast.makeText(this, "Hooray! You have found it. Now, send a feedback and tell us", Toast.LENGTH_SHORT).show();
        }
        if (count == 30) {
            Toast.makeText(this, "created by Gabriel Carmen", Toast.LENGTH_SHORT).show();
        }
        if (count == 45) {
            Toast.makeText(this, "You can stop now", Toast.LENGTH_SHORT).show();
        }

    }

}
