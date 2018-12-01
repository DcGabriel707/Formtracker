package com.dcgabriel.formtracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class FormsContract {

    public static final String CONTENT_AUTHORITY = "com.dcgabriel.formtracker"; //usually the package name.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FORMS = "forms"; //same as table name??
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FORMS);


    public static final class FormEntryTable implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FORMS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORMS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FORMS;
        public static final String TABLE_NAME = "forms";

        public static final String _ID = "_id";
        public static final String COLUMN_TYPE = "form_type";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMPANY = "company";  // used for company in jobs tab and scholarship tab, location for college tab

        public static final String COLUMN_DEADLINE = "deadline";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_DATE_SUBMITTED = "date_submitted"; //option greyed out if status is unsubmitted

        public static final String COLUMN_REQUIREMENTS = "requirements";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOGIN_USERNAME = "login_username";
        public static final String COLUMN_LOGIN_PASSWORD = "login_password";
        public static final String COLUMN_ESSAY_STATUS = "essay_status"; // used for college tab and scholarship tab.
        public static final String COLUMN_TODO = "todo";
        public static final String COLUMN_NOTES = "other_notes";

        public static final String COLUMN_LOCATION = "location"; // should be used in every tab. rename to address
        public static final String COLUMN_WEBSITE = "website";
        public static final String COLUMN_CONTACT_EMAIL = "contact_email";
        public static final String COLUMN_CONTACT_NUMBER = "contact_number";

        //exclusive to scholarship tab
        public static final String COLUMN_SCHOLARSHIP_AWARD = "scholarship_award_amount";
        public static final String COLUMN_SCHOLARSHIP_TYPE = "scholarship_type"; //unnecessary?

        //exclusive to job tab
        public static final String COLUMN_JOB_TYPE = "job_type";
        public static final String COLUMN_JOB_SALARY = "job_salary";
        public static final String COLUMN_JOB_POST_DATE = "job_post_date";

        //exclusive to college tab
        public static final String COLUMN_COLLEGE_AVE_COST = "college_ave_cost";

        //extra columns reserved for future updates
        public static final String COLUMN_EXTRA_ONE = "extra1";
        public static final String COLUMN_EXTRA_TWO = "extra2";
        public static final String COLUMN_EXTRA_THREE = "extra3";
        public static final String COLUMN_EXTRA_FOUR = "extra4";
        public static final String COLUMN_EXTRA_FIVE = "extra5";

        //other values
        public static final String FORM_TYPE_SCHOLARSHIP = "Scholarship";
        public static final String FORM_TYPE_COLLEGE = "College";
        public static final String FORM_TYPE_EMPLOYMENT = "Employment";
        public static final String FORM_TYPE_OTHERS = "Others";

        public static final int JOB_TYPE_INTERNSHIP = 0;
        public static final int JOB_TYPE_PART_TIME = 1;
        public static final int JOB_TYPE_FULL_TIME = 2;


        public static final String STATUS_INCOMPLETE = "Incomplete";
        public static final String STATUS_SUBMITTED = "Submitted";
        public static final String STATUS_REJECTED = "Rejected";
        public static final String STATUS_ACCEPTED = "Accepted";

        //todo create a projection string  that will use every field for use in querying every fields(eg. FormEntryTable.COLUMN_NAME, FormEntryTable.COLUMN_COMPANY, FormEntryTable.COLUMN_SCHOLARSHIP_AWARD, FormEntryTable.COLUMN_STATUS...)


    }
}
