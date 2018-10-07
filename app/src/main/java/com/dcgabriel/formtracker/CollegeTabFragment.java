package com.dcgabriel.formtracker;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;
import com.dcgabriel.formtracker.data.FormsDBHelper;

public class CollegeTabFragment extends TabFragment {
    private final String TAG = "CollegeTabFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        formType = FormEntryTable.FORM_TYPE_COLLEGE;
        childContext = CollegeTabFragment.this.getActivity();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected View getFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.college_fragment, parent, false);
        return view;
    }
}
