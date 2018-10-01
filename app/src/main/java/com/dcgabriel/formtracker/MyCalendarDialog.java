package com.dcgabriel.formtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyCalendarDialog {
    private Context context;
    private CardView cardView;
    private String dateString;
    private TextView dateTextView;
    private Uri currentUri;

    //private DatePickerDialog.OnDateSetListener dateListener;
    private DatePickerDialog datePickerDialog;
    private String TAG = "MyCalendarDialog";

    MyCalendarDialog(Context mContext, CardView mCardView,  String mDateString, final TextView mDateTextView, Uri mCurrentUri, DatePickerDialog.OnDateSetListener dateListener) {
        context = mContext;
        cardView = mCardView;
        dateString = mDateString;
        dateTextView = mDateTextView;
        currentUri = mCurrentUri;


        Calendar calendar = Calendar.getInstance();

        //if the entry is being updated and there is a date
        if (currentUri != null && dateString != null) {//todo fix, enhance
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date date = simpleDateFormat.parse(dateString);
                calendar.setTime(date);
                datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else { //if there is no deadline.
            datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog, dateListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.getWindow();
                datePickerDialog.show();
                Log.d(TAG, "cardview onClick: ");
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "setDateSubmitted bottom: ************///////////////// deadlineDateString=" + dateString);
    }

    public String getDateString() {
        return dateString;
    }
}
