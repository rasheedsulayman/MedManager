package com.r4sh33d.medmanager.datepickers;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.r4sh33d.medmanager.addmedication.AddMedicationFragment;

import java.util.Calendar;

public  class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static  String IS_START_DATE_ARGS = "is_start_date";

    public static TimePickerFragment newInstance(boolean isStartTime) {
        Bundle args = new Bundle();
        args.putBoolean(IS_START_DATE_ARGS , isStartTime);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((AddMedicationFragment)getTargetFragment()).
                onTimeSet(getArguments().getBoolean(IS_START_DATE_ARGS), view,hourOfDay,minute);
        // Do something with the time chosen by the user
    }


}
