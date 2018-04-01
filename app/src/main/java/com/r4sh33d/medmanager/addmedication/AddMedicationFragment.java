package com.r4sh33d.medmanager.addmedication;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.datepickers.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMedicationFragment extends Fragment implements AddMedicationContract.View {
    private static String TAG = AddMedicationFragment.class.getSimpleName();
    @BindView(R.id.medication_name_edit_text)
    EditText medicationName;
    @BindView(R.id.medication_quantity_edit_text)
    EditText medicationQuantityEditText;
    @BindView(R.id.starting_date_value)
    TextView startingDateValue;
    @BindView(R.id.ending_date_value)
    TextView endingDateValue;

    Calendar startingDateCalender, endingDateCalender;


    public AddMedicationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medication, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startingDateCalender = Calendar.getInstance();
        endingDateCalender = Calendar.getInstance();
        setCalenderDefault(startingDateCalender);
        setCalenderDefault(endingDateCalender);
    }


    public void setCalenderDefault(Calendar calendar) {
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.HOUR_OF_DAY , 0);//UTC offset
    }

    @OnClick(R.id.starting_date_value)
    void onClickStartingDate() {
        DialogFragment newFragment = DatePickerFragment.newInstance(true);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.starting_time_value)
    void onClickStartingTimeValue() {

    }


    @OnClick(R.id.ending_date_value)
    void onClickEndingDateValue() {
        DialogFragment newFragment = DatePickerFragment.newInstance(false);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public void moveToNextStep() { }


    @OnClick(R.id.button)
    void onClickSave() {
        Log.d(TAG , "The difference in time Milliseconds is " +
                (endingDateCalender.getTimeInMillis() - startingDateCalender.getTimeInMillis()));
    }

    public void onDateSet(boolean isStartDate, DatePicker view, int year, int month, int dayOfMonth) {
        if (isStartDate) {
            Log.d(TAG, "Initially " + startingDateCalender.getTimeInMillis() + "");
            startingDateCalender.set(year, month, dayOfMonth);
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            startingDateValue.setText(month_date.format(startingDateCalender.getTime()));
            month_date.applyPattern("dd MMM yyyy hh:mm:ss:SS");
            Log.d(TAG, month_date.format(startingDateCalender.getTime()));
            Log.d(TAG, startingDateCalender.getTimeInMillis() + "");
        } else {
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            Log.d(TAG , "Before ");
            month_date.applyPattern("dd MMM yyyy HH:mm:ss:SS");
            Log.d(TAG, month_date.format(endingDateCalender.getTime()));
            endingDateCalender.set(year, month, dayOfMonth);
            endingDateValue.setText(month_date.format(endingDateCalender.getTime()));
            month_date.applyPattern("dd MMM yyyy HH:mm:ss:SS");
            Log.d(TAG, month_date.format(endingDateCalender.getTime()));
            Log.d(TAG, endingDateCalender.getTimeInMillis() + "");
            endingDateCalender.add(Calendar.HOUR_OF_DAY , 24);
            Log.d(TAG , "After Addition");
            month_date.applyPattern("dd MMM yyyy HH:mm:ss:SS");
            Log.d(TAG, month_date.format(endingDateCalender.getTime()));
            Log.d(TAG, endingDateCalender.getTimeInMillis() + "");
        }
    }

    public void onTimeSet(boolean isStartTime, TimePicker view, int year, int month) {

    }
}
