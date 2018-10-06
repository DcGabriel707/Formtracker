package com.dcgabriel.formtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcgabriel.formtracker.data.FormsContract.FormEntryTable;

import java.util.ArrayList;


public class ScholarshipTabFragment extends TabFragment {
    private final String TAG = "ScholarshipTabFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        formType = FormEntryTable.FORM_TYPE_SCHOLARSHIP;
        childContext = ScholarshipTabFragment.this.getActivity();
        super.onCreate(savedInstanceState);

    }


    @Override
    protected View getFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scholarship_fragment, parent, false);
        return view;
    }











    //todo make the status CardView in the entry CardView a spinner. This would make it easier for users to update.
    //todo backgroundTint only works with 5.1+. Replace with app:backgroundTint?
    //todo if date is somehow empty, stop the app from crashing 9-26-18
    //todo use bottom sheet for sorting. add icons
    //todo use inline menu for sorting

}
