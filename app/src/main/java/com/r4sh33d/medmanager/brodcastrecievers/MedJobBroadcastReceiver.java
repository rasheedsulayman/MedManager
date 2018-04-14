package com.r4sh33d.medmanager.brodcastrecievers;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.r4sh33d.medmanager.R;
import com.r4sh33d.medmanager.database.MedicationDBHelper;
import com.r4sh33d.medmanager.database.MedicationLoader;
import com.r4sh33d.medmanager.models.Medication;
import com.r4sh33d.medmanager.utility.Constants;
import com.r4sh33d.medmanager.utility.Utils;

public class MedJobBroadcastReceiver extends BroadcastReceiver {
    private  static final String TAG = MedJobBroadcastReceiver.class.getSimpleName();
    private static final int NOTIFICATION_ID = 100;
    private static final String NOTIFICATION_CHANEL_ID = "MedManager channel ";

    @Override
    public void onReceive(Context context, Intent intent) {
         Log.d(TAG , "Broadcast reciever onRecieved called");
        long medicationRowId  = intent.getLongExtra(Constants.KEY_MEDICATIO_DB_ROW_ID, -1);
        MedicationDBHelper medicationDBHelper = new MedicationDBHelper(context);
        SQLiteDatabase db = medicationDBHelper.getWritableDatabase();

        if (medicationRowId != -1) {
            Medication medication = MedicationLoader.getMedicationInfoWithId(medicationRowId , db);
            //send Notification here
            Log.d(TAG , "Medication gotten from the DB " + medication);
            sendNotification(medication , context);
            long newRingTime = System.currentTimeMillis() + medication.interval;
            if ( newRingTime < medication.endTime){
                //if we are still in bound, schedule another alarm
                Utils.scheduleAlarm(medication , context , intent , newRingTime);
                MedicationLoader.updateNextRingTime(medicationRowId , newRingTime, db);
            }
        }
    }

    private void sendNotification(Medication medication, Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setUPNotificationChannel(notificationManager);
        }
        notificationManager.notify(NOTIFICATION_ID, getNotification(medication , context));
        Log.d(TAG, "About to send notification");
    }

    public Notification getNotification(Medication medication , Context context) {
    /*    Intent intent = new Intent(this, NotificationDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_OBJECT_ARGS, fcmNotification);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*/
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(medication.name + " Medication reminder ")
                .setContentText("This is to remind you to take your medication : " + medication.name)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(soundUri)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setUPNotificationChannel(NotificationManager notificationManager) {
        CharSequence name = "Med Manager Channel";
        String description = "This Notification Channel remind you of your scheduled medications using the Med Manager App";
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID, name,
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }
}
