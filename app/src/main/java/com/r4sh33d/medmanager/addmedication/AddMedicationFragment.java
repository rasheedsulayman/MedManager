package com.r4sh33d.medmanager.addmedication;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.r4sh33d.medmanager.MedJobBroadcastReceiver;
import com.r4sh33d.medmanager.models.Interval;
import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.database.MedicationDBHelper;
import com.r4sh33d.medmanager.datepickers.DatePickerFragment;
import com.r4sh33d.medmanager.datepickers.TimePickerFragment;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Constants;
import com.r4sh33d.medmanager.utility.LocalData;
import com.r4sh33d.medmanager.utility.Utils;

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
    EditText medicationNameEditText;
    @BindView(R.id.medication_description_edit_text)
    EditText medicationDescriptionEditText;
    @BindView(R.id.medication_quantity_edit_text)
    EditText medicationQuantityEditText;
    @BindView(R.id.starting_date_value)
    TextView startingDateValue;
    @BindView(R.id.ending_date_value)
    TextView endingDateValue;
    @BindView(R.id.medication_interval_hour_edit_text)
    TextView medicationIntervalEditText;
    @BindView(R.id.starting_time_value)
    TextView startingTimeValue;
    @BindView(R.id.medication_interval_spinner)
    Spinner medicationIntervalSpinner;
    Calendar startingDateCalender, endingDateCalender;
    AddMedicationContract.Presenter addMedicationPresenter;
    String startDate, startTime, endDate;


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
        addMedicationPresenter = new AddMedicationPresenter(this);
        startingDateCalender = Calendar.getInstance();
        endingDateCalender = Calendar.getInstance();
        Utils.setCalenderDefault(startingDateCalender);
        Utils.setCalenderDefault(endingDateCalender);
        prepareSpinner();
    }

    void prepareSpinner() {
        ArrayAdapter<Interval> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, LocalData.intervalArrayList);
        medicationIntervalSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.starting_date_value)
    void onClickStartingDate() {
        DialogFragment newFragment = DatePickerFragment.newInstance(true , true);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.starting_time_value)
    void onClickStartingTimeValue() {
        TimePickerFragment newFragment = TimePickerFragment.newInstance(true);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "timePicker");
    }


    @OnClick(R.id.ending_date_value)
    void onClickEndingDateValue() {
        DialogFragment newFragment = DatePickerFragment.newInstance(false , true);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    @Override
    public void onMedicationInsertedToDb(Medication medication) {
        Log.d(TAG , "OnMedication inserted " + medication);
        AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), MedJobBroadcastReceiver.class);
        intent.putExtra(Constants.KEY_MEDICATIO_DB_ROW_ID , medication.dbRowId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),
                (int) medication.dbRowId, //Realistically, our db will never reach Integer.MAX_VALUE
                intent, 0);
        addMedicationPresenter.scheduleNotificationJob(alarmMgr , medication , alarmIntent);
    }

    @Override
    public void moveToNextStep() { }

    @OnClick(R.id.button)
    void onClickSave() {
        Interval interval = (Interval) medicationIntervalSpinner.getSelectedItem();
        if (!validateEditTexts(medicationNameEditText)) {
            showToast("Please enter medication name ");
            return;
        }
        if (!validateEditTexts(medicationQuantityEditText)) {
            showToast("Please enter quantity to proceed ");
            return;
        }
        if (TextUtils.isEmpty(startDate)) {
            showToast("Please enter start date to proceed");
            return;
        }
        if (TextUtils.isEmpty(startTime)) {
            showToast("Please enter start time to proceed");
            return;
        }
        if (TextUtils.isEmpty(endDate)) {
            showToast("Please enter end date to proceed");
            return;
        }

        Medication medication = new Medication(medicationNameEditText.getText().toString(),
                medicationDescriptionEditText.getText().toString(),
                medicationQuantityEditText.getText().toString(),
                startingDateCalender.getTimeInMillis(),
                endingDateCalender.getTimeInMillis(),
                interval.getTimeInMilliseconds());
        Log.d(TAG, "The values are " + medication);
        addMedicationPresenter.addMedicationToDb(medication, new MedicationDBHelper(getContext()));
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onDateSet(boolean isStartDate, DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        if (isStartDate) {
            startingDateCalender.set(year, month, dayOfMonth);
            startDate = month_date.format(startingDateCalender.getTime());
            startingDateValue.setText(startDate);
        } else {
            // End date
            endingDateCalender.set(year, month, dayOfMonth);
            //this calender instance is from 00:00 am this 'day of the month',
            // we want our alarms to still be valid till the end of that day ,
            //so shift by 24 hours
            endingDateCalender.set(Calendar.HOUR_OF_DAY, 24);
            endDate = month_date.format(endingDateCalender.getTime());
            endingDateValue.setText(endDate);
        }
    }

    public void onTimeSet(boolean isStartTime, TimePicker view, int hourOfDay, int minutes) {
        if (isStartTime) {
            startingDateCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startingDateCalender.set(Calendar.MINUTE, minutes);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            startTime = dateFormat.format(startingDateCalender.getTime());
            startingTimeValue.setText(startTime);
        }
    }

    public static boolean validateEditTexts(EditText... editTexts) {
        for (EditText newEdittext : editTexts) {
            if (newEdittext.getText().toString().trim().length() < 1) {
                newEdittext.setError("This Field is required");
                return false;
            }
        }
        return true;
    }
}
