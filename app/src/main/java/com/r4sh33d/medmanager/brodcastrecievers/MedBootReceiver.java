package com.r4sh33d.medmanager.brodcastrecievers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.r4sh33d.medmanager.database.MedicationDBHelper;
import com.r4sh33d.medmanager.database.MedicationLoader;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Constants;
import com.r4sh33d.medmanager.utility.Utils;

import java.util.ArrayList;

public class MedBootReceiver extends BroadcastReceiver{
    private static final String TAG = MedBootReceiver.class.getSimpleName();
    //The medication Alarms are cleared when device turns off, try to reschedule them here again.
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Toast.makeText(context, "MedManager Boot completed", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onBootRecieved");
            MedicationDBHelper medicationDBHelper = new MedicationDBHelper(context);
            SQLiteDatabase db = medicationDBHelper.getReadableDatabase();
            ArrayList<Medication> activeMedications = MedicationLoader.getMedicationsList(db,
                    MedicationLoader.ACTIVE_MEDICATION_SELECTION);
            for (Medication medication : activeMedications){
                long scheduleTime  = medication.nextRingTime;
                long currentTime = System.currentTimeMillis();
                while (scheduleTime < currentTime){
                    scheduleTime += medication.interval;
                }
                //check if the schedule time is still in bound
                if (scheduleTime < medication.endTime && scheduleTime > currentTime){
                    Intent alarmIntent = new Intent(context, MedJobBroadcastReceiver.class);
                    alarmIntent.putExtra(Constants.KEY_MEDICATION_DB_ROW_ID , medication.dbRowId);
                    Utils.scheduleAlarm(medication , context  , alarmIntent , scheduleTime);
                }
            }
            db.close();
        }
    }
}
