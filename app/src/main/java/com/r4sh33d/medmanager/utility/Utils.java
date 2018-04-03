package com.r4sh33d.medmanager.utility;

import android.os.Build;

public class Utils {

    public static boolean isKitKatAndAbove(){
      return  Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
