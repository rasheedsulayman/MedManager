package com.r4sh33d.medmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Constants;
import com.r4sh33d.medmanager.utility.Utils;

public class MedJobBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Medication medication  = intent.getParcelableExtra(Constants.KEY_MEDICATION_BUNDLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if ((medication.startTime + medication.interval) < medication.endTime){

        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                (int) medication.dbRowId, //Realistically, our db will never reach Integer.MAX_VALUE
                intent, 0);

        if (Utils.isKitKatAndAbove()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, medication.startTime, alarmIntent);
        }
    }



    void sendNotification(){

    }

}
