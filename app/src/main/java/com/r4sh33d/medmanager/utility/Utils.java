package com.r4sh33d.medmanager.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.EditText;

import com.r4sh33d.medmanager.brodcastrecievers.MedJobBroadcastReceiver;
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

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null,
                null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
