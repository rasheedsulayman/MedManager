package com.r4sh33d.medmanager.utility;


import com.r4sh33d.medmanager.Interval;

import java.util.ArrayList;

public class LocalData {
    public static ArrayList<Interval> intervalArrayList;

    static {
        intervalArrayList = new ArrayList<>();
        for (double i = 0.5 ; i <= 24 ; i += 0.5 ){
            intervalArrayList.add(new Interval("Every "  + i + " hours ", i ));
        }
    }
}
