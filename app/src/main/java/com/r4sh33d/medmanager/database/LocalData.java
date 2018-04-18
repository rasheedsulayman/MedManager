package com.r4sh33d.medmanager.database;


import android.util.Log;

import com.r4sh33d.medmanager.models.Interval;

import java.util.ArrayList;

public class LocalData {
    private static final String TAG = LocalData.class.getSimpleName();
    public static ArrayList<Interval> intervalArrayList;

    static {
        intervalArrayList = new ArrayList<>();
         Log.d(TAG , "The interval is " + (0.5/60.0f));
        intervalArrayList.add(new Interval("Every 30 seconds", (0.5/60.0f)));
        intervalArrayList.add(new Interval("Every 5 minutes" , (5/60.0f)));
        intervalArrayList.add(new Interval("Every 10 minutes" , (10/60.0f)));
        for (double i = 0.5 ; i <= 24 ; i += 0.5 ){
            intervalArrayList.add(new Interval("Every "  + i + " hours ", i ));
        }
    }
}
