package com.dcgabriel.formtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcgabriel.formtracker.data.FormsContract;

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



