package com.dcgabriel.formtracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyCalendarDialog {
    private Context context;
    private String dateString;

    private DatePickerDialog datePickerDialog;
    private String TAG = "MyCalendarDialog";

    MyCalendarDialog(Context mContext, CardView mCardView, String mDateString, final TextView mDateTextView, Uri mCurrentUri, DatePickerDialog.OnDateSetListener dateListener, SimpleDateFormat simpleDateFormat) {
        context = mContext;
        dateString = mDateString;
        Calendar calendar = Calendar.getInstance();

        //if the entry is being updated and there is a date
        if (mCurrentUri != null && dateString != null) {//todo fix, enhance
            try {
                Date date = simpleDateFormat.parse(dateString);
                calendar.setTime(date);
                datePickerDialog = new DatePickerDialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else { //if there is no deadline.
            datePickerDialog = new DatePickerDialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog, dateListener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        mCardView.setOnClickListener(new View.OnClickListener() {
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
