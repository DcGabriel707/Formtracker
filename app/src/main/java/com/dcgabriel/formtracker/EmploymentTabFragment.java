package com.dcgabriel.formtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dcgabriel.formtracker.data.FormsContract;
import com.dcgabriel.formtracker.data.FormsDBHelper;

import java.util.ArrayList;

public class EmploymentTabFragment extends TabFragment {

    private static final String TAG = "EmploymentTabFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        formType = FormsContract.FormEntryTable.FORM_TYPE_EMPLOYMENT;
        childContext = EmploymentTabFragment.this.getActivity();
        super.onCreate(savedInstanceState);

    }


    @Override
    protected View getFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employment_fragment, parent, false);
        return view;
    }

}


//todo when creating the view application xml, use material theme textview to display the details. use small colored cardview to display each required documents??

