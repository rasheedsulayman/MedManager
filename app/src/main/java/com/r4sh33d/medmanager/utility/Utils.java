package com.r4sh33d.medmanager.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.r4sh33d.medmanager.models.Interval;
import com.r4sh33d.medmanager.models.Medication;

import java.util.ArrayList;
import java.util.Calendar;

public class Utils {

    public static boolean isKitKatAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static void scheduleAlarm(Medication medication, Context context, Intent intent, long triggerTime) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                (int) medication.dbRowId, //Realistically, our db will never reach Integer.MAX_VALUE
                intent, 0);
        if (Utils.isKitKatAndAbove()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, alarmIntent);
        }
    }

    public static void setCalenderDefault(Calendar calendar) {
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.HOUR_OF_DAY, 0); //UTC offset
    }

    public static int getSpinnerIndexFromMedicationsList(long intervalTime, ArrayList<Interval> intervals) {
        for (int i = 0; i < intervals.size(); i++) {
            if (intervals.get(i).getTimeInMilliseconds() == intervalTime) {
                return i;
            }
        }
        return 0;
    }
}
