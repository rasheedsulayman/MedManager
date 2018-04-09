package com.r4sh33d.medmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

public class MonthOnlyPickerDialog extends DatePickerDialog implements DatePicker.OnDateChangedListener {

    public MonthOnlyPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(
            context,
            Build.VERSION.SDK_INT >= 21 ? R.style.DialogTheme : 0,
            callBack,
            year,
            monthOfYear,
            dayOfMonth
        );
        init(context);
    }

    private void init(Context context) {
        setTitle("");
        getDatePicker().setCalendarViewShown(false);
        int day = context.getResources().getIdentifier("android:id/day", null, null);
        if(day != 0){
            View dayPicker = getDatePicker().findViewById(day);
            if(dayPicker != null){
                dayPicker.setVisibility(View.GONE);
            }
        }
    }

    public void onDateChanged(DatePicker view, int year, int month, int day) { }
}