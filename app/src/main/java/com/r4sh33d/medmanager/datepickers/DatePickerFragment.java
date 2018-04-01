package com.r4sh33d.medmanager.datepickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.r4sh33d.medmanager.addmedication.AddMedicationFragment;

import java.util.Calendar;

public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static  String IS_START_DATE_ARGS = "is_start_date";

    public static DatePickerFragment newInstance(boolean isStartDate) {
        Bundle args = new Bundle();
        args.putBoolean(IS_START_DATE_ARGS , isStartDate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((AddMedicationFragment)getTargetFragment()).
                onDateSet(getArguments().getBoolean(IS_START_DATE_ARGS), view,year,month, day);
        // Do something with the date chosen by the user
    }
}
