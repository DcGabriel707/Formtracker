package com.dcgabriel.formtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class AddEntryActivity extends AppCompatActivity {
    private static String TAG = "AddEntry";
    private EditText nameEditText;
    private EditText companyEditText;
    private Spinner statusSpinner;
    private TextView deadlineDateTextView;
    private TextView dateSubmittedTextView;
    private EditText requirementEditText;
    private EditText descriptionEditText;
    private EditText todoEditText;
    private EditText notesEditText;
    private EditText websiteEditText;
    private EditText locationEditText;
    private EditText contactEmailEditText;
    private EditText contactNumberEditText;

    private EditText awardEditText;
    private EditText aveCostEditText;
    private EditText jobTypeEditText;
    private EditText salaryEditText;

    private DatePickerDialog.OnDateSetListener deadlineDateListener;
    private DatePickerDialog.OnDateSetListener dateSubmittedListener;
    protected String jobPostDateString; //this should be the string used when modifying/retrieving the date from the database
    private String jobPostDatePreferredFormat;
    private String deadlineDateString; //this should be the string used when modifying/retrieving the date from the database
    private String deadlinePreferredFormat; // used for displaying deadline string. uses the preferred date format set from the settings
    private String dateSubmittedString; //this should be the string used when modifying/retrieving the date from the database
    private String dateSubmissionPreferredFormat; // used for displaying deadline string. uses the preferred date format set from the settings
    protected TextView jobPostDateTextView;

    private DatePickerDialog.OnDateSetListener jobPostDateListener;

    protected Context childContext;
    protected String formType;
    private Intent intent;
    private Uri currentUri;
    private ContentValues values;
    private SimpleDateFormat simpleDateFormat; // default date format. All dates stored in the database is in this format (MM/dd/yyyy)
    private SimpleDateFormat preferredDateFormat;  // preferred date format set from the settings. This is the one used when dates are printed
    private String preferredDateFormatString;
    private String myDateFormat;

    protected abstract int getLayoutId();

    protected abstract void initializeFromChildActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initializeFromChildActivity();
        handleFindViewByIds();


        //handles the status spinner. string arrays found in strings.xml
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(childContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.statusList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        //gets the intent from RecyclerViewAdapter
        intent = getIntent();
        currentUri = intent.getData();

        //handle status spinner and date submitted
        handleStatusSpinner();

        //retrieves the string format from sharedPreference
        //preferredDateFormat must be initialized before fillExistingData()
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferredDateFormatString = sharedPreferences.getString("DateFormat", "");

        //sets the date format
        //maybe just change the format when displaying the dates, not when saving the dates into the database
        simpleDateFormat = new SimpleDateFormat(getString(R.string.MMddyyyy), Locale.US); // somehow setting to a different order does not work.
        if (preferredDateFormatString.equals(getString(R.string.MMddyyyy))) {
            preferredDateFormat = simpleDateFormat;
        } else if (preferredDateFormatString.equals(getString(R.string.ddMMyyyy))) {
            preferredDateFormat = new SimpleDateFormat(preferredDateFormatString, Locale.US); // somehow setting to a different order does not work.
        }

        //if an entry is being edited, or when the user clicked on the cardView. Not working if i put these findviewbyids on another method
        FloatingActionButton fabAdd = findViewById(R.id.add_entry_add_fab);
        CardView deleteButton = findViewById(R.id.deleteButton);
        final CardView shareButton = findViewById(R.id.shareButton);
        if (currentUri != null) {
            //fills the empty fields with existing data
            fillExistingData();

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteEntryButton();
                }
            });
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateEntryButton();
                }
            });
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareEntryButton();
                }
            });

        } else {
            deleteButton.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.INVISIBLE);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addButton();
                }
            });
        }


        //handles the deadline date. called again to update with existing date
        setDeadlineDate();
        //handles the date submitted
        setDateSubmitted();

        if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            setJobPostDate();
        }

    }

    protected void handleFindViewByIds() {
        nameEditText = findViewById(R.id.nameEditText);
        companyEditText = findViewById(R.id.companyEditText);
        statusSpinner = findViewById(R.id.statusSpinner);
        deadlineDateTextView = findViewById(R.id.deadlineDateText);
        dateSubmittedTextView = findViewById(R.id.dateSubmittedText);
        requirementEditText = findViewById(R.id.requirementEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        todoEditText = findViewById(R.id.todoEditText);
        notesEditText = findViewById(R.id.notesEditText);
        websiteEditText = findViewById(R.id.websiteEditText);
        locationEditText = findViewById(R.id.locationEditText);
        contactEmailEditText = findViewById(R.id.emailEditText);
        contactNumberEditText = findViewById(R.id.numberEditText);

        if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            awardEditText = findViewById(R.id.scholarshipAwardEditText);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_COLLEGE)) {
            aveCostEditText = findViewById(R.id.collegeAveCost);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            jobTypeEditText = findViewById(R.id.jobTypeEditText);
            salaryEditText = findViewById(R.id.salaryEditText);
            jobPostDateTextView = findViewById(R.id.jobPostDateTextView);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_OTHERS)) {

        }
    }

    //retrieves the data from the editTexts and other fields
    private void retrieveData() {
        Log.d(TAG, "retrieveData: ***************");

        String name = nameEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString().trim();
        String requirements = requirementEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String todo = todoEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String website = websiteEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String email = contactEmailEditText.getText().toString().trim();
        String phone = contactNumberEditText.getText().toString().trim();

        values = new ContentValues();
        values.put(FormsContract.FormEntryTable.COLUMN_NAME, name);
        values.put(FormsContract.FormEntryTable.COLUMN_STATUS, status);
        values.put(FormsContract.FormEntryTable.COLUMN_DEADLINE, deadlineDateString);
        values.put(FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED, dateSubmittedString);
        values.put(FormsContract.FormEntryTable.COLUMN_REQUIREMENTS, requirements);
        values.put(FormsContract.FormEntryTable.COLUMN_DESCRIPTION, description);
        values.put(FormsContract.FormEntryTable.COLUMN_TODO, todo);
        values.put(FormsContract.FormEntryTable.COLUMN_NOTES, notes);
        values.put(FormsContract.FormEntryTable.COLUMN_WEBSITE, website);
        values.put(FormsContract.FormEntryTable.COLUMN_LOCATION, location);
        values.put(FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL, email);
        values.put(FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER, phone);

        if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
            String award = awardEditText.getText().toString().trim();
            String company = companyEditText.getText().toString().trim();

            values.put(FormsContract.FormEntryTable.COLUMN_COMPANY, company);
            values.put(FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD, award);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_COLLEGE)) {
            String aveCost = aveCostEditText.getText().toString().trim();
            values.put(FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST, aveCost);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_COLLEGE);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {
            String company = companyEditText.getText().toString().trim();
            String jobType = jobTypeEditText.getText().toString().trim();
            String salary = salaryEditText.getText().toString().trim();

            values.put(FormsContract.FormEntryTable.COLUMN_COMPANY, company);
            values.put(FormsContract.FormEntryTable.COLUMN_JOB_TYPE, jobType);
            values.put(FormsContract.FormEntryTable.COLUMN_JOB_SALARY, salary);
            values.put(FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE, jobPostDateString);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT);
        } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_OTHERS)) {
            String company = companyEditText.getText().toString().trim();

            values.put(FormsContract.FormEntryTable.COLUMN_COMPANY, company);
            values.put(FormsContract.FormEntryTable.COLUMN_TYPE, FormsContract.FormEntryTable.FORM_TYPE_OTHERS);
        }

        Log.d(TAG, "retrieveData: deadlineFinal = " + deadlineDateString + "////////////////////////////////////////////////////////////");
    }


    //change name to addEntryButton or doneButton
    //addButton shares an FAB with the update button.
    public void addButton() {
        Log.d(TAG, "addButton: called*************");

        //retrieves the data from the editTexts and other fields
        retrieveData();

        //FormsProvider will handle this??
        //FormsDBHelper formsDBHelper = new FormsDBHelper(this);
        //SQLiteDatabase db = formsDBHelper.getWritableDatabase();

        //instead of this long newRowId = db.insert(FormEntryTable.TABLE_NAME, null, values);
        Uri uri = getContentResolver().insert(FormsContract.FormEntryTable.CONTENT_URI, values);
        Log.d(TAG, "addButton: data inserted****************");

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        finish();
    }


    //share FAB with the addButton()
    private void updateEntryButton() {
        Log.d(TAG, "updateEntryButton: ***********************");
        int rowsUpdated = 0;

        //retrieves the data from the editTexts and other fields then store them into ContentValues
        retrieveData();

        // from https://developer.android.com/guide/topics/providers/content-provider-basics
        //Sets the updated value and updates the selected words.
        //values.putNull(UserDictionary.Words.LOCALE);
        //
        rowsUpdated = getContentResolver().update(currentUri, values, null, null);

        int pos = intent.getIntExtra("position", -999);

        Intent result = new Intent();
        result.putExtra("position", pos); //position not accurate. only for testign
        result.putExtra("action", 1); //specifies that the action is deleted not updated
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void deleteEntryButton() {
        //Burrowed from https://classroom.udacity.com/courses/ud845/lessons/9baef157-4f66-4513-b612-90c5f6975c21/concepts/5bcf3ea1-e2fe-4259-85d6-8f43b745bd35#
        // CHANGE***************************
        // Only perform the delete if this is an existing pet.
        if (currentUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(currentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(childContext, "Delete Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(childContext, "Delete Successful", Toast.LENGTH_SHORT).show();
            }
        }

        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        int pos = intent.getIntExtra("position", -999); //position received from RecyclerViewAdapter
        result.putExtra("position", pos);
        result.putExtra("action", 2); //specifies that the action is deleted not updated
        finish();
    }

    private void shareEntryButton() {
        Toast.makeText(childContext, "Share", Toast.LENGTH_SHORT).show();
        String shareText = getAllEntryInfo();


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    //checks if column value is empty, else, adds it into the message to be shared
    private String addText(String message, String title, String value) {
        if (value.equals("") || value == null) {
            return message;
        } else {
            return message.concat("\n" + title + ": " + value);
        }
    }

    String getAllEntryInfo() {
        String shareText = " ";
        //todo check projection if it is the most efficient
        String[] projection = {FormsContract.FormEntryTable.COLUMN_NAME, FormsContract.FormEntryTable.COLUMN_COMPANY, FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD,
                FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST,
                FormsContract.FormEntryTable.COLUMN_JOB_TYPE, FormsContract.FormEntryTable.COLUMN_JOB_SALARY, FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE,
                FormsContract.FormEntryTable.COLUMN_STATUS, FormsContract.FormEntryTable.COLUMN_DEADLINE, FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED, FormsContract.FormEntryTable.COLUMN_REQUIREMENTS,
                FormsContract.FormEntryTable.COLUMN_DESCRIPTION, FormsContract.FormEntryTable.COLUMN_TODO, FormsContract.FormEntryTable.COLUMN_NOTES,
                FormsContract.FormEntryTable.COLUMN_WEBSITE, FormsContract.FormEntryTable.COLUMN_LOCATION, FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL, FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER};

        String selection = FormsContract.FormEntryTable._ID + "=?";
        String[] selectionArgs = {intent.getStringExtra("_id")};

        Cursor cursor = childContext.getContentResolver().query(FormsContract.FormEntryTable.CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            shareText = addText(shareText, childContext.getString(R.string.name), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NAME)));
            shareText = addText(shareText, childContext.getString(R.string.company), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
            shareText = addText(shareText, childContext.getString(R.string.description), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DESCRIPTION)));
            shareText = addText(shareText, childContext.getString(R.string.requirements), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_REQUIREMENTS)));
            shareText = addText(shareText, childContext.getString(R.string.deadline), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DEADLINE)));
            shareText = addText(shareText, childContext.getString(R.string.todo), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_TODO)));
            shareText = addText(shareText, childContext.getString(R.string.notes), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NOTES)));
            shareText = addText(shareText, childContext.getString(R.string.website), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_WEBSITE)));
            shareText = addText(shareText, childContext.getString(R.string.address), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_LOCATION)));
            shareText = addText(shareText, childContext.getString(R.string.contactEmail), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL)));
            shareText = addText(shareText, childContext.getString(R.string.contact_phone), cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER)));
        }

        cursor.close();

        return shareText;
    }

    private void handleStatusSpinner() {
        Log.d(TAG, "handleStatusSpinner: **********************");
        final RelativeLayout dateSubmittedLayout = findViewById(R.id.dateSubmittedLayout);
        final LinearLayout outerLayout = findViewById(R.id.outerLayoutStatusDeadlineSubmitted);
        Log.d(TAG, "handleStatusSpinner: wdfawfwf-**********************---------");

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isRemoved = false;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "handleStatusSSpinner: onItemClick: **************************");

                if (((position == 1) || (position == 2) || (position == 3)) && isRemoved) {
                    Log.d(TAG, "handleStatusSpinner: onItemClick: add**************");
                    outerLayout.addView(dateSubmittedLayout);
                    isRemoved = false;
                } else if (position == 0) { //remove dateSubmitted views if the status is "incomplete
                    outerLayout.removeView(dateSubmittedLayout);
                    isRemoved = true;
                    dateSubmittedString = null;
                    dateSubmittedTextView.setText(R.string.choose_date);
                    Log.d(TAG, "handleStatusSpinner: onItemClick: remove*****************");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //turn remove dates into a single method
    public void removeDeadline(View view) {
        Toast.makeText(childContext, "Remove Deadline", Toast.LENGTH_SHORT).show();
        deadlineDateString = null;
        deadlineDateTextView.setText(R.string.choose_date);
    }

    public void removeDateSubmitted(View view) {
        Toast.makeText(childContext, "Remove Deadline", Toast.LENGTH_SHORT).show();
        dateSubmittedString = null;
        dateSubmittedTextView.setText(R.string.choose_date);
    }

    private void setDeadlineDate() {
        CardView deadlineCardView = findViewById(R.id.deadlineAddDateCard);
        deadlineDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {// no leading zeroes causes improper sorting
                m += 1;
                String dateWithoutLeadingZero = m + "/" + d + "/" + y;
                //dates will be set on the preferred format before printing
                deadlinePreferredFormat = toPreferredDateFormat(dateWithoutLeadingZero);
                try {
                    Date tDate = simpleDateFormat.parse(dateWithoutLeadingZero); //converts into "MM/dd/yyyy" format first. using the simpleDateFormat maintains the leading zeroes. Leading zeroes are used for sorting
                    deadlineDateString = simpleDateFormat.format(tDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(childContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                    System.exit(1);
                }
                deadlineDateTextView.setText(deadlinePreferredFormat);
            }
        };
        //I cannot make it work without using another class
        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(childContext, deadlineCardView, deadlineDateString,
                deadlineDateTextView, currentUri, deadlineDateListener, simpleDateFormat);
        deadlineDateString = myCalendarDialog.getDateString();
    }


    private void setDateSubmitted() {
        CardView dateSubmittedCardView = (CardView) findViewById(R.id.dateSubmittedCardView);
        dateSubmittedListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) { //no leading zeroes causes improper sorting
                m += 1;
                String dateWithoutLeadingZero = m + "/" + d + "/" + y;
                //dates will be set on the preferred format before printing
                dateSubmissionPreferredFormat = toPreferredDateFormat(dateWithoutLeadingZero);
                try {
                    Date tDate = simpleDateFormat.parse(dateWithoutLeadingZero); //converts into "MM/dd/yyyy" format first. maintains the leading zeroes
                    dateSubmittedString = simpleDateFormat.format(tDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(childContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                    System.exit(1);
                }
                Toast.makeText(childContext, dateSubmittedString, Toast.LENGTH_SHORT).show();
                dateSubmittedTextView.setText(dateSubmissionPreferredFormat);
            }
        };

        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(childContext, dateSubmittedCardView, dateSubmittedString, dateSubmittedTextView, currentUri, dateSubmittedListener, simpleDateFormat);
        dateSubmittedString = myCalendarDialog.getDateString();

    }

    private void setJobPostDate() {
        CardView jobPostDateCardView = (CardView) findViewById(R.id.jobPostDateCardView);
        jobPostDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) { // no leading zeroes causes improper sorting
                m += 1;
                String dateWithoutLeadingZero = m + "/" + d + "/" + y;
                //dates will be set on the preferred format before printing
                jobPostDatePreferredFormat = toPreferredDateFormat(dateWithoutLeadingZero);
                try {
                    Date tDate = simpleDateFormat.parse(dateWithoutLeadingZero); //converts into "MM/dd/yyyy" format first. maintains the leading zeroes
                    jobPostDateString = simpleDateFormat.format(tDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(childContext, "ParceException catched", Toast.LENGTH_SHORT).show();
                    System.exit(1);
                }
                Toast.makeText(childContext, jobPostDateString, Toast.LENGTH_SHORT).show();
                jobPostDateTextView.setText(jobPostDatePreferredFormat);
            }
        };

        MyCalendarDialog myCalendarDialog = new MyCalendarDialog(childContext, jobPostDateCardView, jobPostDateString, jobPostDateTextView, currentUri, jobPostDateListener, simpleDateFormat);
        jobPostDateString = myCalendarDialog.getDateString();

    }

    //converts date string into preferred date format. Must only be used when displaying date strings not when modifying database.
    private String toPreferredDateFormat(String unformattedDateString) {
        String formattedDate = "   ";
        try { // cannot find away to parse simpleDateFormat without using try catch
            Date date = simpleDateFormat.parse(unformattedDateString);
            formattedDate = preferredDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(childContext, "ParceException catched", Toast.LENGTH_SHORT).show();
            System.exit(1);
        }

        return formattedDate;

    }

    //fills the fields with existing data from the database?
    private void fillExistingData() {
        Log.d(TAG, "fillExistingData: ********************");

        //maybe add all columns here. Would it slow down anything?
        String[] projection = {FormsContract.FormEntryTable.COLUMN_NAME, FormsContract.FormEntryTable.COLUMN_COMPANY, FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD,
                FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST,
                FormsContract.FormEntryTable.COLUMN_JOB_TYPE, FormsContract.FormEntryTable.COLUMN_JOB_SALARY, FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE,
                FormsContract.FormEntryTable.COLUMN_STATUS, FormsContract.FormEntryTable.COLUMN_DEADLINE, FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED, FormsContract.FormEntryTable.COLUMN_REQUIREMENTS,
                FormsContract.FormEntryTable.COLUMN_DESCRIPTION, FormsContract.FormEntryTable.COLUMN_TODO, FormsContract.FormEntryTable.COLUMN_NOTES,
                FormsContract.FormEntryTable.COLUMN_WEBSITE, FormsContract.FormEntryTable.COLUMN_LOCATION, FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL, FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER};

        String selection = FormsContract.FormEntryTable._ID + "=?";
        String[] selectionArgs = {intent.getStringExtra("_id")};

        Log.d(TAG, "fillExistingData: before cursor*****************");
        Cursor cursor = childContext.getContentResolver().query(FormsContract.FormEntryTable.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) { //does it need to have cursor != null?
            deadlineDateString = cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DEADLINE));
            if (deadlineDateString != null) {
                Log.d(TAG, "fillExistingData: has deadline ****************///////////deadlineDateString=" + deadlineDateString);
                deadlineDateTextView.setText(toPreferredDateFormat(deadlineDateString));
            } else {
                Log.d(TAG, "fillExistingData: no deadlineDateString***** = " + deadlineDateString);
                deadlineDateTextView.setText(R.string.noDeadline);
            }

            dateSubmittedString = cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DATE_SUBMITTED));
            if (dateSubmittedString != null) {
                Log.d(TAG, "fillExistingData: has dateSubmitted ****************///////////dateSubmittedString=" + dateSubmittedString);
                dateSubmittedTextView.setText(toPreferredDateFormat(dateSubmittedString));
            } else {
                dateSubmittedTextView.setText(R.string.choose_date);
            }

            Log.d(TAG, "fillExistingData: before filling status********");
            switch (cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_STATUS))) { //fills in the status
                case FormsContract.FormEntryTable.STATUS_INCOMPLETE:
                    statusSpinner.setSelection(0);
                    break;
                case FormsContract.FormEntryTable.STATUS_SUBMITTED:
                    statusSpinner.setSelection(1);
                    break;
                case FormsContract.FormEntryTable.STATUS_ACCEPTED:
                    statusSpinner.setSelection(2);
                    break;
                case FormsContract.FormEntryTable.STATUS_REJECTED:
                    statusSpinner.setSelection(3);
                    break;
                default:
                    statusSpinner.setSelection(0);
                    break;
            }

            nameEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NAME)));
            requirementEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_REQUIREMENTS)));
            descriptionEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_DESCRIPTION)));
            todoEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_TODO)));
            notesEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_NOTES)));
            websiteEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_WEBSITE)));
            locationEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_LOCATION)));
            contactEmailEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_CONTACT_EMAIL)));
            contactNumberEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_CONTACT_NUMBER)));

            if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_SCHOLARSHIP)) {
                companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
                awardEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_SCHOLARSHIP_AWARD)));
            } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_COLLEGE)) {
                aveCostEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COLLEGE_AVE_COST)));
            } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT)) {

                jobPostDateString = cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_JOB_POST_DATE));
                if (jobPostDateString != null) {
                    Log.d(TAG, "fillExistingData: has deadline ****************///////////deadlineDateString=" + jobPostDateString);
                    jobPostDateTextView.setText(toPreferredDateFormat(jobPostDateString));
                } else {
                    Log.d(TAG, "fillExistingData: no deadlineDateString***** = " + jobPostDateString);
                    jobPostDateTextView.setText(R.string.choose_date);
                }
                companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
                jobTypeEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_JOB_TYPE)));
                salaryEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_JOB_SALARY)));
            } else if (formType.equals(FormsContract.FormEntryTable.FORM_TYPE_OTHERS)) {
                companyEditText.setText(cursor.getString(cursor.getColumnIndex(FormsContract.FormEntryTable.COLUMN_COMPANY)));
            }
        }

        cursor.close();
        Log.d(TAG, "fillExistingData: cursor closed*********************");
    }


    //no leading zeroes causes improper sorting
    //hide keyboard when clicked outside edittext
}
