package com.r4sh33d.medmanager.brodcastrecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.r4sh33d.medmanager.database.MedicationDBHelper;
import com.r4sh33d.medmanager.database.MedicationLoader;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Constants;
import com.r4sh33d.medmanager.utility.Utils;

import java.util.ArrayList;

public class MedBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MedicationDBHelper medicationDBHelper = new MedicationDBHelper(context);
            SQLiteDatabase db = medicationDBHelper.getReadableDatabase();
            ArrayList<Medication> activeMedications = MedicationLoader.getMedicationsList(db, MedicationLoader.ACTIVE_MEDICATION_SELECTION);
            for (Medication medication : activeMedications){
                long scheduleTime  = medication.nextRingTime;
                long currentTime = System.currentTimeMillis();
                while (scheduleTime < currentTime){
                    scheduleTime += medication.interval;
                }
                if (scheduleTime < medication.endTime && scheduleTime > currentTime){
                    Intent alarmIntent = new Intent(context, MedJobBroadcastReceiver.class);
                    intent.putExtra(Constants.KEY_MEDICATIO_DB_ROW_ID , medication.dbRowId);
                    Utils.scheduleAlarm(medication , context  , alarmIntent , scheduleTime);
                }
            }
            db.close();
        }

    }
}
